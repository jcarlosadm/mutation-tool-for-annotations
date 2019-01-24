package mutation.tool.operator

import mutation.tool.context.Context
import mutation.tool.mutant.Mutant
import java.io.File

/**
 * Add Annotation
 */
class ADA(context: Context, file:File): Operator(context, file) {

    var annotation:String? = null

    override fun checkContext(): Boolean = true

    override fun mutate(): List<Mutant> {
        TODO("not implemented")
    }
}