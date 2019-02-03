package mutation.tool.operator.adat

import org.json.JSONObject
import java.io.File

class ADATMapBuilder(private val mapFile:File) {
    val map = mutableMapOf<String, List<Map<String, String>>>()

    fun build() {
        val content = mapFile.readText(Charsets.UTF_8)
        val json = JSONObject(content)

        for (annotation in json.getJSONArray("annotations")) {
            annotation as JSONObject
            val annotationName = annotation.getString("name")

            val attrList = mutableListOf<Map<String, String>>()
            for (attr in annotation.getJSONArray("attributes")) {
                attr as JSONObject

                val attrMap = mutableMapOf(
                        "name" to attr.getString("name"),
                        "value" to attr.getString("value")
                )
                if (attr.keySet().contains("asSingle")) {
                    val asSingleValue = attr.getString("asSingle")
                    attrMap.put("asSingle", asSingleValue)
                }

                attrList += attrMap
            }

            if (!attrList.isEmpty()) map.put(annotationName, attrList)
        }
    }
}