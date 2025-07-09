import scala.collection.mutable.ListBuffer
import io.shiftleft.codepropertygraph.generated.*
import io.shiftleft.codepropertygraph.generated.nodes.*
import io.shiftleft.codepropertygraph.generated.help.*
import io.shiftleft.codepropertygraph.generated.language.*
import io.shiftleft.codepropertygraph.generated.accessors.*
import io.shiftleft.semanticcpg.*
import io.shiftleft.semanticcpg.language.*
//import flatgraph.traversal.*

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