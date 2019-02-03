package mutation.tool.operator.ada

import mutation.tool.context.Context
import mutation.tool.operator.Operator
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
private const val PARAM_TYPE = "param_type"
private const val PARAM_ANNOTATION = "param_annotation"
private const val PARAMS_KEY = "params"
private const val TYPE_KEY = "type"
private const val NAME_KEY = "name"
private const val CONDITIONS_KEY = "conditions"
private const val CONTEXT_TYPE_KEY = "contextType"

class ADAChecker(jsonFile:File) {

    private val validatorMap = mutableMapOf<String, MutableMap<String, MutableMap<String, List<String>>>>()
    private val rankMap = mutableMapOf<String, Int>()

    private var currentMap:Map<String, Int>? = null

    private var countBuild = 0

    init {
        if (jsonFile.exists() && !jsonFile.isDirectory) {
            val content = jsonFile.readText(Charsets.UTF_8)
            val json = JSONObject(content)

            for (annotation in json.getJSONArray(ANNOTATIONS_KEY)) {
                annotation as JSONObject

                val annotationName = annotation.getString(NAME_KEY)
                var isTarget = false.toString()
                countBuild = 0

                for (condition in annotation.getJSONArray(CONDITIONS_KEY)) {
                    condition as JSONObject

                    if (condition.keySet().contains(IS_TARGET_KEY) && condition.getString(IS_TARGET_KEY) ==
                            true.toString())
                        isTarget = true.toString()

                    if (condition.keySet().contains(CONTEXT_TYPE_KEY)) {
                        when(condition.getString(CONTEXT_TYPE_KEY)) {
                            CLASS_CONTEXT_VALUE -> this.buildClassCheck(annotationName, isTarget, condition)
                            METHOD_CONTEXT_VALUE -> this.buildMethodCheck(annotationName, isTarget, condition)
                            FIELD_CONTEXT_VALUE -> this.buildFieldCheck(annotationName, isTarget, condition)
                            PARAMETER_CONTEXT_VALUE -> this.buildParameterCheck(annotationName, isTarget, condition)
                        }
                    }
                }

                rankMap[annotationName] = countBuild
            }
        }
    }

    private fun buildClassCheck(annotationName: String, isTarget: String, condition: JSONObject) {
        val accessModifierString = getAccessModifier(condition)
        val annotations = getAnnotations(condition)

        var map = validatorMap[CLASS_CONTEXT_VALUE]
        if (map == null) map = mutableMapOf()

        addIsTarget(annotationName, isTarget, map)
        addAccessModifier(annotationName, accessModifierString, map)
        addAnnotations(annotationName, annotations, map)

        validatorMap[CLASS_CONTEXT_VALUE] = map
    }

    private fun buildMethodCheck(annotationName: String, isTarget: String, condition: JSONObject) {
        val returnString = getReturn(condition)
        val accessModifierString = getAccessModifier(condition)
        val annotations = getAnnotations(condition)
        val params = getParams(condition)

        var map = validatorMap[METHOD_CONTEXT_VALUE]
        if (map == null) map = mutableMapOf()

        addIsTarget(annotationName, isTarget, map)
        addAccessModifier(annotationName, accessModifierString, map)
        addAnnotations(annotationName, annotations, map)
        addReturn(annotationName, returnString, map)
        addParams(annotationName, params, map)

        validatorMap[METHOD_CONTEXT_VALUE] = map
    }

    private fun buildFieldCheck(annotationName: String, isTarget: String, condition: JSONObject) {
        val typeString = getType(condition)
        val accessModifierString = getAccessModifier(condition)
        val annotations = getAnnotations(condition)

        var map = validatorMap[FIELD_CONTEXT_VALUE]
        if (map == null) map = mutableMapOf()

        addIsTarget(annotationName, isTarget, map)
        addAccessModifier(annotationName, accessModifierString, map)
        addAnnotations(annotationName, annotations, map)
        addType(annotationName, typeString, map)

        validatorMap[FIELD_CONTEXT_VALUE] = map
    }

    private fun buildParameterCheck(annotationName: String, isTarget: String, condition: JSONObject) {
        val typeString = getType(condition)
        val annotations = getAnnotations(condition)

        var map = validatorMap[PARAMETER_CONTEXT_VALUE]
        if (map == null) map = mutableMapOf()

        addIsTarget(annotationName, isTarget, map)
        addType(annotationName, typeString, map)
        addAnnotations(annotationName, annotations, map)

        validatorMap[PARAMETER_CONTEXT_VALUE] = map
    }

