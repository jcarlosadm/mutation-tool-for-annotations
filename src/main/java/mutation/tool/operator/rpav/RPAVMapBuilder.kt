package mutation.tool.operator.rpav

import mutation.tool.util.json.AnnotationInfo

/**
 * Build the RPAV map
 * 
 * @param annotationInfos information about annotations
 * @constructor Create the builder
 */
class RPAVMapBuilder(private val annotationInfos:List<AnnotationInfo>) {

    /**
     * map that will help the RPAV operator to build the mutants
     * 
     * Structure of map:
     * annotation -> map (attributeName -> list ( attributeValue ) )
     */
    val map = mutableMapOf<String, Map<String, List<String>>>()

    /**
     * build the map
     */
    fun build() {
        for (info in annotationInfos) {
            val name = info.name

            val attrMap = mutableMapOf<String, List<String>>()
            for (attr in info.attributes) {
                attrMap[attr.name] = attr.validValues
            }

            if (attrMap.isNotEmpty()) map[name] = attrMap
        }
    }
}