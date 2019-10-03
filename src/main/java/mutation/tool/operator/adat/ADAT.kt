package mutation.tool.operator.adat

import com.github.javaparser.ast.NodeList
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.expr.AnnotationExpr
import com.github.javaparser.ast.expr.NormalAnnotationExpr
import mutation.tool.annotation.builder.JavaAnnotationBuilder
import mutation.tool.context.Context
import mutation.tool.mutant.JavaMutant
import mutation.tool.operator.JavaOperator
import mutation.tool.operator.OperatorsEnum
import mutation.tool.annotation.finder.javaAnnotationFinder
import java.io.File

/**
 * Add valid attribute to annotation
 * 
 * @param context context of the operator
 * @param file source file
 * @constructor Create a ADAT operator instance
 */
class ADAT(context: Context, file:File) : JavaOperator(context, file) {
    
    /**
     * map that will help the ADAT operator to build the mutants
     * 
     * Structure of map:
     * annotation -> list (map (attribute name -> attribute value))
     * example: 
     * RequestMapping -> list (map (name -> method,
     *                              value -> RequestMapping.POST))
     */
    lateinit var map:Map<String, List<Map<String, String>>>

    private lateinit var currentJavaMutant:JavaMutant
    private lateinit var currentAnnotation:AnnotationExpr
    private lateinit var currentAttr:Map<String, String>

    override fun checkContext(): Boolean {
        for (annotation in context.annotations) {
            var ok = false
            var validKey = ""
            map.keys.forEach { if (javaAnnotationFinder(annotation, it)) {ok = true; validKey = it} }
            if (!ok) continue

            if (annotation.isNormalAnnotationExpr) {
                //annotation as NormalAnnotationExpr

                for (attr in map.getValue(validKey)) {
                    var notEqual = true
                    for (pair in annotation.pairs) {
                        if (attr.getValue("name") == pair.nameAsString) {
                            notEqual = false
                            break
                        }
                    }

                    if (notEqual) return true
                }
            } else if(annotation.isSingleMemberAnnotationExpr) {
                for (attr in map.getValue(validKey)) {
                    if (attr.containsKey("asSingle") && attr.getValue("asSingle") == "true") return true
                }
            }
        }

        return false
    }

    override fun mutate(): List<JavaMutant> {
        val mutants = mutableListOf<JavaMutant>()

        for (annotation in context.getAnnotations()) {
            var ok = false
            var validKey = ""
            map.keys.forEach { if (javaAnnotationFinder(annotation, it)) {ok = true; validKey = it} }
            if (!ok) continue

            if (annotation.isNormalAnnotationExpr) {
                annotation as NormalAnnotationExpr

                for (attr in map.getValue(validKey)) {
                    var notEqual = true
                    for (pair in annotation.pairs) {
                        if (attr.getValue("name") == pair.nameAsString) {
                            notEqual = false
                            break
                        }
                    }

                    if (notEqual) createMutant(annotation, attr, mutants)
                }
            } else if(annotation.isSingleMemberAnnotationExpr) {
                var containsSingle = false
                for (attr in map.getValue(validKey)) {
                    if (attr.containsKey("asSingle") && attr.getValue("asSingle") == "true") {
                        containsSingle = true
                        break
                    }
                }

                if (containsSingle) {
                    for (attr in map.getValue(validKey)) {
                        if (!attr.containsKey("asSingle"))
                            createMutant(annotation, attr, mutants)
                    }
                }
            }
        }

        return mutants
    }

    private fun createMutant(
            annotation: AnnotationExpr,
            attr: Map<String, String>,
            javaMutants: MutableList<JavaMutant>
    ) {
        currentJavaMutant = JavaMutant(OperatorsEnum.ADAT)
        currentAnnotation = annotation
        currentAttr = attr

        currentJavaMutant.compilationUnit = this.visit()
        javaMutants += currentJavaMutant
    }

    override fun visit(n: ClassOrInterfaceDeclaration?, arg: Any?): Boolean = super.visit(n, arg) &&
            addAttr(n!!.annotations)

    override fun visit(n: MethodDeclaration?, arg: Any?): Boolean = super.visit(n, arg) &&
            addAttr(n!!.annotations)

    override fun visit(n: FieldDeclaration?, arg: Any?): Boolean = super.visit(n, arg) &&
            addAttr(n!!.annotations)

    override fun visit(n: Parameter?, arg: Any?): Boolean = super.visit(n, arg) &&
            addAttr(n!!.annotations)

    private fun addAttr(annotations: NodeList<AnnotationExpr>): Boolean {
        for (annotation in annotations) {
            if (annotation.nameAsString == currentAnnotation.nameAsString) {
                if (annotation.isNormalAnnotationExpr) {
                    annotation as NormalAnnotationExpr

                    annotation.addPair(currentAttr.getValue("name"), currentAttr.getValue("value"))
                    return true
                } else {
                    var validKey = ""
                    map.keys.forEach { if (javaAnnotationFinder(annotation, it)) validKey = it }
                    for (attr in map.getValue(validKey)) {
                        if (attr.containsKey("asSingle")) {
                            val defaultValue = getValue(annotation.toString())
                            val annotationString = makeString(annotation.nameAsString, attr, defaultValue)
                            val otherAnnotation = JavaAnnotationBuilder(annotationString).build()

                            otherAnnotation as NormalAnnotationExpr
                            otherAnnotation.addPair(currentAttr.getValue("name"), currentAttr.getValue("value"))
                            annotation.replace(otherAnnotation)

                            return true
                        }
                    }
                }
            }
        }

        return false
    }

    private fun getValue(annotationString: String): String = annotationString.substring(annotationString.
            indexOf("(") + 1, annotationString.indexOf(")")).trim()

    private fun makeString(nameAsString: String, attr: Map<String, String>, defaultValue: String): String =
            nameAsString + "(" + attr.getValue("name") + " = " + defaultValue + ")"
}