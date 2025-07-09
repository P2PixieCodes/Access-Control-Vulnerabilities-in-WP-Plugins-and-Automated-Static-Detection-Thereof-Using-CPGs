import scala.collection.mutable.ListBuffer
import io.shiftleft.codepropertygraph.generated.*
import io.shiftleft.codepropertygraph.generated.nodes.*
import io.shiftleft.codepropertygraph.generated.help.*
import io.shiftleft.codepropertygraph.generated.language.*
import io.shiftleft.codepropertygraph.generated.accessors.*
import io.shiftleft.semanticcpg.*
import io.shiftleft.semanticcpg.language.*
import flatgraph.traversal.*

def find_vuln_00(cpg: Cpg, print: Boolean = false)(implicit callResolver: ICallResolver) = {
    def sources = cpg.call.name("is_admin")
    def sinks = cpg.call.name("submit_button")

    // discard irrelevant nodes
    val relevant_sources = sources.l // TODO: filter for only those that are automatically reachable?

    // query the vuln
    due_to(cpg,cpg.call.name("submit_button"), relevant_sources.iterator, print)
}