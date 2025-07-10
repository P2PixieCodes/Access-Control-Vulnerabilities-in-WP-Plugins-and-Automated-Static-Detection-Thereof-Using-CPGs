import scala.collection.mutable.ListBuffer
import io.shiftleft.codepropertygraph.generated.*
import io.shiftleft.codepropertygraph.generated.nodes.*
import io.shiftleft.codepropertygraph.generated.help.*
import io.shiftleft.codepropertygraph.generated.language.*
import io.shiftleft.codepropertygraph.generated.accessors.*
import io.shiftleft.semanticcpg.*
import io.shiftleft.semanticcpg.language.*
import flatgraph.traversal.*

def find_vuln_01_a(cpg: Cpg, print: Boolean = false)(implicit callResolver: ICallResolver) = {
    def sources = cpg.call.name("is_admin")
    def sinks = cpg.call.name("echo").filter(node => 
        node.code.contains("javascript") && 
        List("guardar","save","speichern").exists(keyword => node.code.toLowerCase.contains(keyword))
    )
    if sinks.size > 0 then 
        due_to(cpg,sinks,sources,print) 
    else println("could not find any matching sinks")
}

def find_vuln_01_b(cpg: Cpg, filename: String, print: Boolean = false)(implicit callResolver: ICallResolver) = {
// `.html` files are not parsed into the CPG, even if they contain php content
// thus: manually identify files with sink content, and call this function with the filename
    def sources = cpg.call.name("is_admin")
    def sinks = cpg.call.filter(_.code.contains(filename))
    due_to(cpg,sinks,sources,print)
}