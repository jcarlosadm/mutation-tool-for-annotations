package mutation.tool.operator

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import mutation.tool.util.InsertionPoint
import com.github.javaparser.ast.expr.AnnotationExpr
import mutation.tool.annotation.AnnotationContext

interface Operator {
	fun mutate(annotation:AnnotationExpr?, context:AnnotationContext)
}