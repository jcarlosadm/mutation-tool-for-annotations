package mutation.tool.context

import com.github.javaparser.ast.body.MethodDeclaration
import mutation.tool.context.adapter.AnnotationAdapter
import mutation.tool.context.adapter.ModifierAdapter
import mutation.tool.util.xml.getAllTagNodes
import mutation.tool.util.xml.getTagNode
import org.w3c.dom.Node

class MethodContext: Context {
    override val name:String
    override val beginLine:Int
    override val beginColumn:Int
    override val annotations = mutableListOf<AnnotationAdapter>()
    override val accessModifiers = mutableListOf<ModifierAdapter>()
    override val parameters = mutableListOf<ParameterContext>()
    override val returnType:String
    override val type: String? = null
    private val stringRepresentation: String

    constructor(methodDeclaration: MethodDeclaration) {
        this.name = methodDeclaration.nameAsString
        this.beginLine = methodDeclaration.range.get().begin.line
        this.beginColumn = methodDeclaration.range.get().begin.column
        this.returnType = methodDeclaration.typeAsString
        this.stringRepresentation = methodDeclaration.toString()

        for (annotation in methodDeclaration.annotations) {
            annotations.add(AnnotationAdapter(annotation))
        }

        for (modifier in methodDeclaration.modifiers) {
            accessModifiers.add(ModifierAdapter(modifier))
        }

        for (parameter in methodDeclaration.parameters) {
            parameters.add(ParameterContext(parameter))
        }
    }

    constructor(node: Node) {
        val nameNode = getTagNode(node, "name", false)!!
        this.name = nameNode.nodeName
        this.beginLine = Integer.parseInt(nameNode.attributes.getNamedItem("pos:line").textContent)
        this.beginColumn = Integer.parseInt(nameNode.attributes.getNamedItem("pos:column").textContent)
        val typeNode = getTagNode(node, "type", false)!!
        val typeNameNode = getTagNode(typeNode, "name", false)!!
        this.returnType = typeNameNode.textContent
        this.stringRepresentation = node.textContent

        for (attrNode in getAllTagNodes(node, "attribute", listOf("class", "decl_stmt", "parameter"))) {
            annotations.add(AnnotationAdapter(attrNode))
        }

        this.accessModifiers.add(ModifierAdapter(getTagNode(node, "specifier", false)!!))

        for (paramNode in getAllTagNodes(node, "parameter", listOf("class", "decl_stmt"))) {
            this.parameters.add(ParameterContext(paramNode))
        }
    }

    override fun getInsertionPoint(): InsertionPoint = InsertionPoint.METHOD

    override fun toString(): String = this.stringRepresentation
}