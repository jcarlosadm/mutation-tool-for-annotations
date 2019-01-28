package mutation.tool.operator.rpa

import org.json.JSONObject
import java.io.File

class RPAMapBuilder(private val fileConfig:File) {
    val map = mutableMapOf<String, List<String>>()

    fun build() {
        val content = fileConfig.readText(Charsets.UTF_8)
        val json = JSONObject(content)

        for (element in json.getJSONArray("annotations")) {
            element as JSONObject
            val key = element.getString("name")
            val list = mutableListOf<String>()

            for (value in element.getJSONArray("replacements")) {
                value as String
                list += value
            }

            map[key] = list
        }
    }
}