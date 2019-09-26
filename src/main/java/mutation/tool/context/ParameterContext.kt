package mutation.tool.context

import com.github.javaparser.ast.body.Parameter
import mutation.tool.context.adapter.AnnotationAdapter
import mutation.tool.context.adapter.ModifierAdapter
import mutation.tool.util.xml.getAllTagNodes
import mutation.tool.util.xml.getTagNode
import org.w3c.dom.Node

class ParameterContext: Context {
    override val name: String
    override val beginLine: Int
    override val beginColumn: Int
    override val annotations = mutableListOf<AnnotationAdapter>()
    override val accessModifiers = mutableListOf<ModifierAdapter>()
    override val parameters: List<ParameterContext>? = null
    override val returnType: String? = null
    override val type: String?
    private val stringRepresentation:String

    constructor(parameter: Parameter) {
        this.name = parameter.nameAsString
        this.beginLine = parameter.range.get().begin.line
        this.beginColumn = parameter.range.get().begin.column
        this.type = parameter.typeAsString
        this.stringRepresentation = parameter.toString()

        for (annotation in parameter.annotations) {
            this.annotations.add(AnnotationAdapter(annotation))
        }
    }

    constructor(node:Node) {
        val declNode = getTagNode(node, "decl", false)!!
        val nameNode = getTagNode(declNode, "name", false)!!
        this.name = nameNode.textContent
        this.beginLine = Integer.parseInt(nameNode.attributes.getNamedItem("pos:line").textContent)
        this.beginColumn = Integer.parseInt(nameNode.attributes.getNamedItem("pos:column").textContent)

        val typeNode = getTagNode(declNode, "type", false)!!
        val typeNameNode = getTagNode(typeNode, "name", false)!!
        this.type = typeNameNode.textContent

        this.stringRepresentation = node.textContent

        for (annotationNode in getAllTagNodes(node, "attribute", listOf("class", "function", "decl_stmt"))) {
            this.annotations.add(AnnotationAdapter(annotationNode))
        }
    }

    override fun getInsertionPoint(): InsertionPoint = InsertionPoint.PARAMETER

    override fun toString(): String = this.stringRepresentation
}
