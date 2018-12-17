package mutation.tool.util

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.expr.AnnotationExpr
import mutation.tool.annotation.context.*

fun getAnnotations(context: Context):List<AnnotationExpr> = when(context.getInsertionPoint()) {
    InsertionPoint.PROPERTY -> (context as PropertyContext).entity.annotations
    InsertionPoint.METHOD -> (context as MethodContext).entity.annotations
    InsertionPoint.CLASS -> (context as ClassContext).entity.annotations
}

// TODO create test
fun isSameClass(context: Context, classDeclr:ClassOrInterfaceDeclaration):Boolean = (context.getInsertionPoint() ==
        InsertionPoint.CLASS && classDeclr.nameAsString == (context as ClassContext).entity.nameAsString)
// TODO create test
fun isSameMethod(context: Context, methodDeclr:MethodDeclaration): Boolean = (context.getInsertionPoint() ==
        InsertionPoint.METHOD && methodDeclr.toString() == (context as MethodContext).entity.toString())
// TODO create test
fun isSameProp(context: Context, field:FieldDeclaration):Boolean = (context.getInsertionPoint() ==
        InsertionPoint.PROPERTY && field.toString() == (context as PropertyContext).entity.toString())