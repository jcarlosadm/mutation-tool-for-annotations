package mutation.tool.operator.rpa

import mutation.tool.util.json.AnnotationInfo

class RPAMapBuilder(private val annotationInfos: List<AnnotationInfo>) {
    val map = mutableMapOf<String, List<String>>()

    fun build() {
        for (info in annotationInfos) {

            val key = info.name.split(".").last()
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