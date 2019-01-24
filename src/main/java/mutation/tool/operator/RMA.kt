package mutation.tool.operator

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.expr.AnnotationExpr
import mu.KotlinLogging
import mutation.tool.context.*
import mutation.tool.mutant.Mutant
import mutation.tool.mutant.MutateVisitor
import mutation.tool.util.*
import java.io.File

private val logger = KotlinLogging.logger{}

/**
 * Remove Annotation Operator
 */
class RMA(context: Context, file: File) : Operator(context, file) {

    private var currentMutant:Mutant? = null
    private var currentAnnotation:AnnotationExpr? = null
    private var locked:Boolean = false

    override fun checkContext(): Boolean = (getAnnotations(context)).isNotEmpty()

    override fun mutate(): List<Mutant> {
        val mutants = mutableListOf<Mutant>()

        val mutateVisitor = MutateVisitor(this)
        val compUnit = JavaParser.parse(file)

        for (annotation in getAnnotations(context)) {
            val newCompUnit = compUnit.clone()
            currentAnnotation = annotation
            locked = false
            currentMutant = Mutant(OperatorsEnum.RMA)
            mutateVisitor.visit(newCompUnit, null)
            logger.debug { "$newCompUnit" }
            currentMutant?.compilationUnit = newCompUnit
            mutants.add(currentMutant!!)
        }

        return mutants
    }

    override fun visit(n: ClassOrInterfaceDeclaration?, arg: Any?) {
        super.visit(n, arg)
        if (locked || n == null || !isSameClass(context, n)) return
        removeCurrentAnnotation(n.annotations)
    }

    override fun visit(n: FieldDeclaration?, arg: Any?) {
        super.visit(n, arg)
        if (locked || n == null || !isSameProp(context, n)) return
        removeCurrentAnnotation(n.annotations)
    }

    override fun visit(n: MethodDeclaration?, arg: Any?) {
        super.visit(n, arg)
        if (locked || n == null || !isSameMethod(context, n)) return
        removeCurrentAnnotation(n.annotations)
    }

    override fun visit(n: Parameter?, arg: Any?) {
        super.visit(n, arg)
        if (locked || n == null || !isSameParameter(context, n)) return
        logger.debug { "$n" }
        removeCurrentAnnotation(n.annotations)
    }

    private fun removeCurrentAnnotation(annotations:List<AnnotationExpr>){
        if (currentAnnotation == null)
            return

        for (annotation in annotations) {
            if (annotation.toString() == currentAnnotation?.toString()) {
                currentMutant?.before = annotation.toString()
                annotation.remove()
                locked = true
                return
            }
        }
    }
}
