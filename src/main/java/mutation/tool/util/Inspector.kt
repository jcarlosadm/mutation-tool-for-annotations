package mutation.tool.util

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.expr.AnnotationExpr
import mutation.tool.context.*
import org.w3c.dom.Node

/**
 * Check if one context is equals to class declaration
 * 
 * @param context context to compare
 * @param classDeclr declaration to compare
 * @return true if are equals
 */
fun isSameClass(context: Context, classDeclr:ClassOrInterfaceDeclaration):Boolean = (context.getInsertionPoint() ==
        InsertionPoint.CLASS && classDeclr.nameAsString == context.name)

fun isSameClass(context: Context, classDeclr:Node):Boolean = TODO("not implemented")

/**
 * Check if one context is equals to method declaration
 * 
 * @param context context to compare
 * @param methodDeclr declaration to compare
 * @return true if are equals
 */
fun isSameMethod(context: Context, methodDeclr:MethodDeclaration): Boolean = (context.getInsertionPoint() ==
        InsertionPoint.METHOD && methodDeclr.toString() == context.toString())

fun isSameMethod(context: Context, methodDeclr:Node): Boolean = TODO("not implemented")

/**
 * Check if one context is equals to Field declaration
 * 
 * @param context context to compare
 * @param field declaration to compare
 * @return true if are equals
 */
fun isSameProp(context: Context, field:FieldDeclaration):Boolean = (context.getInsertionPoint() ==
        InsertionPoint.PROPERTY && field.toString() == context.toString())

fun isSameProp(context: Context, field:Node):Boolean = TODO("not implemented")

/**
 * Check if one context is equals to Parameter
 * 
 * @param context context to compare
 * @param parameter parameter to compare
 * @return true if are equals
 */
fun isSameParameter(context: Context, parameter:Parameter): Boolean =
        (context.getInsertionPoint() == InsertionPoint.PARAMETER && parameter.toString() == context.toString() &&
                context.beginLine == parameter.range.get().begin.line &&
                context.beginColumn == parameter.range.get().begin.column)

fun isSameParameter(context: Context, parameter:Node): Boolean = TODO("not implemented")

/**
 * count number of attributes of an annotation
 * 
 * @param annotation annotation expression
 * @return number of attributes
 */
fun numOfAnnotationAttributes(annotation: AnnotationExpr): Int {
    if (!annotation.toString().contains(Regex("\\((.*?)\\)")))
        return 0
    return annotation.toString().replaceBefore("(", "").split(",").size
}

fun numOfAnnotationAttributes(annotation: Node): Int {
    TODO("not implemented")
}
