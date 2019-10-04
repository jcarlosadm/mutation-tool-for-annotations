package mutation.tool.context.visitor

import mutation.tool.context.*
import mutation.tool.util.xml.NodeVisitor
import org.w3c.dom.Node

class ContextCatcherCSharpVisitor : NodeVisitor(), ContextCatcherVisitor {
    override val contexts = mutableListOf<Context>()

    override fun visitClass(node: Node) {
        this.contexts.add(ClassContext(node))
    }

    override fun visitMethod(node: Node) {
        this.contexts.add(MethodContext(node))
    }

    override fun visitProperty(node: Node) {
        this.contexts.add(PropertyContext(node))
    }

    override fun visitParameter(node: Node) {
        this.contexts.add(ParameterContext(node))
    }
}