package mutation.tool.context.entity

import com.github.javaparser.Range
import com.github.javaparser.ast.Modifier
import com.github.javaparser.ast.NodeList
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.expr.AnnotationExpr

/**
 * Object that store properties of context.
 *
 * This object has several methods to access properties of context, which depends on type of this context.
 */
class Entity {
    private var classOrInterfaceDeclaration: ClassOrInterfaceDeclaration? = null
    private var methodDeclaration: MethodDeclaration? = null
    private var fieldDeclaration: FieldDeclaration? = null
    private var parameter: Parameter? = null

    /**
     * @constructor create an entity with context of ClassOrInterfaceDeclaration type
     * @param classOrInterfaceDeclaration context instance
     */
    constructor(classOrInterfaceDeclaration: ClassOrInterfaceDeclaration) {
        this.classOrInterfaceDeclaration = classOrInterfaceDeclaration
    }

    /**
     * @constructor create an entity with context of MethodDeclaration type
     * @param methodDeclaration context instance
     */
    constructor(methodDeclaration: MethodDeclaration) {
        this.methodDeclaration = methodDeclaration
    }

    /**
     * @constructor create an entity with context of FieldDeclaration type
     * @param fieldDeclaration context instance
     */
    constructor(fieldDeclaration: FieldDeclaration) {
        this.fieldDeclaration = fieldDeclaration
    }

    /**
     * @constructor create an entity with context of Parameter type
     * @param parameter context instance
     */
    constructor(parameter: Parameter) {
        this.parameter = parameter
    }

    /**
     * @return get name of context
     */
    fun getName():String = when {
        classOrInterfaceDeclaration != null -> classOrInterfaceDeclaration?.nameAsString!!
        methodDeclaration != null -> methodDeclaration?.nameAsString!!
        fieldDeclaration != null -> fieldDeclaration.toString()
        parameter != null -> parameter?.nameAsString!!
        else -> throw Exception()
    }

    /**
     * @return get string representation of context
     */
    override fun toString():String = when {
        classOrInterfaceDeclaration != null -> classOrInterfaceDeclaration?.toString()!!
        methodDeclaration != null -> methodDeclaration?.toString()!!
        fieldDeclaration != null -> fieldDeclaration.toString()
        parameter != null -> parameter?.toString()!!
        else -> throw Exception()
    }

    /**
     * @return get range location of context, on the original file.
     */
    fun getRange():Range = when {
        classOrInterfaceDeclaration != null -> classOrInterfaceDeclaration?.range!!.get()
        methodDeclaration != null -> methodDeclaration?.range?.get()!!
        fieldDeclaration != null -> fieldDeclaration?.range?.get()!!
        parameter != null -> parameter?.range?.get()!!
        else -> throw Exception()
    }

    /**
     * @return get a list of annotations of this context
     */
    fun getAnnotations():NodeList<AnnotationExpr> = when {
        classOrInterfaceDeclaration != null -> classOrInterfaceDeclaration?.annotations!!
        methodDeclaration != null -> methodDeclaration?.annotations!!
        fieldDeclaration != null -> fieldDeclaration?.annotations!!
        parameter != null -> parameter?.annotations!!
        else -> throw Exception()
    }

    /**
     * @return get a list of modifiers of context. Examples: public, private, static, ...
     */
    fun getAccessModifiers(): List<Modifier>? = when {
        classOrInterfaceDeclaration != null -> classOrInterfaceDeclaration?.modifiers?.toList()
        methodDeclaration != null -> methodDeclaration?.modifiers?.toList()
        fieldDeclaration != null -> fieldDeclaration?.modifiers?.toList()
        parameter != null -> parameter?.modifiers?.toList()
        else -> throw Exception()
    }

    /**
     * @return get list of parameters of context, or null if context isn't a MethodDeclaration.
     */
    fun getParams(): List<Parameter>? {
        if (methodDeclaration != null) {
            return methodDeclaration?.parameters
        }

        return null
    }

    /**
     * @return get the string representation of return type of context, or null if the context isn't a MethodDeclaration.
     */
    fun getReturnType(): String? {
        if (methodDeclaration != null) {
            return methodDeclaration?.typeAsString
        }

        return null
    }

    /**
     * @return get the string representation of type of context, or null if the context isn't a FieldDeclaration or a Parameter.
     */
    fun getType(): String? {
        if (fieldDeclaration != null) return fieldDeclaration?.elementType.toString()
        else if (parameter != null) return parameter?.typeAsString

        return null
    }
}