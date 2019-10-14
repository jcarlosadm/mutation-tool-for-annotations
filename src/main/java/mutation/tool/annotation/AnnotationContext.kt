package mutation.tool.annotation

import mutation.tool.annotation.visitor.VisitorStrategy
import mutation.tool.context.Context
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
