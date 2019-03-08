package mutation.tool.operator.rpa

import mutation.tool.util.json.AnnotationInfo

/**
 * Build the RPA map
 *
 * @param annotationInfos infomation about annotations
 * @constructor create the builder
 */
class RPAMapBuilder(private val annotationInfos: List<AnnotationInfo>) {

    /**
     * map that will help the RPA operator to build the mutants
     * 
     * Structure of map:
     * annotation -> list (annotation)
     */
    val map = mutableMapOf<String, List<String>>()

    /**
     * build the map
     */
    fun build() {
        for (info in annotationInfos) {

            val key = info.name
            val list = mutableListOf<String>()

            for (value in info.replaceableBy) {
                for (annot2 in annotationInfos) {
                    if (annot2.name == value) {
                        list += annot2.annotationStrings[0]
                        break
                    }
                }
            }
            if (list.isNotEmpty())
                map[key] = list
        }
    }
}