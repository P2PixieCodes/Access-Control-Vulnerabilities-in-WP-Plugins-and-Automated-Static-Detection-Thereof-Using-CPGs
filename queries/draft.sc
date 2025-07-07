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