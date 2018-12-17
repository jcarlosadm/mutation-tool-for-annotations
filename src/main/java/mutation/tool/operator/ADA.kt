package mutation.tool.operator

import mutation.tool.annotation.context.Context
import mutation.tool.mutant.Mutant
import java.io.File

/**
 * Add Annotation
 */
class ADA(context: Context, file:File): Operator(context, file) {

    override fun checkContext(): Boolean {
        TODO("not implemented")
    }

    override fun mutate(): List<Mutant> {
        TODO("not implemented")
    }
}