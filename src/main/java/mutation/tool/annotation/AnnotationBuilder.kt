package mutation.tool.annotation

import com.github.javaparser.ast.expr.*

class AnnotationBuilder(private val stringRepresentation:String) {
    fun build(): AnnotationExpr {
        if (stringRepresentation.contains(Regex("\\((.*?)\\)"))) {
            if (stringRepresentation.contains("="))
                return getNormalAnnotationExpr()

            return getSingleMemberAnnotationExpr()
        }

        return MarkerAnnotationExpr(stringRepresentation.removePrefix("@"))
    }

    private fun getNormalAnnotationExpr(): NormalAnnotationExpr {
        val expr = NormalAnnotationExpr()
        expr.setName(this.extractName().removePrefix("@"))
        for (pair in this.getPairs()) {
            val key = pair.split(Regex("="), 2)[0].trim()
            val value = pair.split(Regex("="), 2)[1].trim()
            expr.addPair(key, value)
        }

        return expr
    }

    private fun getSingleMemberAnnotationExpr(): SingleMemberAnnotationExpr {
        val expr = SingleMemberAnnotationExpr()
        expr.setName(this.extractName().removePrefix("@"))
        expr.memberValue = this.getSingleMemberValue()
        return expr
    }

    private fun getSingleMemberValue(): Expression? = NameExpr(this.getInsideParentesisValue())

    private fun getPairs(): List<String> = getInsideParentesisValue().split(",")

    private fun extractName(): String = stringRepresentation.substring(0, stringRepresentation.indexOf("("))

    private fun getInsideParentesisValue(): String = stringRepresentation.substring(stringRepresentation.
            indexOf("(") + 1, stringRepresentation.indexOf(")"))
}