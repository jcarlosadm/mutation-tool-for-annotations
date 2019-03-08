package mutation.tool.operator.swtg

import mutation.tool.context.InsertionPoint
import mutation.tool.util.json.AnnotationInfo

/**
 * Build the SWTG map
 * 
 * @param annotationInfos annotation about annotations
 * @constructor Create the builder
 */
class SWTGMapBuilder(private val annotationInfos: List<AnnotationInfo>) {

    /**
     * map that will help the SWTG operator to build the mutants
     * 
     * Structure of map:
     * annotation -> list ( target )
     */
    val map = mutableMapOf<String, List<InsertionPoint>>()

    /**
     * build the map
     */
    fun build() {
        for (info in annotationInfos) {
            if (info.targets.size > 1) map[info.name] = info.targets
        }
    }
}