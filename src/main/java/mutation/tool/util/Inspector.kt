package mutation.tool.util

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.expr.AnnotationExpr
import mutation.tool.context.*

// TODO create test
fun isSameClass(context: Context, classDeclr:ClassOrInterfaceDeclaration):Boolean = (context.getInsertionPoint() ==
        InsertionPoint.CLASS && classDeclr.nameAsString == (context as ClassContext).entity.nameAsString)
// TODO create test
fun isSameMethod(context: Context, methodDeclr:MethodDeclaration): Boolean = (context.getInsertionPoint() ==
        InsertionPoint.METHOD && methodDeclr.toString() == (context as MethodContext).entity.toString())
// TODO create test
fun isSameProp(context: Context, field:FieldDeclaration):Boolean = (context.getInsertionPoint() ==
        InsertionPoint.PROPERTY && field.toString() == (context as PropertyContext).entity.toString())

// TODO create test
fun isSameParameter(context: Context, parameter:Parameter): Boolean = (context.getInsertionPoint() ==
        InsertionPoint.PARAMETER && parameter.toString() == (context as ParameterContext).entity.toString() &&
        parameter.range.equals(context.entity.range))

// TODO create test
fun numOfAnnotationAttributes(annotation: AnnotationExpr): Int {
    if (!annotation.toString().contains(Regex("\\((.*?)\\)")))
        return 0
    return annotation.toString().replaceBefore("(", "").split(",").size
}
