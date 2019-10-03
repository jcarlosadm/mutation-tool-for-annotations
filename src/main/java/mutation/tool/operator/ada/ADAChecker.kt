package mutation.tool.operator.ada

import mutation.tool.context.Context
import mutation.tool.context.InsertionPoint
import mutation.tool.operator.JavaOperator
import mutation.tool.annotation.finder.javaAnnotationFinder
import mutation.tool.util.json.AnnotationInfo
import java.io.File

/**
 * Generator of ADA operators
 *
 * @param annotationInfos informations about annotations
 * @constructor create a generator of ADA operators
 */
class ADAChecker(private val annotationInfos:List<AnnotationInfo>) {

    private val targetMap = mutableMapOf<InsertionPoint, MutableList<AnnotationInfo>>()

    /**
     * build the generator on basis of information about annotations
     */
    fun build() {
        for (info in annotationInfos) {
            for (target in info.targets) {
                var annotationInfos:MutableList<AnnotationInfo>
                if (targetMap.containsKey(target)) {
                    if (this.listContainsInfo(targetMap.getValue(target), info)) continue
                    annotationInfos = targetMap.getValue(target)
                } else {
                    annotationInfos = mutableListOf()
                    targetMap[target] = annotationInfos
                }

                annotationInfos.add(info)
            }
        }
    }

    private fun listContainsInfo(annotationInfos: MutableList<AnnotationInfo>, info: AnnotationInfo): Boolean {
        for (annotationInfo in annotationInfos) {
            if (annotationInfo.name == info.name) return true
        }

        return false
    }

    /**
     * Generate ADA operators on basis of contexts
     * @param contexts list of contexts
     * @return list of operators
     */
    fun check(contexts:List<Context>, javaFile:File): List<JavaOperator> {
        val operators = mutableListOf<JavaOperator>()

        for (context in contexts) {
            if (!targetMap.containsKey(context.getInsertionPoint())) continue

            for (info in targetMap.getValue(context.getInsertionPoint())) {
                val name = info.name

                var ok = true
                for (annotation in context.annotations) {
                    if (javaAnnotationFinder(annotation, name)){
                        ok = false
                        break
                    }
                }

                if (!ok) continue

                for (string in info.annotationStrings) {
                    val operator = ADA(context, javaFile)
                    operator.annotation = string
                    operators += operator
                }
            }
        }

        return operators
    }

}
