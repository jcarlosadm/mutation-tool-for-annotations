package mutation.tool.operator.adat

import mutation.tool.util.json.AnnotationInfo

/**
 * Build the ADAT map
 * 
 * @param annotationInfos information about annotations
 * @constructor create the builder
 */
class ADATMapBuilder(private val annotationInfos:List<AnnotationInfo>) {

    /**
     * map that will help the ADAT operator to build the mutants
     * 
     * Structure of map:
     * annotation -> list (map (attribute name -> attribute value))
     * example: 
     * RequestMapping -> list (map (name -> method,
     *                              value -> RequestMapping.POST))
     */
    val map = mutableMapOf<String, List<Map<String, String>>>()

    /**
     * build the map
     */
    fun build() {

        for (info in annotationInfos) {
            if (info.attributes.isEmpty()) continue

            val annotationName = info.name

            val attrList = mutableListOf<Map<String, String>>()
            for (attr in info.attributes) {
                val attrMap = mutableMapOf(
                        "name" to attr.name,
                        "value" to attr.validValues[0]
                )
                if (attr.default) {
                    val asSingleValue = "true"
                    attrMap["asSingle"] = asSingleValue
                }

                attrList += attrMap
            }

            if (!attrList.isEmpty()) map[annotationName] = attrList
        }
    }
}