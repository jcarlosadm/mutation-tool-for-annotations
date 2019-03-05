package mutation.tool.util

import mutation.tool.context.InsertionPoint
import mutation.tool.operator.OperatorsEnum
import mutation.tool.operator.ada.ADAChecker
import mutation.tool.util.json.getAnnotationInfos
import java.io.File

const val JSON_ANNOTATION_CONFIG = "./config/annotations.json"

class MutationToolConfig(val pathSources: File, val pathTests: File) {

    constructor(pathSources: File) : this(pathSources, File(""))

    var projectName:String = ""

    var threads:Int = 1
        set(value) {
            if (value > 0) field = value
        }

    var testMutants:Boolean = false

    var testOriginalProject:Boolean = false

    val operators = mutableListOf<OperatorsEnum>()

    var mutantsFolder:String = MUTANTS_FOLDER

    var adaChecker:ADAChecker? = null

    var adatMap:Map<String, List<Map<String, String>>>? = null

    var swtgMap:Map<String, List<InsertionPoint>>? = null

    var rpaMap:Map<String, List<String>>? = null

    var rpatMap:Map<String, Map<String, List<Map<String, String>>>>? = null

    var rpavMap:Map<String, Map<String, List<String>>>? = null

    val annotationInfos = getAnnotationInfos(File(JSON_ANNOTATION_CONFIG))

    fun setDebugOn() {
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "debug")
    }
}