package mutation.tool.annotation

import com.github.javaparser.JavaParser
import mutation.tool.annotation.visitor.VisitorStrategy
import mutation.tool.context.Context
import mutation.tool.context.ContextCatcherVisitor
import mutation.tool.util.Language
import mutation.tool.util.MutationToolConfig
import java.io.File

/**
 * Get list of contexts of a java file. These contexts are possible locations for annotations.
 *
 * @param javaFile Source file.
 * @return A list of contexts.
 */
fun getListOfAnnotationContext(validFile: File, visitorStrategy: VisitorStrategy):List<Context> {
    return visitorStrategy.getContexts(validFile)
}
