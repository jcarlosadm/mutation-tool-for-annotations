package mutation.tool.util

import mutation.tool.operator.OperatorsEnum
import java.io.File

class MutationToolConfig(val pathSources: File, val pathTests: File) {
    var projectName:String = ""
    var threads = 1
        set(value) {
            if (value > 0) field = value
        }
    var testMutants:Boolean = true
    var testOriginalProject:Boolean = true

    val operators = mutableListOf<OperatorsEnum>()
}