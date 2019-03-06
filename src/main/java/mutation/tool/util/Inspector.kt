package mutation.tool.util

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.expr.AnnotationExpr
import mutation.tool.context.*


fun isSameClass(context: Context, classDeclr:ClassOrInterfaceDeclaration):Boolean = (context.getInsertionPoint() ==
        InsertionPoint.CLASS && classDeclr.nameAsString == context.getName())

fun isSameMethod(context: Context, methodDeclr:MethodDeclaration): Boolean = (context.getInsertionPoint() ==
        InsertionPoint.METHOD && methodDeclr.toString() == context.toString())

fun isSameProp(context: Context, field:FieldDeclaration):Boolean = (context.getInsertionPoint() ==
        InsertionPoint.PROPERTY && field.toString() == context.toString())

fun isSameParameter(context: Context, parameter:Parameter): Boolean =
        (context.getInsertionPoint() == InsertionPoint.PARAMETER && parameter.toString() == context.toString() &&
                context.getRange().begin.line == parameter.range.get().begin.line &&
                context.getRange().begin.column == parameter.range.get().begin.column)

fun numOfAnnotationAttributes(annotation: AnnotationExpr): Int {
    if (!annotation.toString().contains(Regex("\\((.*?)\\)")))
        return 0
    return annotation.toString().replaceBefore("(", "").split(",").size
}
