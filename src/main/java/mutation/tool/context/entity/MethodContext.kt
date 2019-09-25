package mutation.tool.context.entity

import com.github.javaparser.ast.body.MethodDeclaration
import mutation.tool.context.InsertionPoint
import mutation.tool.context.adapter.AnnotationAdapter
import mutation.tool.context.adapter.ModifierAdapter
import mutation.tool.util.xml.getAllTagNodes
import mutation.tool.util.xml.getTagNode
import org.w3c.dom.Node

// TODO move to context package
class MethodContext:Context {
    private var name:String
    private var beginLine = 0
    private var beginColumn = 0
    private var annotations = mutableListOf<AnnotationAdapter>()
    private var modifiers = mutableListOf<ModifierAdapter>()
    private var parameters = mutableListOf<ParameterContext>()
    private var returnType:String

    constructor(methodDeclaration: MethodDeclaration) {
        this.name = methodDeclaration.nameAsString
        this.beginLine = methodDeclaration.range.get().begin.line
        this.beginColumn = methodDeclaration.range.get().begin.column
        this.returnType = methodDeclaration.typeAsString

        for (annotation in methodDeclaration.annotations) {
            annotations.add(AnnotationAdapter(annotation))
        }

        for (modifier in methodDeclaration.modifiers) {
            modifiers.add(ModifierAdapter(modifier))
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

        for (attrNode in getAllTagNodes(node, "attribute", listOf("class", "decl_stmt", "parameter"))) {
            annotations.add(AnnotationAdapter(attrNode))
        }

        this.modifiers.add(ModifierAdapter(getTagNode(node, "specifier", false)!!))

        for (paramNode in getAllTagNodes(node, "parameter", listOf("class", "decl_stmt"))) {
            this.parameters.add(ParameterContext(paramNode))
        }
    }

    override fun getName(): String = this.name
    override fun getBeginLine(): Int = this.beginLine
    override fun getBeginColumn(): Int = this.beginColumn
    override fun getAnnotations(): List<AnnotationAdapter> = this.annotations
    override fun getAccessModifiers(): List<ModifierAdapter>? = this.modifiers
    override fun getParameters(): List<ParameterContext>? = this.parameters
    override fun getReturnType(): String? = this.returnType
    override fun getType(): String? = null
    override fun getInsertionPoint(): InsertionPoint = InsertionPoint.METHOD
}