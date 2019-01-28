package mutation.tool.annotation

import com.github.javaparser.JavaParser
import mutation.tool.context.Context
import mutation.tool.context.ContextCatcherVisitor
import java.io.File

fun getListOfAnnotationContext(javaFile: File):List<Context> {
    val compilationUnit = JavaParser.parse(javaFile)
    val visitor = ContextCatcherVisitor()
    visitor.visit(compilationUnit, null)

    return visitor.contexts
}
