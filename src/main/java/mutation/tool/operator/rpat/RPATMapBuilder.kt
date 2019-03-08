package mutation.tool.operator.rpat

import mutation.tool.util.json.AnnotationInfo

/**
 * Build the RPAT map
 * 
 * @param annotationInfos information about annotations
 * @constructor Creates the builder
 */
class RPATMapBuilder(private val annotationInfos:List<AnnotationInfo>) {

    /**
     * map that will help the RPAT operator to build the mutants
     * 
     * Structure of map:
     * annotation -> map (attribute -> list ( 
     *                                   map (attributeName -> attributeValue)
     *                                      ))
     */
    val map = mutableMapOf<String, Map<String, List<Map<String, String>>>>()

    /**
     * build the map
     */
    fun build() {
        for (info in annotationInfos) {
            val name = info.name

            val attrMap = mutableMapOf<String, List<Map<String, String>>>()

            for (attribute in info.attributes) {
                val attrName = attribute.name

                val repList = mutableListOf<Map<String, String>>()
                for (replacement in (info.attributes - attribute)) {
                    val repName = replacement.name
                    val repValue = replacement.validValues[0]

                    repList += mapOf("name" to repName, "value" to repValue)
                }

                if (!repList.isEmpty()) {
                    attrMap[attrName] = repList
                    if (attribute.default) {
                        attrMap[""] = repList
                    }
                }
            }

            if (!attrMap.keys.isEmpty()) map[name] = attrMap
        }
    }
}