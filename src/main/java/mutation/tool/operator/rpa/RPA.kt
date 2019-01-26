package mutation.tool.operator.rpa

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.expr.AnnotationExpr
import mutation.tool.annotation.AnnotationBuilder
import mutation.tool.context.Context
import mutation.tool.mutant.Mutant
import mutation.tool.mutant.MutateVisitor
import mutation.tool.operator.Operator
import mutation.tool.operator.OperatorsEnum
import mutation.tool.util.getAnnotations
import java.io.File

/**
 * Replace an annotation for another
 */
class RPA(context: Context, file: File) : Operator(context, file) {

    lateinit var switchMap:Map<String, List<String>>
    lateinit var importMap:Map<String, String>

    private lateinit var currentMutant:Mutant
    private lateinit var currentAnnotation: AnnotationExpr
    private lateinit var currentAnnotationRep: String

    override fun checkContext(): Boolean {
        for (annotation in getAnnotations(context)){
            if (switchMap.containsKey(annotation.nameAsString))
                return true
        }

        return false
    }

    override fun mutate(): List<Mutant> {
        val mutants = mutableListOf<Mutant>()

        val mutateVisitor = MutateVisitor(this)
        val compUnit = JavaParser.parse(file)

        for (annotation in getAnnotations(context)) {
            if (!switchMap.containsKey(annotation.nameAsString) || switchMap[annotation.nameAsString] == null) continue

            currentAnnotation = annotation

            for (annotationRep in switchMap.getValue(annotation.nameAsString)){
                val newCompUnit = compUnit.clone()
                currentAnnotationRep = annotationRep
                locked = false
                currentMutant = Mutant(OperatorsEnum.RPA)

                mutateVisitor.visit(newCompUnit, null)
                currentMutant.compilationUnit = newCompUnit
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
                val op = annotation.findCompilationUnit()

                if (annotation.replace(AnnotationBuilder(currentAnnotationRep).build())){
                    if (importMap.containsKey(this.getName(currentAnnotationRep))) {
                        if (op.isPresent)
                            op.get().addImport(importMap[this.getName(currentAnnotationRep)])
                    }

                    locked = true
                    return true
                }
            }
        }

        return false
    }

    private fun getName(annotationString:String):String {
        if (!annotationString.contains("("))
            return annotationString
        return annotationString.substring(0, annotationString.indexOf("("))
    }
}