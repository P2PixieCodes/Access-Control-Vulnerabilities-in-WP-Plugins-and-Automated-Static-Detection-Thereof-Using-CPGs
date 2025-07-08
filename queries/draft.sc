import scala.collection.mutable.ListBuffer
//import $dep.`io.shiftleft::codepropertygraph:1.7.38`
//import io.shiftleft.codepropertygraph.generated.*
//import io.shiftleft.codepropertygraph.generated.nodes.*
//import io.shiftleft.codepropertygraph.generated.help.*
//import $dep.`io.shiftleft::semanticcpg:1.3.522` //https://mvnrepository.com/artifact/io.shiftleft/semanticcpg_3/1.3.522
//import io.shiftleft.semanticcpg.*

/* WIP SCALA FILE */

/** Find every CALL of `is_admin`. */
def findIsAdminCalls = {
    cpg.call.name("is_admin") // returns Iterator[Call]
    //cpg.method("is_admin").callIn
}

/** Find every CONTROL_STRUCTURE that calls the method `<callName>` in its condition. */
def findControlStructuresWhoseConditionCalls(callName: String) = {
    cpg.controlStructure.where(_.condition.repeat(_.astChildren)(_.whilst(_.not(_.isCall.name(callName)))))
    // cpg.controlStructure.where(_.condition.or(_.isCall.name(callname),_.repeat(_.astChildren)(_.until(_.isCall.name(callName)))))
}

/** Find every CALL of method `<callName>` that is part of a CONTROL_STRUCTURE condition. */
def findCallInConditions(callName: String): Iterator[AstNode] = {
    cpg.controlStructure.condition.repeat(_.astChildren)(_.whilst(_.not(_.isCall.name(callName))))
    //cpg.controlStructure.condition.or(_.isCall.name(callName),_.repeat(_.astChildren)(_.until(_.isCall.name(callName))))
}

/** Find every CALL of `<operator>.conditional` (inline if-else) that uses method `<callName>`. */
def findConditionalCalling(callName: String) = {
    cpg.call.name("<operator>.conditional").where(_.astChildren.isExpression.argumentIndex(1).repeat(_.astChildren)(_.whilst(_.not(_.isCall.name(callName)))))
    //cpg.call.name("<operator>.conditional").where(_.astChildren.isExpression.argumentIndex(1).or(_.isCall.name(callName),_.repeat(_.astChildren)(_.until(_.isCall.name(callName)))))
}

/** Find every CALL of method `<callName>` that is part of a `<operator>.conditional` CALL's argument. */
def findCallInConditionals(callName: String) = {
    cpg.call.name("<operator>.conditional").astChildren.isExpression.argumentIndex(1).repeat(_.astChildren)(_.whilst(_.not(_.isCall.name(callName))))
}

def findIncludedFiles(traversal: Iterator[Call]): Iterator[File] = {
    cpg.file.filter(fileNode => traversal.or(_.name("include"),_.name("require"),_.name("include_once"),_.name("require_once")).argument.ast.isLiteral.typeFullName("string").code.exists(_.contains(fileNode.name)))
    //cpg.file.filter(fileNode => importCalls.argument.ast.isLiteral.typeFullName("string").code.exists(_.contains(fileNode.name)))
}

