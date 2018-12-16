package mutation.tool.annotation

import com.github.javaparser.JavaParser
import mutation.tool.annotation.context.Context
import mutation.tool.util.ClassVisitor
import java.io.File

fun getListOfAnnotationContext(javaFile: File):List<Context> {
    val compilationUnit = JavaParser.parse(javaFile)
    val visitor = ClassVisitor()
    visitor.visit(compilationUnit, null)

    return visitor.contexts
}
