package mutation.tool.operator.adat

import com.github.javaparser.ast.expr.MarkerAnnotationExpr
import com.github.javaparser.ast.expr.NormalAnnotationExpr
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr
import mutation.tool.context.Context
import mutation.tool.mutant.Mutant
import mutation.tool.operator.Operator
import mutation.tool.util.getAnnotations
import java.io.File

/**
 * Add valid attribute to annotation
 */
class ADAT(context: Context, file:File) : Operator(context, file) {

    lateinit var map:Map<String, List<Map<String, String>>>

    override fun checkContext(): Boolean {
        TODO("not implemented")
    }

    override fun mutate(): List<Mutant> {
        TODO("not implemented")
    }
}