package mutation.tool.util.xml

enum class NodeType(val nodeName: String) {
    CLASS("class"),
    METHOD("function"),
    PARAMETER("parameter"),
    PROPERTY("decl_stmt"),
    ATTRIBUTE("attribute");
}