package mutation.tool.operator.ada

import mutation.tool.context.Context
import mutation.tool.mutant.CSharpMutant
import mutation.tool.mutant.CSharpMutateVisitor
import mutation.tool.operator.CSharpOperator
import java.io.File

class CSharpADA(context: Context, file: File) : CSharpOperator(context, file) {
    override val mutateVisitor = CSharpMutateVisitor(this)

    var annotation:String? = null

    override fun checkContext(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun mutate(): List<CSharpMutant> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}