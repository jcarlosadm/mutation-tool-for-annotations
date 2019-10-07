package mutation.tool.operator.rpa

import mutation.tool.context.Context
import mutation.tool.mutant.CSharpMutant
import mutation.tool.mutant.CSharpMutateVisitor
import mutation.tool.operator.CSharpOperator
import java.io.File

class CSharpRPA(context: Context, file: File) : CSharpOperator(context, file) {
    override val mutateVisitor = CSharpMutateVisitor(this)

    lateinit var switchMap:Map<String, List<String>>

    override fun checkContext(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun mutate(): List<CSharpMutant> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}