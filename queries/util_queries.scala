import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks.{break,breakable}

import io.shiftleft.codepropertygraph.generated.*
import io.shiftleft.codepropertygraph.generated.nodes.*
import io.shiftleft.codepropertygraph.generated.help.*
import io.shiftleft.codepropertygraph.generated.language.*
import io.shiftleft.codepropertygraph.generated.accessors.*
import io.shiftleft.semanticcpg.*
import io.shiftleft.semanticcpg.language.*
import flatgraph.traversal.*

/**
  * Given a file <global> method, get all includes/requires of the corresponding file.
  *
  * @param cpg
  *     the CPG instance
  * @param methods
  *     an `Iterator[Method]` which may or may not contain `filename.php:<global>` methods
  * @return 
  *     an `Iterator[Call]` of `include`/`require` statements corresponding to any `<global>` methods; 
  *     if there are no `<global>` methods, the iterator is empty
  */
def get_inclusion_calls(cpg: Cpg, methods: Iterator[Method]): Iterator[Call] = {

    // we assume that files' global methods are named `filename.php:<global>`
    val file_methods = methods.filter(_.name.contains("<global>")).l // fsr this has to be a list - if it's an iterator, it doesn't want to work?
    
    val all_file_calls = cpg.call.or(_.name("include"),_.name("include_once"),_.name("require"),_.name("require_once")).l

    // we assume that the entire filename is passed as a string literal to the statement
    def relevant_file_calls = all_file_calls.iterator.filter(node => file_methods.iterator.exists(method_node => { 
        node.code.contains(method_node.filename)
        || node.code.contains(method_node.filename.substring(method_node.filename.lastIndexOf("/")+1)) // case where the filename without path is passed
    }))

    // TODO: case where the filename/path is dynamically constructed
    
    relevant_file_calls
}

/**
  * Given a filename, get the file's `<global>` method.
  *
  * @param cpg
  *     the CPG instance
  * @param file_name
  *     the given filename as a `String`
  * @return
  *     an `Iterator[Method]` with the corresponding `<global>` file method;
  *     if there is no file with that name, the iterator is empty
  */
def get_entry_method(cpg: Cpg, file_name: String): Iterator[Method] = {
    cpg.file.name(file_name).namespaceBlock.typeDecl.method
    //cpg.method.fullName(file_name + ":<global>")
}

/**
  * Filter the given nodes for those that are reachable within the method.
  * 
  * (Note: this does not check for unreachable code due to always-false conditions)
  *
  * @param nodes
  *     an `Iterator[? <: AstNode]`
  * @return
  *     an `Iterator[? <: AstNode]` which contains only those nodes that are reachable
  */
def is_part_of_containing_methods_execution(nodes: Iterator[? <: AstNode]): Iterator[? <: AstNode] = {
    // assumption that we are always working with CALL nodes

    nodes.isCfgNode.filter(node => 
        // is part of method CFG
        node.method.dominates.exists(_.equals(node)) 
        // add potential other possibilities like this:
        //|| <condition>
    )
}

/**
  * Finds callback uses of the given methods.
  *
  * @param cpg
  *     the CPG instance
  * @param methods
  *     an `Iterator[Method]` with methods which may or may not be passed as callbacks
  * @return
  *     an `Iterator[Call]` which contains method calls that take callbacks for any of the given methods
  */
def get_calls_via_callbacks(cpg: Cpg, methods: Iterator[Method]): Iterator[Call] = {
    // get names to search for (ignore file methods)
    val method_names = methods.whereNot(_.name("<global>")).name.l

    // (incomplete) list of methods that take callbacks
    val callback_methods = Map(
        "add_action" -> 2, 
        "add_options_page" -> 5
    )

    val callback_calls_wip = cpg.call.filter(node => callback_methods.keys.exists(_.contains(node.name))).l
    
    val callback_calls = cpg.call
        // get relevant methods
        .filter(node => callback_methods.keys.exists(_.contains(node.name)))
        // filter relevant CALLs by identifying relevant METHOD-names in corresponding callbacks
        .filter(node => node.argument(callback_methods(node.name)) // get callback
            match { // get method name according to callback type
    
                // "classname::methodname" as string (LITERAL) -- only possible for static methods
                case x if x.isLiteral && x.code.contains("::")
                    =>  method_names.exists(
                            name => x.code.endsWith("::" + name)
                            // TODO: also check if classname matches method's containing/inheriting TYPE_DECL
                        )
                // "methodname" as string (LITERAL)
                case x if x.isLiteral
                    =>  method_names.exists(name => x.code.contains(name))
                
                // callback using array()
                // `x.astChildren.order(3).cast[Call].argument(2)` reveals either object (IDENTIFIER) or (only if method is static) classname (LITERAL) // TODO
                // `x.astChildren.order(2).cast[Call].argument(2)` reveals method name (LITERAL)
                case x if x.isBlock
                    =>  method_names.exists(
                            name => x.astChildren.order(3).cast[Call].argument(2).next.code.contains(name)
                            // TODO: handle other argument
                                // TODO: check if IDENTIFIER's object is instance of method's containing/inheriting TYPE_DECL
                                // TODO: check if LITERAL matches method's containing inheriting TYPE_DECL
                        )
                
                // object with `__invoke` method (IDENTIFIER)
                case x if x.isIdentifier
                    =>  false 
                        // TODO: check if IDENTIFIER's object is instance of a class with defined `__invoke` method
                
                // unknown type of callback ?
                case _
                    =>  false
            }
        )
    callback_calls
}

