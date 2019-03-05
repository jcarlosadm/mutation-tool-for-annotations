package mutation.tool.operator.rpav

import mutation.tool.util.json.AnnotationInfo
import org.json.JSONObject
import java.io.File

class RPAVMapBuilder(private val annotationInfos:List<AnnotationInfo>) {
    val map = mutableMapOf<String, Map<String, List<String>>>()

    fun build() {
        for (info in annotationInfos) {
            val name = info.name.split(".").last()

            val attrMap = mutableMapOf<String, List<String>>()
            for (attr in info.attributes) {
                attrMap.put(attr.name, attr.validValues)
            }

            if (attrMap.isNotEmpty()) map.put(name, attrMap)
        }
    }
}