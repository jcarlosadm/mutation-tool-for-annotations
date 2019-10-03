package mutation.tool.operator.rmat

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.expr.*
import mutation.tool.context.Context
import mutation.tool.mutant.JavaMutant
import mutation.tool.operator.JavaOperator
import mutation.tool.operator.OperatorsEnum
import mutation.tool.util.numOfAnnotationAttributes
import java.io.File

/**
 * Removes an attribute from a code annotation
 * 
 * @param context context of this operator
 * @param file source file
 * @constructor create a RMAT operator
 */
class RMAT(context: Context, file: File) : JavaOperator(context, file) {
    private var currentJavaMutant:JavaMutant? = null
    private var currentAnnotation:AnnotationExpr? = null
    private var currentIndex:Int? = null

    override fun checkContext(): Boolean {
        for (annotation in context.getAnnotations()) {
            if (annotation.toString().contains(Regex("\\((.*?)\\)"))) {
                return true
            }
        }

        return false
    }

    override fun mutate(): List<JavaMutant> {
        val mutants = mutableListOf<JavaMutant>()

        for (annotation in context.getAnnotations()) {
            val nAttr = numOfAnnotationAttributes(annotation)
            for (index in 0..(nAttr-1)) {
                currentAnnotation = annotation
                currentIndex = index
                currentJavaMutant = JavaMutant(OperatorsEnum.RMAT)
                currentJavaMutant!!.compilationUnit = this.visit()
                mutants.add(currentJavaMutant!!)
            }
        }

        return mutants
    }

    override fun visit(n: ClassOrInterfaceDeclaration?, arg: Any?): Boolean = super.visit(n, arg) &&
            removeAttribute(n!!.annotations)

    override fun visit(n: MethodDeclaration?, arg: Any?) : Boolean = super.visit(n, arg) &&
            removeAttribute(n!!.annotations)

    override fun visit(n: FieldDeclaration?, arg: Any?) : Boolean = super.visit(n, arg) &&
            removeAttribute(n!!.annotations)

    override fun visit(n: Parameter?, arg: Any?) : Boolean = super.visit(n, arg) &&
            removeAttribute(n!!.annotations)

    private fun removeAttribute(annotations:List<AnnotationExpr>):Boolean {
        for (annotation in annotations) {
            if (annotation.toString() == currentAnnotation.toString()) {
                when (annotation) {
                    is SingleMemberAnnotationExpr -> {
                        annotation.replace(MarkerAnnotationExpr(Name(annotation.nameAsString + "()")))
                    }
                    is NormalAnnotationExpr -> {
                        annotation.pairs.removeAt(currentIndex!!)
                    }
                }

                return true
            }
        }

        return false
    }
}