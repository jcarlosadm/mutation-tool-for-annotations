package mutation.tool.operator

import mutation.tool.context.Context
import mutation.tool.mutant.Mutant
import java.io.File

/**
 * Replace an annotation for another
 */
class RPA(context: Context, file: File) : Operator(context, file) {

    override fun checkContext(): Boolean {
        TODO("not implemented")
    }

    override fun mutate(): List<Mutant> {
        TODO("not implemented")
    }
}