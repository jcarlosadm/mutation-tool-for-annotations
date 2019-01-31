package mutation.tool.util

import mutation.tool.context.InsertionPoint
import mutation.tool.operator.OperatorsEnum
import mutation.tool.operator.ada.ADAChecker
import java.io.File

const val SWTG_FILEPATH_CONFIG = "./config/SWTG_map.json"
const val RPA_FILEPATH_CONFIG = "./config/RPA_map.json"
const val ADAT_FILEPATH_CONFIG = "./config/ADAT_map.json"
const val RPAT_FILEPATH_CONFIG = "./config/RPAT_map.json"
const val RPAV_FILEPATH_CONFIG = "./config/RPAV_map.json"
const val IMPORT_MAP_FILEPATH_CONFIG = "./config/import_map.json"

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

    var adatMap:Map<String, List<Map<String, String>>>? = null

    var swtgMap:Map<String, List<InsertionPoint>>? = null

    var rpaMap:Map<String, List<String>>? = null

    var rpatMap:Map<String, Map<String, List<Map<String, String>>>>? = null

    var rpavMap:Map<String, Map<String, List<String>>>? = null

    var importMap:Map<String, String>? = null

    fun setDebugOn() {
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "debug")
    }
}