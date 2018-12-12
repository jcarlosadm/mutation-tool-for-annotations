package mutation.tool.operator

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import mutation.tool.util.InsertionPoint
import com.github.javaparser.ast.expr.AnnotationExpr
import mutation.tool.annotation.AnnotationContext

fun getValidOperators(context:AnnotationContext):List<Operator> {
    TODO("not implemented")
}

interface Operator {
    fun mutate(context:AnnotationContext)
}