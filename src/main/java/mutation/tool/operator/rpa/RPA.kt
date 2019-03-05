package mutation.tool.operator.rpa

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.expr.AnnotationExpr
import mutation.tool.annotation.AnnotationBuilder
import mutation.tool.context.Context
import mutation.tool.mutant.Mutant
import mutation.tool.operator.Operator
import mutation.tool.operator.OperatorsEnum
import java.io.File

/**
 * Replace an annotation for another
 */
class RPA(context: Context, file: File) : Operator(context, file) {
    lateinit var switchMap:Map<String, List<String>>

    private lateinit var currentMutant:Mutant
    private lateinit var currentAnnotation: AnnotationExpr
    private lateinit var currentAnnotationRep: String

    override fun checkContext(): Boolean {
        for (annotation in context.getAnnotations()){
            var ok = false
            var validKey = ""
            for (key in switchMap.keys) {
                if (key.contains(annotation.nameAsString)) {
                    validKey = key
                    ok = true
                    break
                }
            }
            if (ok) {
                for (annotation2 in context.getAnnotations()) {
                    for (value in switchMap.getValue(validKey)) {
                        if (value.contains(annotation2.nameAsString))
                            return false
                    }
                }
                return true
            }
        }

        return false
    }

    override fun mutate(): List<Mutant> {
        val mutants = mutableListOf<Mutant>()

        for (annotation in context.getAnnotations()) {
            var validKey = ""
            var ok = false
            for (key in switchMap.keys) {
                if (key.contains(annotation.nameAsString)) {
                    validKey = key
                    ok = true
                    break
                }
            }
            if (ok) {
                for (annotation2 in context.getAnnotations()) {
                    for (value in switchMap.getValue(validKey)) {
                        if (value.contains(annotation2.nameAsString)) {
                            ok = false
                            break
                        }
                    }
                    if (!ok) break
                }
            }
            if (!ok) continue

            currentAnnotation = annotation

            for (annotationRep in switchMap.getValue(validKey)){
                currentAnnotationRep = annotationRep
                currentMutant = Mutant(OperatorsEnum.RPA)

                currentMutant.compilationUnit = this.visit()
                mutants += currentMutant
            }
        }

        return mutants
    }

    override fun visit(n: ClassOrInterfaceDeclaration?, arg: Any?): Boolean = super.visit(n, arg) &&
            replaceAnnotation(n?.annotations!!)

    override fun visit(n: MethodDeclaration?, arg: Any?): Boolean = super.visit(n, arg) &&
            replaceAnnotation(n?.annotations!!)

    override fun visit(n: FieldDeclaration?, arg: Any?): Boolean = super.visit(n, arg) &&
            replaceAnnotation(n?.annotations!!)

    override fun visit(n: Parameter?, arg: Any?): Boolean = super.visit(n, arg) &&
            replaceAnnotation(n?.annotations!!)

    private fun replaceAnnotation(annotations: List<AnnotationExpr>):Boolean {
        for (annotation in annotations) {
            if (annotation.toString() == currentAnnotation.toString()) {
                return annotation.replace(AnnotationBuilder(currentAnnotationRep).build())
            }
        }

        return false
    }
}