/* WIP SCALA FILE */

// ...

/*
Joern Queries that I haven't translated to SCALA yet

Find every CALL of `is_admin`:
    cpg.call.name("is_admin")
    cpg.method("is_admin").callIn

Find every CONTROL_STRUCTURE that only uses `is_admin` as its condition:
    cpg.call.name("is_admin").where(_.conditionIn).astParent.isControlStructure
Find every CALL of `is_admin` that is the entire CONDITION expression of a CONTROL_STRUCTURE:
    cpg.call.name("is_admin").where(_.conditionIn).and(_.astParent.isControlStructure)

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



NOT MINE
    cpg.method.filter(node => node.isExternal == true)

*/