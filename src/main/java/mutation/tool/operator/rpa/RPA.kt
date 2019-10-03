package mutation.tool.operator.rpa

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.expr.AnnotationExpr
import mutation.tool.annotation.builder.JavaAnnotationBuilder
import mutation.tool.context.Context
import mutation.tool.mutant.JavaMutant
import mutation.tool.operator.JavaOperator
import mutation.tool.operator.OperatorsEnum
import mutation.tool.annotation.finder.javaAnnotationFinder
import java.io.File

/**
 * Replace an annotation for another
 * 
 * @param context context of the operator
 * @param file source file
 * @constructor Create a RPA operator instance
 */
class RPA(context: Context, file: File) : JavaOperator(context, file) {

    /**
     * map that will help the RPA operator to build the mutants
     * 
     * Structure of map:
     * annotation -> list (annotation)
     */
    lateinit var switchMap:Map<String, List<String>>

    private lateinit var currentJavaMutant:JavaMutant
    private lateinit var currentAnnotation: AnnotationExpr
    private lateinit var currentAnnotationRep: String

    override fun checkContext(): Boolean {
        for (annotation in context.getAnnotations()){
            var ok = false
            var validKey = ""
            switchMap.keys.forEach { if (javaAnnotationFinder(annotation, it)) {ok = true; validKey = it} }
            if (ok) {
                for (annotation2 in (context.getAnnotations() - annotation)) {
                    ok = true
                    switchMap.getValue(validKey).forEach { if (javaAnnotationFinder(annotation2, it)) ok = false }
                    if (!ok) return false
                }
                return true
            }
        }

        return false
    }

    override fun mutate(): List<JavaMutant> {
        val mutants = mutableListOf<JavaMutant>()

        for (annotation in context.getAnnotations()) {
            var ok = false
            var validKey = ""
            switchMap.keys.forEach { if (javaAnnotationFinder(annotation, it)) {ok = true; validKey = it} }
            if (!ok || switchMap[validKey] == null) continue

            ok = true
            for (annotation2 in (context.getAnnotations() - annotation)) {
                switchMap.getValue(validKey).forEach { if (javaAnnotationFinder(annotation2, it)) ok = false }
            }
            if (!ok) continue

            currentAnnotation = annotation

            for (annotationRep in switchMap.getValue(validKey)){
                currentAnnotationRep = annotationRep
                currentJavaMutant = JavaMutant(OperatorsEnum.RPA)

                currentJavaMutant.compilationUnit = this.visit()
                mutants += currentJavaMutant
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
                return annotation.replace(JavaAnnotationBuilder(currentAnnotationRep).build())
            }
        }

        return false
    }
}