/**
  * Finds calls of methods which were defined via anonymous function variable assignment.
  *
  * @param cpg
  *     the CPG instance
  * @param methods
  *     an `Iterator[Method]` of anonymous functions
  * @return
  *     an `Iterator[Call]` which contains calls to relevant anonymous functions
  */
def get_calls_via_variable_assignment(cpg: Cpg, methods: Iterator[Method]): Iterator[Call | Nothing] = {
    // TODO
    return Iterator()
}

/**
  * Filter the given nodes for those that are part of a condition (for either a control structure or an inline if-statement).
  *
  * @param cpg
  *     the CPG instance
  * @param relevant_nodes
  *     an `Iterator[? <: AstNode]`
  * @return
  *     an `Iterator[? <: AstNode]` which contains only those that are part of a condition
  */
def is_part_of_condition(cpg: Cpg, relevant_nodes: Iterator[? <: AstNode]): Iterator[? <: AstNode] = {
    val compareSet = relevant_nodes.toSet

    cpg.controlStructure.condition.ast.filter(node => compareSet contains node)
    ++ cpg.call.name("<operator>.conditional").argument(1).filter(node => !node.cfgNext.equals(node._argumentIn)).ast.filter(node => compareSet contains node)
    // if there are other possibilities
    //++ <query>
}

/**
  * Finds paths by which execution of any node in group B can lead to execution of any node in group A.
  * 
  * (Note: this does not evaluate expressions in conditions of control structures)
  *
  * @param cpg
  *     the CPG instance
  * @param input_consequence_nodes
  *     an `Iterator[? <: AstNode]` of nodes in group A
  * @param input_cause_nodes
  *     an `Iterator[? <: AstNode]` of nodes in group B
  * @param callResolver
  *     (implicit) - required for successful compilation
  * @return
  *     to be decided ...
  */
