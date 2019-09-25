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
    private var name:String
    private var beginLine = 0
    private var beginColumn = 0
    private var annotations = mutableListOf<AnnotationAdapter>()
    private var accessModifiers = mutableListOf<ModifierAdapter>()
    private var string: String

    constructor(classOrInterfaceDeclaration: ClassOrInterfaceDeclaration) {
        this.name = classOrInterfaceDeclaration.nameAsString
        this.beginLine = classOrInterfaceDeclaration.range.get().begin.line
        this.beginColumn = classOrInterfaceDeclaration.range.get().begin.column
        this.string = classOrInterfaceDeclaration.toString()

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
        this.string = node.textContent

        for (attrNode in getAllTagNodes(node, "attribute", listOf("decl_stmt", "function", "parameter"))) {
            this.annotations.add(AnnotationAdapter(attrNode))
        }

        val mod = getTagNode(node, "specifier", false)
        if (mod != null) this.accessModifiers.add(ModifierAdapter(mod))
    }

    override fun getName(): String = this.name
    override fun getBeginLine(): Int = this.beginLine
    override fun getBeginColumn(): Int = this.beginColumn
    override fun getAnnotations(): List<AnnotationAdapter> = annotations
    override fun getAccessModifiers(): List<ModifierAdapter>? = accessModifiers
    override fun getParameters(): List<ParameterContext>? = null
    override fun getReturnType(): String? = null
    override fun getType(): String? = null
    override fun toString(): String = this.string
    override fun getInsertionPoint(): InsertionPoint = InsertionPoint.CLASS
}