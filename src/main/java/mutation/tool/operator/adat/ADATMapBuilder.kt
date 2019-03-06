package mutation.tool.operator.adat

import mutation.tool.util.json.AnnotationInfo

class ADATMapBuilder(private val annotationInfos:List<AnnotationInfo>) {
    val map = mutableMapOf<String, List<Map<String, String>>>()

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