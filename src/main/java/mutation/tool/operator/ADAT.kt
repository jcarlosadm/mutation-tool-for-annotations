package mutation.tool.operator

import mutation.tool.context.Context
import mutation.tool.mutant.Mutant
import java.io.File

/**
 * Add valid attribute to annotation
 */
class ADAT(context: Context, file:File) : Operator(context, file) {

    override fun checkContext(): Boolean {
        TODO("not implemented")
    }

    override fun mutate(): List<Mutant> {
        TODO("not implemented")
    }
}