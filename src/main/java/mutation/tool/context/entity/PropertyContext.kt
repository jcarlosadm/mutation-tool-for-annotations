package mutation.tool.context.entity

import com.github.javaparser.ast.body.FieldDeclaration
import mutation.tool.context.InsertionPoint
import mutation.tool.context.adapter.AnnotationAdapter
import mutation.tool.context.adapter.ModifierAdapter
import org.w3c.dom.Node

// TODO move to context package
class PropertyContext:Context {

    constructor(fieldDeclaration: FieldDeclaration) {
        TODO("not implemented")
    }

    constructor(node: Node) {
        TODO("not implemented")
    }

    override fun getName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBeginLine(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBeginColumn(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAnnotations(): List<AnnotationAdapter> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAccessModifiers(): List<ModifierAdapter>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getParameters(): List<ParameterContext>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getReturnType(): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getType(): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInsertionPoint(): InsertionPoint = InsertionPoint.PROPERTY
}