package mutation.tool.annotation

import com.github.javaparser.JavaParser
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
fun getListOfAnnotationContext(validFile: File, config: MutationToolConfig):List<Context> {
    if (config.language == Language.JAVA)
        return javaStrategy(validFile)
    return cSharpStrategy(validFile)
}

private fun javaStrategy(javaFile: File):List<Context> {
    val compilationUnit = JavaParser.parse(javaFile)
    val visitor = ContextCatcherVisitor()
    visitor.visit(compilationUnit, null)

    return visitor.contexts
}

private fun cSharpStrategy(cSharpFile: File):List<Context> {
    TODO("not implemented")
}
