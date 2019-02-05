package mutation.tool.operator.ada

import mutation.tool.context.Context
import mutation.tool.context.InsertionPoint
import mutation.tool.operator.Operator
import mutation.tool.operator.ada.condition.Condition
import mutation.tool.operator.ada.condition.ConditionFactory
import mutation.tool.operator.ada.condition.Param
import org.json.JSONObject
import java.io.File

private const val CLASS_CONTEXT_VALUE = "CLASS"
private const val METHOD_CONTEXT_VALUE = "METHOD"
private const val FIELD_CONTEXT_VALUE = "FIELD"
private const val PARAMETER_CONTEXT_VALUE = "PARAMETER"

private const val IS_TARGET_KEY = "isTarget"
private const val ACCESS_MODIFIER_KEY = "accessModifier"
private const val RETURN_KEY = "return"
private const val ANNOTATIONS_KEY = "annotations"
private const val PARAMS_KEY = "params"
private const val TYPE_KEY = "type"
private const val NAME_KEY = "name"
private const val CONDITIONS_KEY = "conditions"
private const val CONTEXT_TYPE_KEY = "contextType"

class ADAChecker(private val jsonFile:File) {

    private val conditions = mutableMapOf<String, MutableList<Condition>>()

    fun build(): Boolean {
        if (!jsonFile.exists() || jsonFile.isDirectory) return false

        val content = jsonFile.readText(Charsets.UTF_8)
        val json = JSONObject(content)
        val conditionFactory = ConditionFactory()

        for (annotation in json.getJSONArray(ANNOTATIONS_KEY)) {
            annotation as JSONObject

            val annotationName = annotation.getString(NAME_KEY)

            val conditionList = mutableListOf<Condition>()
            for (conditionJson in annotation.getJSONArray(CONDITIONS_KEY)) {
                conditionJson as JSONObject

                var contextType = InsertionPoint.CLASS
                var isTarget = false
                var accessModifier:String? = null
                var returnType:String? = null
                var type:String? = null
                var annotationNames:MutableList<String>? = null
                var params:MutableList<Param>? = null

                when(conditionJson.getString(CONTEXT_TYPE_KEY)) {
                    CLASS_CONTEXT_VALUE -> contextType = InsertionPoint.CLASS
                    METHOD_CONTEXT_VALUE -> contextType = InsertionPoint.METHOD
                    FIELD_CONTEXT_VALUE -> contextType = InsertionPoint.PROPERTY
                    PARAMETER_CONTEXT_VALUE -> contextType = InsertionPoint.PARAMETER
                }

                if (conditionJson.keySet().contains(IS_TARGET_KEY) && conditionJson.getString(IS_TARGET_KEY) ==
                        true.toString()) isTarget = true

                if (conditionJson.keySet().contains(ACCESS_MODIFIER_KEY))
                    accessModifier = conditionJson.getString(ACCESS_MODIFIER_KEY)

                if (conditionJson.keySet().contains(RETURN_KEY)) returnType = conditionJson.getString(RETURN_KEY)

                if (conditionJson.keySet().contains(TYPE_KEY)) type = conditionJson.getString(TYPE_KEY)

                if (conditionJson.keySet().contains(ANNOTATIONS_KEY)) {
                    val annotations = mutableListOf<String>()
                    for (annotationName in conditionJson.getJSONArray(ANNOTATIONS_KEY))
                        annotations += (annotationName as String)
                    annotationNames = annotations
                }

                if (conditionJson.keySet().contains(PARAMS_KEY)) {
                    val paramList = mutableListOf<Param>()
                    for (paramJson in conditionJson.getString(PARAMS_KEY)) {
                        paramJson as JSONObject

                        val paramObj = Param()
                        paramObj.type = paramJson.getString(TYPE_KEY)

                        if (paramJson.keySet().contains(ANNOTATIONS_KEY)) {
                            val annotations = mutableListOf<String>()
                            for (annotationName in paramJson.getJSONArray(ANNOTATIONS_KEY)) {
                                annotations += (annotationName as String)
                            }

                            paramObj.annotations = annotations
                        }

                        paramList += paramObj
                    }

                    if (paramList.isNotEmpty()) params = paramList
                }

               conditionList += conditionFactory.getInstance(contextType, isTarget, accessModifier, returnType, type,
                       annotationNames, params)
            }

            if (conditionList.isNotEmpty()) conditions[annotationName] = conditionList
        }

        return true
    }

    fun check(contexts:List<Context>, javaFile:File): List<Operator> {
        val operators = mutableListOf<Operator>()

        for (context in contexts) {
            val otherContexts = contexts - context

            for (annotationName in conditions.keys) {
                var ok = true
                for (condition in conditions[annotationName]!!) {
                    if (!condition.check(context, true) && !checkOthersContexts(otherContexts, condition)) {
                        ok = false
                        break
                    }
                }

                if (!ok) continue

                val operator = ADA(context, javaFile)
                operator.annotation = annotationName
                operators += operator
            }
        }

        return operators
    }

    private fun checkOthersContexts(otherContexts: List<Context>, condition: Condition): Boolean {
        for (context in otherContexts) {
            if (condition.check(context, false)) return true
        }

        return false
    }

}
