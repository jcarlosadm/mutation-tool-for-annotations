package mutation.tool.util

import mutation.tool.context.InsertionPoint
import mutation.tool.operator.OperatorsEnum
import mutation.tool.operator.ada.ADAChecker
import mutation.tool.util.json.getAnnotationInfos
import java.io.File

const val JSON_ANNOTATION_CONFIG = "./config/annotations.json"

/**
 * Mutation tool configuration class
 *
 * @param pathSources path to the source folder (without tests)
 * @param pathTests path to the test folder (without sources)
 * @constructor create a set of configurations
 */
class MutationToolConfig(val pathSources: File, val pathTests: File) {

    /**
     * create a set of configurations
     * 
     * @param pathSources path of the source folder (without tests)
     */
    constructor(pathSources: File) : this(pathSources, File(""))

    /**
     * name of the original project to be mutated
     */
    var projectName:String = ""

    /**
     * number of threads. default value is 1. must be greater than zero.
     */
    var threads:Int = 1
        set(value) {
            if (value > 0) field = value
        }

    /**
     * if the tool will test the mutants
     */
    var testMutants:Boolean = false

    /**
     * if the tool will test the original project
     */
    var testOriginalProject:Boolean = false

    /**
     * list of valid operators
     */
    val operators = mutableListOf<OperatorsEnum>()

    /**
     * folder where the mutants are generated
     */
    var mutantsFolder:String = MUTANTS_FOLDER

    /**
     * Generator of ADA operators
     */
    var adaChecker:ADAChecker? = null

    /**
     * map of ADAT operator
     */
    var adatMap:Map<String, List<Map<String, String>>>? = null

    /**
     * map of SWTG operator
     */
    var swtgMap:Map<String, List<InsertionPoint>>? = null

    /**
     * map of RPA operator
     */
    var rpaMap:Map<String, List<String>>? = null

    /**
     * map of RPAT operator
     */
    var rpatMap:Map<String, Map<String, List<Map<String, String>>>>? = null

    /**
     * map of RPAV operator
     */
    var rpavMap:Map<String, Map<String, List<String>>>? = null

    /**
     * informations about annotations
     */
    val annotationInfos = getAnnotationInfos(File(JSON_ANNOTATION_CONFIG))

    /**
     * Language processing
     */
    var language = Language.JAVA

    /**
     * turn on debug information
     */
    fun setDebugOn() {
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "debug")
    }
}