def model_abwp_simple_counter = {

    /* V1: disregarded bc of unnecessary expression evaluation

    // get IF-control structure that only calls `is_admin`
    def source_cs = cpg.controlStructure.isIf.where(_.condition.isCall.name("is_admin")) // Iterator[ControlStructure]
    // follow CFG to true-case (order == 2; see joern/semanticcpg/src/main/scala/io/shiftleft/semanticcpg/language/types/expressions/ControlStructureTraversal.scala)
    def true_case = source_cs.condition.cfgNext.where(_.repeat(_.astParent)(_.until(_.astParent.isControlStructure.within(sourceCS.toSet))).filter(_.order == 2)) // Iterator[CfgNode]
    */

    /* V2: disregarded bc it's easier to go sink->src instead of src->sink
    def isSink(node: AstNode): Boolean = sinks.contains(node)
    // collect all potential sinks in a list
    val findings: ListBuffer[AstNode] = new ListBuffer()
    // check in CFG descendants
    findings.addAll(cfgDescendants.filter(isSink))
    // assumption that file imports
    var called_methods: Iterator[Method] = 
           cfgDescendants.isCall.callee.cast[Method].internal // get called methods
        ++ findIncludedFiles(cfgDescendants).method.name("<global>") // get imported files
    //Iterator.single(cfgDescendants.isCall.callee.cast[Method].internal.l(1)).call.name("require_once").argument.ast.isLiteral.typeFullName("string").code.l.p
    while !called_methods.isEmpty do
        // get new called methods and imported files
        val new_methods = new ListBuffer()
        called_methods.foreach(method => new_methods += method.cfgNode.isCall.callee.cast[Method].internal.l)
        called_methods = new_methods.toList
    findings
    */

    /* V3 */

    def sources: Iterator[Call] = (findCallInConditions("is_admin") ++ findCallInConditionals("is_admin")).cast[Call]
    def cfgDescendants: Iterator[CfgNode] = sources.dominates

    def sinks: Iterator[Method] = cpg.method.name("submit_button")

    // using `calledBy` (see joern/semanticcpg/src/main/scala/io/shiftleft/semanticcpg/language/callgraphextension/MethodTraversal.scala)
    //    NOTE: this won't work on the original due to indirect method calls

    val result0a = sinks.calledBy(sources.method) // directly/transitively called in the same method
    val result0b = sinks.callIn.dominatedBy(sources) // in the same method *and* part of `cfgDescendants` // <- doppelt gemoppelt
    val result1 = sinks.calledBy(cfgDescendants.isCall.callee) // with one method call in-between

    

    // partially inspired by https://jaiverma.github.io/blog/joern-intro Case 2-4

    val indirectCalls = cpg.call.or(_.name("add_action"), _.name("add_options_page"), _.name("add_shortcode")) // indirect method calls
    val importCalls = cpg.call.or(_.name("include"),_.name("require"),_.name("include_once"),_.name("require_once")) // import calls of files

    val sinkCalls = sinks.callIn // direct calls of SINK
    val methods = sinkCalls.method.dedup // methods containing SINK
    
    // TODO: filter indirect calls by method name in arguments
    // TODO: filter import calls by file name in arguments IF current method is `"<global>"`
    //if methods.name then 
    
    //cpg.file.filter(fileNode => importCalls.argument.ast.isLiteral.typeFullName("string").code.exists(_.contains(fileNode.name)))

}

@main def hello() = println("Hello, World!")

/*
Joern Queries that I haven't translated to SCALA

Find every CONTROL_STRUCTURE that only uses `is_admin` as its condition:
    cpg.controlStructure.where(_.condition.isCall.name("is_admin"))
    cpg.call.name("is_admin").where(_.conditionIn).astParent.isControlStructure
Find every CALL of `is_admin` that is the entire CONDITION expression of a CONTROL_STRUCTURE:
    cpg.controlStructure.condition.isCall.name("is_admin")
    cpg.call.name("is_admin").where(_.conditionIn).and(_.astParent.isControlStructure)

Find every CONTROL_STRUCTURE that uses `is_admin` as _part of_ a more complex condition:
    cpg.controlStructure.where(_.condition.repeat(_.astChildren)(_.until(_.isCall.name("is_admin"))))
Find every CALL of `is_admin` in a complex CONTROL_STRUCTURE condition:
    cpg.controlStructure.condition.repeat(_.astChildren)(_.until(_.isCall.name("is_admin")))
    
Find every CONTROL_STRUCTURE with multiple BLOCK children:
    cpg.controlStructure.filter(_.astChildren.isBlock.size > 1)

Find IF-case of <given IF-type CONTROL_STRUCTURE>:
    <...>.whenTrue
    <...>.filter(_.astChildren.isBlock.size > 1).astChildren.order(2)

Find ELSE-case of <given IF-type CONTROL_STRUCTURE>:
    <...>.whenFalse
    <...>.filter(_.astChildren.isBlock.size > 1).astChildren.order(3)

Find IF-block for simple `is_admin` check:
    cpg.call.name("is_admin").where(_.conditionIn).astParent.isControlStructure.whenTrue.isBlock

Find the CFG-successor for every CALL of `is_admin` that is the entire CONDITION expression of a CONTROL_STRUCTURE:
    cpg.call.name("is_admin").where(_.conditionIn).and(_.astParent.isControlStructure).cfgNext.where(_.repeat(_.astParent)(_.until(_.astParent.is(cpg.call.name("is_admin").where(_.conditionIn).astParent.isControlStructure.l(0)))).filter(_.order == 2)).p

Find every CALL of `submit_button`:
    cpg.call.name("submit_button")

Find every CALL of submit_button that is guaranteed to run during the execution of the containing method:
    cpg.call.name("submit_button").filter(node => node.method.methodReturn.dominatedBy.contains(node)).p

Find every CALL of `is_admin` that is reachable:
    cpg.call.name("is_admin").where(_.cfgPrev)

(WIP) Find an indirect call of `is_admin` (via passing the name to another function call):
    cpg.literal.code("\"is_admin\"").argumentIn.<???>



*/

