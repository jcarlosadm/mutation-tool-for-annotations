package mutation.tool.context.entity

import com.github.javaparser.ast.body.Parameter
import mutation.tool.context.InsertionPoint
import mutation.tool.context.adapter.AnnotationAdapter
import mutation.tool.context.adapter.ModifierAdapter
import org.w3c.dom.Node

// TODO move to context package
class ParameterContext:Context {
    override val name: String
        get() = TODO("not implemented")
    override val beginLine: Int
        get() = TODO("not implemented")
    override val beginColumn: Int
        get() = TODO("not implemented")
    override val annotations: List<AnnotationAdapter>
        get() = TODO("not implemented")
    override val accessModifiers: List<ModifierAdapter>?
        get() = TODO("not implemented")
    override val parameters: List<ParameterContext>?
        get() = TODO("not implemented")
    override val returnType: String?
        get() = TODO("not implemented")
    override val type: String?
        get() = TODO("not implemented")
    private val stringRepresentation:String
        get() = TODO("not implemented")

    constructor(paramenter: Parameter) {
        TODO("not implemented")
    }

    constructor(node:Node) {
        TODO("not implemented")
    }

    override fun getInsertionPoint(): InsertionPoint = InsertionPoint.PARAMETER

    override fun toString(): String = this.stringRepresentation
}
