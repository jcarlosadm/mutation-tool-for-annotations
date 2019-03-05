package mutation.tool.operator.adat

import mutation.tool.util.json.AnnotationInfo
import org.json.JSONObject
import java.io.File

class ADATMapBuilder(private val annotationInfos:List<AnnotationInfo>) {
    val map = mutableMapOf<String, List<Map<String, String>>>()

    fun build() {

        for (info in annotationInfos) {
            if (info.attributes.isEmpty()) continue

            val annotationName = info.name.split(".").last()

            val attrList = mutableListOf<Map<String, String>>()
            for (attr in info.attributes) {
                val attrMap = mutableMapOf(
                        "name" to attr.name,
                        "value" to attr.validValues[0]
                )
                if (attr.default) {
                    val asSingleValue = "true"
                    attrMap.put("asSingle", asSingleValue)
                }

                attrList += attrMap
            }

            if (!attrList.isEmpty()) map.put(annotationName, attrList)
        }
    }
}