/*
JOERN QUERIES — NOT MINE
Find methods marked as "external" by joern (i.e. missing a method body):
    cpg.method.filter(node => node.isExternal == true)

*/


/**
  * Given a file <global> method, get all includes/requires of the corresponding file.
  *
  * @param methods
  *     an `Iterator[Method]` which may or may not contain `filename.php:<global>` methods
  * @return 
  *     an `Iterator[Call]` of `include`/`require` statements corresponding to any `<global>` methods; 
  *     if there are no `<global>` methods, the iterator is empty
  */
def get_inclusion_calls(methods: Iterator[Method]): Iterator[Call] = {

    // we assume that files' global methods are named `filename.php:<global>`
    def file_methods = methods.filter(_.name.contains("<global>"))
    
    val all_file_calls = cpg.call.or(_.name("include"),_.name("include_once"),_.name("require"),_.name("require_once")).l

    // we assume that the entire filename is passed as a string literal to the statement
    def relevant_file_calls = all_file_calls.iterator.filter(node => global_methods.exists(method_node => node.code.contains(method_nodenode.filename)))

    // TODO: case where the filename/path is dynamically constructed
    
    relevant_file_calls
}

/**
  * Given a filename, get the file's `<global>` method.
  *
  * @param main_file_name
  *     the given filename as a `String`
  * @return
  *     an `Iterator[Method]` with the corresponding `<global>` file method;
  *     if there is no file with that name, the iterator is empty
  */
def get_entry_method(file_name: String): Iterator[Method] = {
    cpg.file(main_file_name).namespaceBlock.typeDecl.method
    //cpg.method.fullName(file_name + ":<global>")
}

/**
  * Filter the given CALLs for those that are reachable within the method.
  * 
  * (Note: this does not check for unreachable code due to always-false conditions)
  *
  * @param calls
  *     an `Iterator[Call]`
  * @return
  *     an `Iterator[Call]` which contains only those nodes that are reachable
  */
def is_part_of_containing_methods_execution(calls: Iterator[Call]): Iterator[Call] = {
    // assumption that we are always working with CALL nodes

    calls.filter(node => 
        // is part of method CFG
        node.method.dominates.exists(_.equals(node)) 
        // add potential other possibilities like this:
        //|| <condition>
    )
}

/**
  * Finds callback uses of the given methods.
  *
  * @param methods
  *     an `Iterator[Method]` with methods which may or may not be passed as callbacks
  * @return
  *     an `Iterator[Call]` which contains method calls that take callbacks for any of the given methods
  */
def get_calls_via_callbacks(methods: Iterator[Method]): Iterator[Call] = {
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
                    => false // TODO
                // "methodname" as string (LITERAL)
                case x if x.isLiteral
                    => method_names.exists(name => x.code.contains(name))
                // callback using array()
                // `x.astChildren.order(3).cast[Call].argument(2)` reveals either object (IDENTIFIER) or (only if method is static) classname (LITERAL) // TODO
                // `x.astChildren.order(2).cast[Call].argument(2)` reveals method name (LITERAL)
                case x if x.isBlock
                    => method_names.exists(name => x.astChildren.order(3).cast[Call].argument(2).next.code.contains(name))
                // object with `__invoke` method (IDENTIFIER)
                case x if x.isIdentifier
                    => false // TODO
                // unknown type of callback ?
                case _
                    => false
            }
        )
    callback_calls
}

/**
  * Filter the given nodes for those that are part of a condition (for either a control structure or an inline if-statement).
  *
  * @param relevant_calls
  *     an `Iterator[AstNode]`
  * @return
  *     an `Iterator[AstNode]` which contains only those that are part of a condition
  */
def is_part_of_condition(relevant_calls: Iterator[AstNode]): Iterator[AstNode] = {
    val compareSet = relevant_calls.toSet

    cpg.controlStructure.condition.ast.filter(node => compareSet contains node)
    ++ cpg.call.name("<operator>.conditional").argument(1).filter(node => !node.cfgNext.equals(node.argumentIn)).ast.filter(node => compareSet contains node)
    // if there are other possibilities
    //++ <query>
}
