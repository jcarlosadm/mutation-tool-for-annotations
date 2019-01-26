package mutation.tool.operator

import com.github.javaparser.JavaParser
import mutation.tool.context.Context
import mutation.tool.mutant.Mutant
import mutation.tool.mutant.MutateVisitor
import mutation.tool.util.getAnnotations
import java.io.File

/**
 * Switch the annotation to another valid target
 */
class SWTG(context: Context, file:File, val allContexts: List<Context>): Operator(context, file) {

    override fun checkContext(): Boolean {
        TODO("not implemented")
    }

    override fun mutate(): List<Mutant> {
        TODO("not implemented")
    }
}