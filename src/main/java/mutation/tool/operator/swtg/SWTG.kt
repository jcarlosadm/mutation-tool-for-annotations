package mutation.tool.operator.swtg

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.NodeList
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.expr.AnnotationExpr
import mutation.tool.context.Context
import mutation.tool.context.InsertionPoint
import mutation.tool.mutant.Mutant
import mutation.tool.mutant.MutateVisitor
import mutation.tool.operator.Operator
import mutation.tool.operator.OperatorsEnum
import mutation.tool.util.*
import java.io.File

/**
 * Switch the annotation to another valid target
 */
class SWTG(context: Context, file:File, val allContexts: List<Context>): Operator(context, file) {

    lateinit var mapContextType:Map<String, List<InsertionPoint>>

    private lateinit var currentMutant: Mutant
    private lateinit var currentAnnotation: AnnotationExpr
    private lateinit var currentOtherContext: Context
    private var lockedInsert = false

    override fun checkContext(): Boolean {
        for (annotation in getAnnotations(context)) {
            if (mapContextType.containsKey(annotation.nameAsString) && mapContextType[annotation.nameAsString] != null) {
                for (insertionPoint in mapContextType[annotation.nameAsString]!!) {
                    if (context.getInsertionPoint() != insertionPoint)
                        return true
                }
            }
        }

        return false
    }

    override fun mutate(): List<Mutant> {
        val mutants = mutableListOf<Mutant>()
        val mutateVisitor = MutateVisitor(this)
        val compUnit = JavaParser.parse(file)

        for (annotation in getAnnotations(context)) {
            if (!mapContextType.containsKey(annotation.nameAsString) || mapContextType[annotation.nameAsString] == null)
                continue

            for (insertionPoint in mapContextType[annotation.nameAsString]!!) {
                if (context.getInsertionPoint() == insertionPoint) continue

                for (otherContext in allContexts) {
                    if (otherContext.getInsertionPoint() != insertionPoint) continue

                    locked = false
                    lockedInsert = false
                    currentMutant = Mutant(OperatorsEnum.SWTG)
                    currentAnnotation = annotation
                    currentOtherContext = otherContext

                    val newCompUnit = compUnit.clone()
                    mutateVisitor.visit(newCompUnit, null)
                    currentMutant.compilationUnit = newCompUnit
                    mutants += currentMutant
                }
            }
        }

        return mutants
    }

    override fun visit(n: ClassOrInterfaceDeclaration?, arg: Any?): Boolean {
        if (super.visit(n, arg))
            return removeAnnotation(n!!.annotations)
        return !lockedInsert && n != null && isSameClass(currentOtherContext, n) && addAnnotation(n.annotations)
    }

    override fun visit(n: MethodDeclaration?, arg: Any?): Boolean {
        if (super.visit(n, arg))
            return removeAnnotation(n!!.annotations)
        return !lockedInsert && n != null && isSameMethod(currentOtherContext, n) && addAnnotation(n.annotations)
    }

    override fun visit(n: FieldDeclaration?, arg: Any?): Boolean {
        if (super.visit(n, arg))
            return removeAnnotation(n!!.annotations)
        return !lockedInsert && n != null && isSameProp(currentOtherContext, n) && addAnnotation(n.annotations)
    }

    override fun visit(n: Parameter?, arg: Any?): Boolean {
        if (super.visit(n, arg))
            return removeAnnotation(n!!.annotations)
        return !lockedInsert && n != null && isSameParameter(currentOtherContext, n) && addAnnotation(n.annotations)
    }

    private fun removeAnnotation(annotations: NodeList<AnnotationExpr>): Boolean {
        for (annotation in annotations) {
            if (annotation.toString() == currentAnnotation.toString()) {
                annotation.remove()
                locked = true
                return true
            }
        }

        return false
    }

    private fun addAnnotation(annotations: NodeList<AnnotationExpr>): Boolean {
        annotations.add(currentAnnotation)
        lockedInsert = true
        return true
    }
}