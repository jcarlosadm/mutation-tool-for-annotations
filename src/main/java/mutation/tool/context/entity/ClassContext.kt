package mutation.tool.context.entity

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import mutation.tool.context.InsertionPoint
import mutation.tool.context.adapter.AnnotationAdapter
import mutation.tool.context.adapter.ModifierAdapter
import mutation.tool.util.xml.getAllTagNodes
import mutation.tool.util.xml.getTagNode
import org.w3c.dom.Node

// TODO move to context package
class ClassContext:Context {
    override val name:String
    override val beginLine:Int
    override val beginColumn:Int
    override val annotations = mutableListOf<AnnotationAdapter>()
    override val accessModifiers = mutableListOf<ModifierAdapter>()
    override val parameters: List<ParameterContext>? = null
    override val returnType: String? = null
    override val type: String? = null
    private val stringRepresentation: String

    constructor(classOrInterfaceDeclaration: ClassOrInterfaceDeclaration) {
        this.name = classOrInterfaceDeclaration.nameAsString
        this.beginLine = classOrInterfaceDeclaration.range.get().begin.line
        this.beginColumn = classOrInterfaceDeclaration.range.get().begin.column
        this.stringRepresentation = classOrInterfaceDeclaration.toString()

        for (annotation in classOrInterfaceDeclaration.annotations) {
            this.annotations.add(AnnotationAdapter(annotation))
        }
        for (modifier in classOrInterfaceDeclaration.modifiers) {
            this.accessModifiers.add(ModifierAdapter(modifier))
        }
    }

    constructor(node: Node) {
        val nameNode = getTagNode(node, "name", false)!!
        this.name = nameNode.textContent
        this.beginLine = Integer.parseInt(nameNode.attributes.getNamedItem("pos:line").textContent)
        this.beginColumn = Integer.parseInt(nameNode.attributes.getNamedItem("pos:column").textContent)
        this.stringRepresentation = node.textContent

        for (attrNode in getAllTagNodes(node, "attribute", listOf("decl_stmt", "function", "parameter"))) {
            this.annotations.add(AnnotationAdapter(attrNode))
        }

        val mod = getTagNode(node, "specifier", false)
        if (mod != null) this.accessModifiers.add(ModifierAdapter(mod))
    }

    override fun getInsertionPoint(): InsertionPoint = InsertionPoint.CLASS

    override fun toString(): String = this.stringRepresentation
}