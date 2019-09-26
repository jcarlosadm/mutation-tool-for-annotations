package mutation.tool.context.entity

import com.github.javaparser.ast.body.FieldDeclaration
import mutation.tool.context.InsertionPoint
import mutation.tool.context.adapter.AnnotationAdapter
import mutation.tool.context.adapter.ModifierAdapter
import mutation.tool.util.xml.getAllTagNodes
import mutation.tool.util.xml.getTagNode
import org.w3c.dom.Node

// TODO move to context package
class PropertyContext:Context {
    override val name: String
    override val beginLine: Int
    override val beginColumn: Int
    override val annotations = mutableListOf<AnnotationAdapter>()
    override val accessModifiers = mutableListOf<ModifierAdapter>()
    override val parameters: List<ParameterContext>? = null
    override val returnType: String? = null
    override val type: String?
    private val stringRepresentation:String

    constructor(fieldDeclaration: FieldDeclaration) {
        this.name = fieldDeclaration.toString()
        this.beginLine = fieldDeclaration.range.get().begin.line
        this.beginColumn = fieldDeclaration.range.get().begin.column
        this.type = fieldDeclaration.elementType.toString()
        this.stringRepresentation = fieldDeclaration.toString()

        for (annotationExpr in fieldDeclaration.annotations) {
            this.annotations.add(AnnotationAdapter(annotationExpr))
        }

        for (modifier in fieldDeclaration.modifiers) {
            this.accessModifiers.add(ModifierAdapter(modifier))
        }
    }

    constructor(node: Node) {
        val declNode = getTagNode(node, "decl", false)!!
        val nameNode = getTagNode(declNode, "name", false)!!
        this.name = nameNode.textContent

        val positionTag = getTagNode(node, "pos:position", false)!!
        this.beginLine = Integer.parseInt(positionTag.attributes.getNamedItem("pos:line").textContent)
        this.beginColumn = Integer.parseInt(positionTag.attributes.getNamedItem("pos:column").textContent)

        val typeNode = getTagNode(declNode, "type", false)!!
        val typeNameNode = getTagNode(typeNode, "name", false)!!
        this.type = typeNameNode.textContent

        this.stringRepresentation = node.textContent

        for (annotation in getAllTagNodes(declNode, "attribute", listOf("class", "parameter", "function"))) {
            this.annotations.add(AnnotationAdapter(annotation))
        }

        val modifierNode = getTagNode(declNode, "specifier", false)!!
        this.accessModifiers.add(ModifierAdapter(modifierNode))
    }

    override fun getInsertionPoint(): InsertionPoint = InsertionPoint.PROPERTY

    override fun toString(): String = this.stringRepresentation
}