package mutation.tool.operator.rmat

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.expr.*
import mu.KotlinLogging
import mutation.tool.context.Context
import mutation.tool.mutant.Mutant
import mutation.tool.mutant.MutateVisitor
import mutation.tool.operator.Operator
import mutation.tool.operator.OperatorsEnum
import mutation.tool.util.*
import java.io.File

private val logger = KotlinLogging.logger{}

/**
 * Removes an attribute from a code annotation
 */
class RMAT(context: Context, file: File) : Operator(context, file) {

    private var currentMutant:Mutant? = null
    private var currentAnnotation:AnnotationExpr? = null
    private var currentIndex:Int? = null

    override fun checkContext(): Boolean {
        for (annotation in getAnnotations(context)) {
            if (annotation.toString().contains(Regex("\\((.*?)\\)"))) {
                return true
            }
        }

        return false
    }

    override fun mutate(): List<Mutant> {
        val mutants = mutableListOf<Mutant>()

        val compilationUnit = JavaParser.parse(file)
        val visitor = MutateVisitor(this)

        for (annotation in getAnnotations(context)) {
            val nAttr = numOfAnnotationAttributes(annotation)
            for (index in 0..(nAttr-1)) {
                val newCompUnit = compilationUnit.clone()
                currentAnnotation = annotation
                currentIndex = index
                locked = false
                currentMutant = Mutant(OperatorsEnum.RMAT)
                visitor.visit(newCompUnit, null)
                logger.debug { "$newCompUnit" }
                currentMutant!!.compilationUnit = newCompUnit
                mutants.add(currentMutant!!)
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
                currentMutant?.before = annotation.toString()
                when (annotation) {
                    is SingleMemberAnnotationExpr -> {
                        annotation.replace(MarkerAnnotationExpr(Name(annotation.nameAsString + "()")))
                        currentMutant?.after = "@${annotation.nameAsString}()"
                    }
                    is NormalAnnotationExpr -> {
                        annotation.pairs.removeAt(currentIndex!!)
                        currentMutant?.after = annotation.toString()
                    }
                }

                locked = true
                return true
            }
        }

        return false
    }
}