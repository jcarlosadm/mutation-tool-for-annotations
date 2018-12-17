package mutation.tool.operator

import mutation.tool.annotation.context.Context
import mutation.tool.mutant.Mutant
import java.io.File

/**
 * Switch the annotation to another valid target
 */
class SWTG(context: Context, file:File, val anotherContexts: List<Context>): Operator(context, file) {

    override fun checkContext(): Boolean {
        TODO("not implemented")
    }

    override fun mutate(): List<Mutant> {
        TODO("not implemented")
    }
}