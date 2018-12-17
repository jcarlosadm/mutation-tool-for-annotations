package mutation.tool.operator

import com.github.javaparser.ast.expr.MarkerAnnotationExpr
import com.github.javaparser.ast.expr.NormalAnnotationExpr
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr
import mutation.tool.annotation.context.Context
import mutation.tool.mutant.Mutant
import mutation.tool.util.getAnnotations
import java.io.File

/**
 * Removes an attribute from a code annotation
 */
class RMAT(context: Context, file: File) : Operator(context, file) {

    override fun checkContext(): Boolean {
        val annotations = getAnnotations(context)
        for (annotation in annotations) {
            if (annotation.toString().contains(Regex("\\((.*?)\\)"))) {
                return true
            }
        }

        return false
    }

    override fun mutate(): List<Mutant> {
        TODO("not implemented")
    }
}