package mutation.tool.operator.rpat

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.expr.AnnotationExpr
import com.github.javaparser.ast.expr.NormalAnnotationExpr
import mutation.tool.context.Context
import mutation.tool.mutant.Mutant
import mutation.tool.mutant.MutateVisitor
import mutation.tool.operator.Operator
import mutation.tool.operator.OperatorsEnum
import mutation.tool.util.getAnnotations
import java.io.File

/**
 * Replace a code annotation attribute by another
 */
class RPAT(context: Context, file: File) : Operator(context, file) {

    lateinit var map: Map<String, Map<String, List<Map<String, String>>>>

    private lateinit var currentMutant:Mutant
    private lateinit var currentAnnotation: AnnotationExpr
    private lateinit var currentAttr:String
    private lateinit var currentAttrRep:String

    override fun checkContext(): Boolean {
        for (annotation in getAnnotations(context)){
            if (!map.containsKey(annotation.nameAsString)) continue

            if (annotation.isSingleMemberAnnotationExpr && map.getValue(annotation.nameAsString).containsKey(""))
                return true
            else if (annotation.isNormalAnnotationExpr){
                annotation as NormalAnnotationExpr
                // check each attr of annotation
                for (pair in annotation.pairs) {
                    // if present on map
                    if (map.getValue(annotation.nameAsString).containsKey(pair.nameAsString)) {
                        // check if replacements is not present on annotation
                        var notContain = true
                        for (mapAttr in map.getValue(annotation.nameAsString).getValue(pair.nameAsString)) {
                            for (anotherPair in annotation.pairs) {
                                if (anotherPair.nameAsString == mapAttr.getValue("name")) {
                                    notContain = false
                                    break
                                }
                            }
                        }
                        if (notContain) return true
                    }
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
            if (!map.containsKey(annotation.nameAsString)) continue

            if (annotation.isSingleMemberAnnotationExpr && map.getValue(annotation.nameAsString).containsKey("")) {
                for (attrMap in map.getValue(annotation.nameAsString).getValue(""))
                    createMutant(annotation, attrMap, compUnit, mutateVisitor, mutants)
            }
            else if (annotation.isNormalAnnotationExpr) {
                
            }
        }

        return mutants
    }

    private fun createMutant(
            annotation: AnnotationExpr,
            attrMap: Map<String, String>,
            compUnit: CompilationUnit,
            mutateVisitor: MutateVisitor,
            mutants: MutableList<Mutant>
    ) {
        currentAnnotation = annotation
        currentMutant = Mutant(OperatorsEnum.RPAT)
        currentAttr = ""
        currentAttrRep = attrMap.getValue("name")

        val newCompUnit = compUnit.clone()
        mutateVisitor.visit(newCompUnit, null)
        currentMutant.compilationUnit = newCompUnit
        mutants += currentMutant
    }
}