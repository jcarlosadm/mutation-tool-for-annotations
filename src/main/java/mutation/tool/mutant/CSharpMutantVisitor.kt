package mutation.tool.mutant

import mutation.tool.operator.CSharpOperator
import mutation.tool.util.xml.NodeVisitor
import org.w3c.dom.Node

class CSharpMutateVisitor(private val cSharpOperator: CSharpOperator): NodeVisitor() {

    override fun visitClass(node: Node) {
        if (cSharpOperator.visitClass(node)) cSharpOperator.lock()
    }

    override fun visitMethod(node: Node) {
        if (cSharpOperator.visitMethod(node)) cSharpOperator.lock()
    }

    override fun visitProperty(node: Node) {
        if (cSharpOperator.visitProperty(node)) cSharpOperator.lock()
    }

    override fun visitParameter(node: Node) {
        if (cSharpOperator.visitParameter(node)) cSharpOperator.lock()
    }
}