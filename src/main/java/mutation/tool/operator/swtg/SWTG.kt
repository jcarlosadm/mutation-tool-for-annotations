package mutation.tool.operator.swtg

import com.github.javaparser.ast.NodeList
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.expr.AnnotationExpr
import mutation.tool.context.Context
import mutation.tool.context.InsertionPoint
import mutation.tool.mutant.Mutant
import mutation.tool.operator.Operator
import mutation.tool.operator.OperatorsEnum
import mutation.tool.util.*
import java.io.File

/**
 * Switch the annotation to another valid target
 *
 * @param context context of the operator
 * @param file source file
 * @constructor Create a SWTG operator instance
 */
class SWTG(context: Context, file:File, private val allContexts: List<Context>): Operator(context, file) {

    /**
     * map that will help the SWTG operator to build the mutants
     * 
     * Structure of map:
     * annotation -> list ( target )
     */
    lateinit var mapContextType:Map<String, List<InsertionPoint>>

    private lateinit var currentMutant: Mutant
    private lateinit var currentAnnotation: AnnotationExpr
    private lateinit var currentOtherContext: Context
    private var lockedInsert = false

    override fun checkContext(): Boolean {
        for (annotation in context.getAnnotations()) {
            var ok = false
            var validKey = ""
            mapContextType.keys.forEach { if (annotationFinder(annotation, it)) {ok = true; validKey = it} }

            if (ok && mapContextType[validKey] != null) {
                for (insertionPoint in mapContextType.getValue(validKey)) {
                    if (context.getInsertionPoint() != insertionPoint)
                        return true
                }
            }
        }

        return false
    }

    override fun mutate(): List<Mutant> {
        val mutants = mutableListOf<Mutant>()

        for (annotation in context.getAnnotations()) {
            var ok = false
            var validKey = ""
            mapContextType.keys.forEach { if (annotationFinder(annotation, it)) {ok = true; validKey = it} }

            if (!ok || mapContextType[validKey] == null) continue

            for (insertionPoint in mapContextType.getValue(validKey)) {
                if (context.getInsertionPoint() == insertionPoint) continue

                for (otherContext in allContexts) {
                    if (otherContext.getInsertionPoint() != insertionPoint) continue

                    lockedInsert = false
                    currentMutant = Mutant(OperatorsEnum.SWTG)
                    currentAnnotation = annotation
                    currentOtherContext = otherContext

                    currentMutant.compilationUnit = this.visit()
                    mutants += currentMutant
                }
            }
        }

        return mutants
    }

    override fun visit(n: ClassOrInterfaceDeclaration?, arg: Any?): Boolean {
        if (super.visit(n, arg))
            return removeAnnotation(n!!.annotations)
        else if (!lockedInsert && n != null && isSameClass(currentOtherContext, n)) addAnnotation(n.annotations)
        return false
    }

    override fun visit(n: MethodDeclaration?, arg: Any?): Boolean {
        if (super.visit(n, arg))
            return removeAnnotation(n!!.annotations)
        else if(!lockedInsert && n != null && isSameMethod(currentOtherContext, n)) addAnnotation(n.annotations)
        return false
    }

    override fun visit(n: FieldDeclaration?, arg: Any?): Boolean {
        if (super.visit(n, arg))
            return removeAnnotation(n!!.annotations)
        else if(!lockedInsert && n != null && isSameProp(currentOtherContext, n)) addAnnotation(n.annotations)
        return false
    }

    override fun visit(n: Parameter?, arg: Any?): Boolean {
        if (super.visit(n, arg))
            return removeAnnotation(n!!.annotations)
        else if(!lockedInsert && n != null && isSameParameter(currentOtherContext, n)) addAnnotation(n.annotations)
        return false
    }

    private fun removeAnnotation(annotations: NodeList<AnnotationExpr>): Boolean {
        for (annotation in annotations) {
            if (annotation.toString() == currentAnnotation.toString()) {
                annotation.remove()
                return true
            }
        }

        return false
    }

    private fun addAnnotation(annotations: NodeList<AnnotationExpr>) {
        annotations.add(currentAnnotation)
        lockedInsert = true
    }
}