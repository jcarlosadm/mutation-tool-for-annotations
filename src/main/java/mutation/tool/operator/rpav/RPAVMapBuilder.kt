package mutation.tool.operator.rpav

import mutation.tool.util.json.AnnotationInfo

class RPAVMapBuilder(private val annotationInfos:List<AnnotationInfo>) {
    val map = mutableMapOf<String, Map<String, List<String>>>()

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