package mutation.tool.operator.rpat

import org.json.JSONObject
import java.io.File

class RPATMapBuilder(private val jsonFile:File) {
    val map = mutableMapOf<String, Map<String, List<Map<String, String>>>>()

    fun build() {
        val content = jsonFile.readText(Charsets.UTF_8)
        val json = JSONObject(content)

        for (annotation in json.getJSONArray("annotations")) {
            annotation as JSONObject

            val annotationName = annotation.getString("name")

            val attrMap = mutableMapOf<String, List<Map<String, String>>>()

            for (attribute in annotation.getJSONArray("attributes")) {
                attribute as JSONObject

                val attrName = attribute.getString("name")

                val repList = mutableListOf<Map<String, String>>()
                for (replacement in attribute.getJSONArray("replacements")) {
                    replacement as JSONObject

                    val repName = replacement.getString("name")
                    val repValue = replacement.getString("value")

                    repList += mapOf("name" to repName, "value" to repValue)
                }

                if (!repList.isEmpty()) attrMap.put(attrName, repList)
            }

            if (!attrMap.keys.isEmpty()) map.put(annotationName, attrMap)
        }
    }
}