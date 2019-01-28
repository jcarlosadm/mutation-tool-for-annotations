package mutation.tool.util

import org.json.JSONObject
import java.io.File

class ImportMapBuilder(private val importFile:File) {
    val map = mutableMapOf<String, String>()

    fun build() {
        val content = importFile.readText(Charsets.UTF_8)
        val json = JSONObject(content)

        for (element in json.getJSONArray("imports")) {
            element as JSONObject
            map[element.getString("name")] = element.getString("import")
        }
    }
}