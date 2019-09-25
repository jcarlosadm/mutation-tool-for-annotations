package mutation.tool.util.xml

import org.w3c.dom.Node

fun getTagNode(node: Node, tag: String, recursive: Boolean):Node? {
    if (node.nodeName == tag) { return node }
    for (i in 0 until node.childNodes.length) {
        if (node.childNodes.item(i).nodeName == tag) {
            return node.childNodes.item(i)
        }

        var nodeResult:Node?
        if (recursive) {
            nodeResult = getTagNode(node.childNodes.item(i), tag, recursive)
            if (nodeResult != null) return nodeResult
        }
    }

    return null
}

fun getAllTagNodes(node: Node, tag: String, ignoreTags:List<String>):List<Node> {
    val list = mutableListOf<Node>()
    if (ignoreTags.contains(node.nodeName)) { return list }
    if (node.nodeName == tag) { list.add(node) }
    for (i in 0 until node.childNodes.length) {
        list.addAll(getAllTagNodes(node.childNodes.item(i), tag, ignoreTags))
    }
    return list
}