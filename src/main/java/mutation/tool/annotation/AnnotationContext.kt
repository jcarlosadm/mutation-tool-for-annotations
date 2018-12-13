package mutation.tool.annotation

import com.github.javaparser.JavaParser
import com.github.javaparser.Range
import com.github.javaparser.ast.expr.AnnotationExpr
import mutation.tool.util.ClassVisitor
import mutation.tool.util.InsertionPoint
import java.io.File
import java.util.*

fun getListOfAnnotationContext(javaFile: File):List<AnnotationContext> {
    val compilationUnit = JavaParser.parse(javaFile)
    val visitor = ClassVisitor()
    visitor.visit(compilationUnit, null)

    return visitor.contexts
}

class AnnotationContext(val annotation: AnnotationExpr?, val insertionPoint:InsertionPoint) {
    var range: Optional<Range>? = null

    fun setRange(range:Optional<Range>):AnnotationContext {
        this.range = range
        return this
    }
}
