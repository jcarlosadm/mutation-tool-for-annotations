package mutation.tool.operator.rpav

import org.json.JSONObject
import java.io.File

class RPAVMapBuilder(private val jsonFile:File) {
    val map = mutableMapOf<String, Map<String, List<String>>>()

    fun build() {
        val content = jsonFile.readText(Charsets.UTF_8)
        val json = JSONObject(content)

        for (annotation in json.getJSONArray("annotations")) {
            annotation as JSONObject
            val annotationName = annotation.getString("name")

            val attrMap = mutableMapOf<String, List<String>>()
            for (attr in annotation.getJSONArray("attributes")) {
                attr as JSONObject
                val attrName = attr.getString("name")

                val valueList = mutableListOf<String>()
                for (value in attr.getJSONArray("values")) {
                    value as String
                    valueList += value
                }
                if (!valueList.isEmpty()) attrMap.put(attrName, valueList)
            }
            if (attrMap.isNotEmpty()) map.put(annotationName, attrMap)
        }
    }
}