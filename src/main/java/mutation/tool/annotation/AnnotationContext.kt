package mutation.tool.annotation

import com.github.javaparser.ast.expr.AnnotationExpr
import mutation.tool.util.InsertionPoint
import java.io.File

fun getListOfAnnotationContext(javaFile: File):List<AnnotationContext> {
    TODO("not implemented")
}

class AnnotationContext(val annotation: AnnotationExpr?, val insertionPoint:InsertionPoint) {
}
