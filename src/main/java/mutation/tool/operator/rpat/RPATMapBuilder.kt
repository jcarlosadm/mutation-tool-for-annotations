package mutation.tool.operator.rpat

import mutation.tool.util.json.AnnotationInfo
import org.json.JSONObject
import java.io.File

class RPATMapBuilder(private val annotationInfos:List<AnnotationInfo>) {
    val map = mutableMapOf<String, Map<String, List<Map<String, String>>>>()

    fun build() {
        for (info in annotationInfos) {
            val name = info.name.split(".").last()

            val attrMap = mutableMapOf<String, List<Map<String, String>>>()

            for (attribute in info.attributes) {
                val attrName = attribute.name

                val repList = mutableListOf<Map<String, String>>()
                for (replacement in (info.attributes - attribute)) {
                    val repName = replacement.name
                    val repValue = replacement.validValues[0]

                    repList += mapOf("name" to repName, "value" to repValue)
                }

                if (!repList.isEmpty()) attrMap.put(attrName, repList)
            }

            if (!attrMap.keys.isEmpty()) map.put(name, attrMap)
        }
    }
}