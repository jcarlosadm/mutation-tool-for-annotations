package mutation.tool.context

import com.github.javaparser.Range
import com.github.javaparser.ast.Modifier
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.expr.AnnotationExpr
import mutation.tool.context.entity.Entity
import java.util.*

abstract class Context(private val entity: Entity) {
    abstract fun getInsertionPoint(): InsertionPoint
    fun getAnnotations():List<AnnotationExpr> = entity.getAnnotations()
    fun getName():String = entity.getName()
    override fun toString(): String = entity.toString()
    fun getRange():Range = entity.getRange()
    fun getModifiers(): List<Modifier>? = entity.getAccessModifiers()
    fun getReturnType(): String? = entity.getReturnType()
    fun getParams(): List<Parameter>? = entity.getParams()
    fun getType(): String? = entity.getType()
}