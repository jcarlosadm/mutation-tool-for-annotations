package mutation.tool.operator.swtg

import mutation.tool.context.InsertionPoint
import mutation.tool.util.json.AnnotationInfo

class SWTGMapBuilder(private val annotationInfos: List<AnnotationInfo>) {

    val map = mutableMapOf<String, List<InsertionPoint>>()

    fun build() {
        for (info in annotationInfos) {
            val key = info.name.split(".").last()

            val insertionPointList = info.targets

            if (insertionPointList.size > 1)
                map[key] = insertionPointList
        }
    }
}