def due_to(cpg: Cpg, input_consequence_nodes: Iterator[? <: AstNode], input_cause_nodes: Iterator[? <: AstNode], print: Boolean = false)(implicit callResolver: ICallResolver): List[(? <: AstNode, ? <: AstNode)] = {
    val consequence_nodes = input_consequence_nodes.toSet
    val cause_nodes = input_cause_nodes.toSet

    if print then
        println("STARTING SEARCH AT:")
        consequence_nodes.foreach(node => println(s"    ${Iterator.single(node).p}"))
        println("")
    if print then
        println("SEARCHING FOR:")
        cause_nodes.foreach(node => println(s"    ${Iterator.single(node).p}"))
        println("")

    // save followed steps in List // TODO: come up with better way to store/follow paths
    val result = ListBuffer.empty[(? <: AstNode,? <: AstNode)]
    
    // search backwards from consequence to find a cause (bc less potential branching)
    var search_set: Set[? <: AstNode] = consequence_nodes
    breakable {
        while search_set.size > 0 do
            if print && !search_set.equals(consequence_nodes) then
                println("------------------------------ LOOP")
                println("NEW SEARCH FOR:")
                search_set.foreach(node => println(s"    ${Iterator.single(node).p}"))
                println("")
            var new_search_set: Set[? <: AstNode] = Set()

            // search for direct CFG connection: execution of cause node can lead to execution of target node within the same method
            var found_set: Set[? <: AstNode] = Set()
            search_set.foreach(x => {
                x.isCfgNode.dominatedBy.foreach(y => { 
                    if cause_nodes contains y then {
                        // add "due to" pair to result
                        var temp = (x,y)
                        result += temp
                        // remove node `x` from further searches in current loop
                        found_set = found_set ++ Set(x) // for some reason, using `+` or `incl` does not work
                        if print then println("FOUND - SEE RESULT")
                    }
                })
            })

            // no direct CFG connection -> remaining consequence nodes may be part of *methods* that are called "due to" a cause_node
            search_set = search_set.iterator.filter(node => !found_set.contains(node)).toSet

            // check: nodes must be reachable within method in order for method call to be relevant
            search_set = is_part_of_containing_methods_execution(search_set.iterator).toSet

            if search_set.size <= 0 then break 

            if print then
                println("SEARCHING FOR CALLS OF METHODS:")
                search_set.isCfgNode.foreach(node => {
                    if !node.equals(node.method) then println(s"    ${Iterator.single(node.method).p}")
                })
                println("")
            /*
            // TEMPORARY ADDITION TO MAKE CLEAR THE CONNECTION BETWEEN THE NODE AND THE METHOD I FIND IT IN
            search_set.isCfgNode.foreach(node => {
                if !node.equals(node.method) then {
                    var temp = (node, node.method)
                    result += temp
                }
            })
            */

            // search for all nodes
            search_set.isCfgNode.method.dedup.foreach(method_node => {
                var found_0: Boolean = false
                var found_1: Boolean = false
                var found_2: Boolean = false
                var found_3: Boolean = false
                method_node match
                    // anonymous function
                    case y if y.name.contains("<lambda>") => {
                        new_search_set = new_search_set ++ get_calls_via_variable_assignment(cpg, Iterator.single(y)).toSet
                        if print then
                            found_0 = get_calls_via_variable_assignment(cpg, Iterator.single(y)).toSet.size > 0
                        /*
                        // TEMPORARY ADDITION TO MAKE CLEAR THE CONNECTION BETWEEN THE METHOD AND THE NODE THAT CALLS IT
                        get_calls_via_variable_assignment(cpg, Iterator.single(y)).toSet.foreach(callnode => {
                            var temp = (method_node,callnode)
                            result += temp
                        })
                        */ 
                    }
                    // global file function
                    case y if y.name.equals("<global>") => { 
                        new_search_set = new_search_set ++ get_inclusion_calls(cpg, Iterator.single(y)).toSet 
                        if print then
                            found_1 = get_inclusion_calls(cpg, Iterator.single(y)).toSet.size > 0
                        /*
                        // TEMPORARY ADDITION TO MAKE CLEAR THE CONNECTION BETWEEN THE METHOD AND THE NODE THAT CALLS IT
                        get_inclusion_calls(cpg, Iterator.single(y)).toSet.foreach(callnode => {
                            var temp = (method_node,callnode)
                            result += temp
                        })
                        */
                    }
                    // regular function
                    case y => {
                        new_search_set = new_search_set 
                            ++ y.callIn.toSet // regular call -> CPG has `CALL`-edge
                            ++ get_calls_via_callbacks(cpg, Iterable.single(y)).toSet // via callback
                        if print then
                            found_2 = y.callIn.toSet.size > 0
                            found_3 = get_calls_via_callbacks(cpg, Iterable.single(y)).toSet.size > 0
                        /*
                        // TEMPORARY ADDITION TO MAKE CLEAR THE CONNECTION BETWEEN THE METHOD AND THE NODE THAT CALLS IT
                        y.callIn.toSet.foreach(callnode => {
                            var temp = (method_node,callnode)
                            result += temp
                        })
                        // TEMPORARY ADDITION TO MAKE CLEAR THE CONNECTION BETWEEN THE METHOD AND THE NODE THAT CALLS IT
                        get_calls_via_callbacks(cpg, Iterable.single(y)).toSet.foreach(callnode => {
                            var temp = (method_node,callnode)
                            result += temp
                        })
                        */
                    }
                if print then
                    if (found_0 || found_1 || found_2 || found_3) then
                        println("FOUND CALLS")
                        if found_0 then get_calls_via_variable_assignment(cpg, Iterator.single(method_node)).foreach(node => println(s"    ${Iterator.single(node).p}"))
                        if found_1 then get_inclusion_calls(cpg, Iterator.single(method_node)).foreach(node => println(s"    ${Iterator.single(node).p}"))
                        if found_2 then method_node.callIn.foreach(node => println(s"    ${Iterator.single(node).p}"))
                        if found_3 then get_calls_via_callbacks(cpg, Iterable.single(method_node)).foreach(node => println(s"    ${Iterator.single(node).p}"))
                        println("")
                    else 
                        println("FOUND NO CALLS")
                        println("")

            })

            // prepare for next loop
            search_set = new_search_set
    }

    result.to(List)

}