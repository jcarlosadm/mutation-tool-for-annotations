package mutation.tool.util.json

import mutation.tool.context.InsertionPoint
import org.json.JSONObject
import java.io.File

fun getAnnotationInfos(jsonConfig:File):List<AnnotationInfo> {
    val annotationInfos = mutableListOf<AnnotationInfo>()

    if (!jsonConfig.exists() || jsonConfig.isDirectory)
        throw Exception("${jsonConfig.name} don't exists")

    val json = JSONObject(jsonConfig.readText(Charsets.UTF_8))
    for (element in json.getJSONArray("annotations")) {
        element as JSONObject

        if (!element.has("annotation") || !element.has("targets")) continue

        val targets = getTargets(element)
        if (targets.isEmpty()) continue

        annotationInfos += AnnotationInfo(element.getString("annotation"), getReplaceableBy(element), targets,
                getAttributes(element))
    }

    return annotationInfos
}

private fun getAttributes(element: JSONObject): List<Attribute> {
    if (!element.has("attributes")) return emptyList()

    val attributes = mutableListOf<Attribute>()
    for (attribute in element.getJSONArray("attributes")) {
        attribute as JSONObject

        if (!attribute.has("name") || !attribute.has("type") || !attribute.has("validValues"))
            continue

        val attributeName = attribute.getString("name")
        val attributeType = attribute.getString("type")
        val validValues = mutableListOf<String>()
        for (validValue in attribute.getJSONArray("validValues")) {
            validValue as String
            validValues += validValue
        }
        var default = false
        if (attribute.has("default") && attribute.getString("default") == true.toString())
            default = true

        attributes += Attribute(attributeName, attributeType, validValues, default)
    }

    return attributes
}

private fun getReplaceableBy(element: JSONObject): List<String> {
    if (!element.has("replaceableBy")) return emptyList()

    val replaceableBy = mutableListOf<String>()
    for (replaceable in element.getJSONArray("replaceableBy")) {
        replaceable as String
        replaceableBy += replaceable
    }
    return replaceableBy
}

private fun getTargets(element: JSONObject): List<InsertionPoint> {
    val targets = mutableListOf<InsertionPoint>()
    for (target in element.getJSONArray("targets")) {
        when (target as String) {
            "type" -> targets += InsertionPoint.CLASS
            "method" -> targets += InsertionPoint.METHOD
            "field" -> targets += InsertionPoint.PROPERTY
            "parameter" -> targets += InsertionPoint.PARAMETER
        }
    }
    return targets
}