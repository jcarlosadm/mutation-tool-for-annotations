package mutation.tool.util.xml

import org.w3c.dom.Node

abstract class NodeVisitor {
    protected open fun visitClass(node: Node) {}
    protected open fun visitMethod(node: Node) {}
    protected open fun visitProperty(node: Node) {}
    protected open fun visitParameter(node: Node) {}
    protected open fun visitAttribute(node: Node) {}

    fun visit(rootNode: Node) {
        this.visit(rootNode, false, false, false, false, false)
    }

    private fun visit(
            node: Node,
            enterClass: Boolean,
            enterMethod: Boolean,
            enterProp: Boolean,
            enterParam: Boolean,
            enterAtrib: Boolean
    ) {
        val enter = EnterInfo()
        enter.copy(enterClass,enterMethod,enterProp,enterParam,enterAtrib)

        when(node.nodeName) {
            NodeType.CLASS.nodeName -> {
                enter.enterClass = true
                this.visitClass(node)
            }
            NodeType.METHOD.nodeName -> {
                enter.enterMethod = true
                this.visitMethod(node)
            }
            NodeType.PARAMETER.nodeName -> {
                enter.enterParam = true
                this.visitParameter(node)
            }
            NodeType.PROPERTY.nodeName -> {
                enter.enterProp = true
                if (enter.enterClass && !enter.enterMethod) this.visitProperty(node)
            }
            NodeType.ATTRIBUTE.nodeName -> {
                enter.enterAtrib = true
                this.visitAttribute(node)
            }
        }

        for (index in 0 until node.childNodes.length) {
            val childNode = node.childNodes.item(index)
            this.visit(childNode, enter.enterClass,enter.enterMethod,enter.enterProp,enter.enterParam,enter.enterAtrib)
        }
    }
}

private class EnterInfo {
    var enterClass = false
    var enterMethod = false
    var enterParam = false
    var enterProp = false
    var enterAtrib = false

    fun copy(enterClass:Boolean, enterMethod:Boolean, enterProp:Boolean, enterParam:Boolean, enterAtrib:Boolean) {
        this.enterClass = enterClass
        this.enterMethod = enterMethod
        this.enterProp = enterProp
        this.enterParam = enterParam
        this.enterAtrib = enterAtrib
    }
}
