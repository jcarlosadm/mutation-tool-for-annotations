package mutation.tool.context

import com.github.javaparser.Range
import com.github.javaparser.ast.Modifier
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.expr.AnnotationExpr
import mutation.tool.context.entity.Entity

/**
 * Location where annotations can be inserted.
 *
 * @property entity object to access context properties.
 * @constructor creates a context instance
 */
abstract class Context(private val entity: Entity) {
    /**
     * @return insertion point (type) of this context.
     */
    abstract fun getInsertionPoint(): InsertionPoint

    /**
     * @return list of annotations of this context.
     */
    fun getAnnotations():List<AnnotationExpr> = entity.getAnnotations()

    /**
     * @return name of this context.
     */
    fun getName():String = entity.getName()

    /**
     * @return string representation of this context.
     */
    override fun toString(): String = entity.toString()

    /**
     * @return get range location of this context, on the original file.
     */
    fun getRange():Range = entity.getRange()

    /**
     * @return get list of modifiers of this context. Example: public, private, static...
     */
    fun getModifiers(): List<Modifier>? = entity.getAccessModifiers()

    /**
     * @return get the return type of this context, or null if this context isn't a MethodDeclaration
     */
    fun getReturnType(): String? = entity.getReturnType()

    /**
     * @return get list of parameters of this context, or null if this context isn't a MethodDeclaration
     */
    fun getParams(): List<Parameter>? = entity.getParams()

    /**
     * @return get the return type of this context, or null if this context isn't a Parameter or a FieldDeclaration
     */
    fun getType(): String? = entity.getType()
}