package mutation.tool.operator

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.expr.AnnotationExpr
import mu.KotlinLogging
import mutation.tool.annotation.context.*
import mutation.tool.mutant.Mutant
import mutation.tool.mutant.MutateVisitor
import mutation.tool.util.getAnnotations
import mutation.tool.util.isSameClass
import mutation.tool.util.isSameMethod
import mutation.tool.util.isSameProp
import java.io.File

private val logger = KotlinLogging.logger{}

/**
 * Remove Annotation Operator
 */
class RMA(context: Context, file: File) : Operator(context, file) {

    private var currentAnnotation:AnnotationExpr? = null
    private var lock:Boolean = false

    override fun checkContext(): Boolean = (getAnnotations(context)).isNotEmpty()

    override fun mutate(): List<Mutant> {
        val mutants = mutableListOf<Mutant>()

        val mutateVisitor = MutateVisitor(this)
        val compUnit = JavaParser.parse(file)

        for (annotation in getAnnotations(context)) {
            val newCompUnit = compUnit.clone()
            currentAnnotation = annotation
            lock = false
            mutateVisitor.visit(newCompUnit, null)
            logger.debug { "$newCompUnit" }
            mutants.add(Mutant(newCompUnit))
        }

        return mutants
    }

    override fun visit(n: ClassOrInterfaceDeclaration?, arg: Any?) {
        super.visit(n, arg)
        if (lock || n == null || !isSameClass(context, n)) return
        removeCurrentAnnotation(n.annotations!!)
    }

    override fun visit(n: FieldDeclaration?, arg: Any?) {
        super.visit(n, arg)
        if (lock || n == null || !isSameProp(context, n)) return
        removeCurrentAnnotation(n.annotations!!)
    }

    override fun visit(n: MethodDeclaration?, arg: Any?) {
        super.visit(n, arg)
        if (lock || n == null || !isSameMethod(context, n)) return
        removeCurrentAnnotation(n.annotations!!)
    }

    private fun removeCurrentAnnotation(annotations:List<AnnotationExpr>){
        if (currentAnnotation == null)
            return

        for (annotation in annotations) {
            if (annotation.toString() == currentAnnotation?.toString()) {
                annotation.remove()
                lock = true
                return
            }
        }
    }
}
