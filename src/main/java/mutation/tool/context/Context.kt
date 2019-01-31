package mutation.tool.context

import com.github.javaparser.Range
import com.github.javaparser.ast.expr.AnnotationExpr
import mutation.tool.context.entity.Entity

abstract class Context(private val entity: Entity) {
    abstract fun getInsertionPoint(): InsertionPoint
    fun getAnnotations():List<AnnotationExpr> = entity.getAnnotations()
    fun getName():String = entity.getName()
    override fun toString(): String = entity.toString()
    fun getRange():Range = entity.getRange()
}