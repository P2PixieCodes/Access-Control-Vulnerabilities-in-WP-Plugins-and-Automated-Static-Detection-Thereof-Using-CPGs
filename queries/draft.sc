/* WIP SCALA FILE */

// ...

/*
Joern Queries that I haven't translated to SCALA yet

Find every CALL of `is_admin`:
    cpg.call.name("is_admin")
    cpg.method("is_admin").callIn

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

Find every CONTROL_STRUCTURE that uses `is_admin` in its condition:
    cpg.controlStructure.where(_.condition.repeat(_.astChildren)(_.whilst(_.not(_.isCall.name("is_admin")))))
    cpg.controlStructure.where(_.condition.or(_.isCall.name("is_admin"),_.repeat(_.astChildren)(_.until(_.isCall.name("is_admin")))))
Find every CALL of `is_admin` that is used in a CONTROL_STRUCTURE condition:
    cpg.controlStructure.condition.repeat(_.astChildren)(_.whilst(_.not(_.isCall.name("is_admin"))))
    cpg.controlStructure.condition.or(_.isCall.name("is_admin"),_.repeat(_.astChildren)(_.until(_.isCall.name("is_admin"))))

Find every CALL of `<operator>.conditional` (inline condition) that uses `is_admin`:
    cpg.call.name("<operator>.conditional").where(_.astChildren.isExpression.argumentIndex(1).repeat(_.astChildren)(_.whilst(_.not(_.isCall.name("is_admin")))))
    cpg.call.name("<operator>.conditional").where(_.astChildren.isExpression.argumentIndex(1).or(_.isCall.name("is_admin"),_.repeat(_.astChildren)(_.until(_.isCall.name("is_admin")))))
Find every inline condition that contains `is_admin`:
    cpg.call.name("<operator>.conditional").astChildren.isExpression.argumentIndex(1).repeat(_.astChildren)(_.whilst(_.not(_.isCall.name("is_admin"))))

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

Find the CFG-successor for every CALL of `is_admin` the is the entire CONDITION expression of a CONTROL_STRUCTURE:
    cpg.call.name("is_admin").where(_.conditionIn).and(_.astParent.isControlStructure).cfgNext.where(_.repeat(_.astParent)(_.until(_.astParent.is(cpg.call.name("is_admin").where(_.conditionIn).astParent.isControlStructure.l(0)))).filter(_.order == 2)).p

Find every CALL of `submit_button`:
    cpg.call.name("submit_button")

Find every CALL of submit_button that is guaranteed to run during the execution of the containing method:
    cpg.call.name("submit_button").filter(node => node.method.methodReturn.dominatedBy.contains(node)).p

Find every CALL of `is_admin` that is reachable:
    cpg.call.name("is_admin").where(_.cfgPrev)


*/

/*
JOERN QUERIES — NOT MINE
    cpg.method.filter(node => node.isExternal == true)

*/