package mutation.tool.annotation.visitor

import com.github.javaparser.JavaParser
import mutation.tool.context.ContextCatcherVisitor
import mutation.tool.context.Context
import java.io.File

class JavaStrategy:VisitorStrategy {
    override fun getContexts(file: File): List<Context> {
        val compilationUnit = JavaParser.parse(file)
        val visitor = ContextCatcherVisitor()
        visitor.visit(compilationUnit, null)

        return visitor.contexts
    }
}