    fun check(contexts:List<Context>, javaFile:File):List<Operator> {
        TODO("not implemented")
    }

    private fun getParams(condition: JSONObject): List<Param>? {
        if (!condition.keySet().contains(PARAMS_KEY)) return null
        val params = mutableListOf<Param>()
        for (paramJson in condition.getJSONArray(PARAMS_KEY)) {
            paramJson as JSONObject

            val param = Param()
            param.type = condition.getString(TYPE_KEY)

            if (paramJson.keySet().contains(ANNOTATIONS_KEY)) {
                for (annotation in paramJson.getJSONArray(ANNOTATIONS_KEY)) {
                    annotation as String
                    param.annotations += annotation
                }
            }

            params += param
        }

        return params
    }

    private fun getAnnotations(condition: JSONObject): List<String>? {
        if (!condition.keySet().contains(ANNOTATIONS_KEY)) return null
        val annotations = mutableListOf<String>()
        for (annotation in condition.getJSONArray(ANNOTATIONS_KEY)) {
            annotation as String
            annotations += annotation
        }

        return annotations
    }

    private fun getAccessModifier(condition: JSONObject): String? {
        if (!condition.keySet().contains(ACCESS_MODIFIER_KEY)) return null
        return condition.getString(ACCESS_MODIFIER_KEY)
    }

    private fun getReturn(condition: JSONObject): String? {
        if (!condition.keySet().contains(RETURN_KEY)) return null
        return condition.getString(RETURN_KEY)
    }

    private fun getType(condition: JSONObject): String? {
        if (!condition.keySet().contains(TYPE_KEY)) return null
        return condition.getString(TYPE_KEY)
    }

    private fun addIsTarget(annotationName: String, target: String, map: MutableMap<String, MutableMap<String,
            List<String>>>) {
        countBuild++
        updateMap(annotationName, map, IS_TARGET_KEY, target)
    }

    private fun addAccessModifier(annotationName: String, accessModifierString: String?,map: MutableMap<String,
            MutableMap<String, List<String>>>) {
        if (accessModifierString == null) return
        countBuild++
        updateMap(annotationName, map, ACCESS_MODIFIER_KEY, accessModifierString)
    }

    private fun addAnnotations(annotationName: String, annotations: List<String>?, map: MutableMap<String,
            MutableMap<String, List<String>>>) {
        if (annotations == null || annotations.isEmpty()) return

        for (annotationChild in annotations) {
            countBuild++
            updateMap(annotationName, map, ANNOTATIONS_KEY, annotationChild)
        }
    }

    private fun addReturn(annotationName: String, returnString: String?, map: MutableMap<String,
            MutableMap<String, List<String>>>) {
        if (returnString == null) return
        countBuild++
        updateMap(annotationName, map, RETURN_KEY, returnString)
    }

    private fun addParams(annotationName: String, params: List<Param>?, map: MutableMap<String,
            MutableMap<String, List<String>>>) {
        if (params == null || params.isEmpty()) return

        for (param in params) {
            countBuild++
            updateMap(annotationName, map, PARAM_TYPE, param.type!!)

            for (annotation in param.annotations) {
                countBuild++
                updateMap(annotationName, map, PARAM_ANNOTATION, annotation)
            }
        }
    }

    private fun addType(annotationName: String, typeString: String?, map: MutableMap<String,
            MutableMap<String, List<String>>>) {
        if (typeString == null) return
        countBuild++
        updateMap(annotationName, map, TYPE_KEY, typeString)
    }

    private fun updateMap(annotationName: String, map: MutableMap<String, MutableMap<String, List<String>>>, key:String,
                          value: String) {
        val list = getList(annotationName, map, key, value)
        val mapList = getMap(map, key)
        mapList[value] = list
        map[key] = mapList
    }

    private fun getMap(map: MutableMap<String, MutableMap<String, List<String>>>,
                       key: String): MutableMap<String, List<String>> {
        if (map.containsKey(key))
            return map.getValue(key)
        return mutableMapOf()
    }

    private fun getList(annotationName: String, map:Map<String, Map<String, List<String>>>, mainKey:String,
                        mainValue:String): List<String> {
        if (map.containsKey(mainKey) && map.getValue(mainKey).containsKey(mainValue)) {
            val list = map.getValue(mainKey).getValue(mainValue).toMutableList()
            list += annotationName
            return list
        }
        return listOf(annotationName)
    }
}

private class Param {
    var type:String? = null
    val annotations = mutableListOf<String>()
}