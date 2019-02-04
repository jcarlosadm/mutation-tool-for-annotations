package mutation.tool.context.entity

import com.github.javaparser.Range
import com.github.javaparser.ast.Modifier
import com.github.javaparser.ast.NodeList
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.expr.AnnotationExpr
import java.lang.Exception
import java.util.*

class Entity {
    var classOrInterfaceDeclaration: ClassOrInterfaceDeclaration? = null
        private set
    var methodDeclaration: MethodDeclaration? = null
        private set
    var fieldDeclaration: FieldDeclaration? = null
        private set
    var parameter: Parameter? = null
        private set

    constructor(classOrInterfaceDeclaration: ClassOrInterfaceDeclaration) {
        this.classOrInterfaceDeclaration = classOrInterfaceDeclaration
    }

    constructor(methodDeclaration: MethodDeclaration) {
        this.methodDeclaration = methodDeclaration
    }

    constructor(fieldDeclaration: FieldDeclaration) {
        this.fieldDeclaration = fieldDeclaration
    }

    constructor(parameter: Parameter) {
        this.parameter = parameter
    }

    fun getName():String {
        if (classOrInterfaceDeclaration != null) return classOrInterfaceDeclaration?.nameAsString!!
        else if (methodDeclaration != null) return methodDeclaration?.nameAsString!!
        else if (fieldDeclaration != null) return fieldDeclaration.toString()
        else if (parameter != null) return parameter?.nameAsString!!
        throw Exception()
    }

    override fun toString():String {
        if (classOrInterfaceDeclaration != null) return classOrInterfaceDeclaration?.toString()!!
        else if (methodDeclaration != null) return methodDeclaration?.toString()!!
        else if (fieldDeclaration != null) return fieldDeclaration.toString()
        else if (parameter != null) return parameter?.toString()!!
        throw Exception()
    }

    fun getRange():Range {
        if (classOrInterfaceDeclaration != null) return classOrInterfaceDeclaration?.range!!.get()
        else if (methodDeclaration != null) return methodDeclaration?.range?.get()!!
        else if (fieldDeclaration != null) return fieldDeclaration?.range?.get()!!
        else if (parameter != null) return parameter?.range?.get()!!
        throw Exception()
    }

    fun getAnnotations():NodeList<AnnotationExpr> {
        if (classOrInterfaceDeclaration != null) return classOrInterfaceDeclaration?.annotations!!
        else if (methodDeclaration != null) return methodDeclaration?.annotations!!
        else if (fieldDeclaration != null) return fieldDeclaration?.annotations!!
        else if (parameter != null) return parameter?.annotations!!
        throw Exception()
    }

    fun getAccessModifiers(): List<Modifier>? {
        if (classOrInterfaceDeclaration != null) return classOrInterfaceDeclaration?.modifiers?.toList()
        else if (methodDeclaration != null) return methodDeclaration?.modifiers?.toList()
        else if (fieldDeclaration != null) return fieldDeclaration?.modifiers?.toList()
        else if (parameter != null) return parameter?.modifiers?.toList()
        throw Exception()
    }

    fun getParams(): List<Parameter>? {
        if (methodDeclaration != null) {
            return methodDeclaration?.parameters
        }

        return null
    }

    fun getReturnType(): String? {
        if (methodDeclaration != null) {
            return methodDeclaration?.typeAsString
        }

        return null
    }

    fun getType(): String? {
        if (fieldDeclaration != null) return fieldDeclaration?.elementType.toString()
        else if (parameter != null) return parameter?.typeAsString

        return null
    }
}