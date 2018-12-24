package mutation.tool.util

import mu.KotlinLogging
import mutation.tool.operator.OperatorsEnum
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

private val logger = KotlinLogging.logger{}

class MutationToolConfig(val pathSources: File, val pathTests: File = File("")) {
    var projectName:String = ""
    var threads = 1
        set(value) {
            if (value > 0) field = value
        }
    var testMutants:Boolean = false
    var testOriginalProject:Boolean = false

    val operators = mutableListOf<OperatorsEnum>()

    var mutantsFolder:String = MUTANTS_FOLDER

    fun setDebugOn() {
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "debug");
    }
}