package mutation.tool.util

import mutation.tool.context.InsertionPoint
import mutation.tool.operator.OperatorsEnum
import mutation.tool.operator.ada.ADAChecker
import java.io.File

// TODO: add rpa_map and import_map, and load aside of adaChecker

const val SWTG_FILEPATH_CONFIG = "./src/main/resources/SWTG_map.json"

class MutationToolConfig(val pathSources: File, val pathTests: File = File("")) {
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

    var swtgMap:Map<String, List<InsertionPoint>>? = null

    fun setDebugOn() {
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "debug")
    }
}