package mutation.tool.annotation.visitor

import mutation.tool.context.Context
import mutation.tool.context.visitor.ContextCatcherCSharpVisitor
import mutation.tool.util.Language
import mutation.tool.util.xml.fileToDocument
import java.io.File

class CSharpStrategy:VisitorStrategy {
    override fun getContexts(file: File): List<Context> {
        val rootNode = fileToDocument(file, Language.C_SHARP)
        val visitor = ContextCatcherCSharpVisitor()
        visitor.visit(rootNode)

        return visitor.contexts
    }
}