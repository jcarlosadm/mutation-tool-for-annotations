package mutation.tool.context.entity

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import org.w3c.dom.Node

class ClassEntity:Entity2 {

    private lateinit var name:String
    private var beginLine = 0
    private var beginColumn = 0
    private lateinit var annotations:List<AnnotationAdapter>

    constructor(classOrInterfaceDeclaration: ClassOrInterfaceDeclaration) {
        this.name = classOrInterfaceDeclaration.nameAsString
        this.beginLine = classOrInterfaceDeclaration.range.get().begin.line
        this.beginColumn = classOrInterfaceDeclaration.range.get().begin.column
        //this.annotations = classOrInterfaceDeclaration.annotations
    }

    constructor(node: Node) {
        TODO("implement")
    }

    override fun getName(): String = this.name
    override fun getBeginLine(): Int = this.beginLine
    override fun getBeginColumn(): Int = this.beginColumn
    override fun getAnnotations(): List<AnnotationAdapter> = annotations

    override fun getAccessModifiers(): List<ModifierAdapter>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getParameters(): List<ParameterEntity>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getReturnType(): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getType(): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun toString(): String {
        return super.toString()
    }
}