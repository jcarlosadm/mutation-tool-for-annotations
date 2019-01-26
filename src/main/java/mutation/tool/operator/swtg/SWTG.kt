package mutation.tool.operator.swtg

import mutation.tool.context.Context
import mutation.tool.mutant.Mutant
import mutation.tool.operator.Operator
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