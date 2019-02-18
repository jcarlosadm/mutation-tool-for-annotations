package mutation.tool.annotation

import com.github.javaparser.JavaParser
import mutation.tool.context.Context
import mutation.tool.context.ContextCatcherVisitor
import java.io.File

/**
 * Get list of contexts of a java file. These contexts are possible locations for annotations.
 */
fun getListOfAnnotationContext(javaFile: File):List<Context> {
    val compilationUnit = JavaParser.parse(javaFile)
    val visitor = ContextCatcherVisitor()
    visitor.visit(compilationUnit, null)

    return visitor.contexts
}
