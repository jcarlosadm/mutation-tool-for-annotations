package mutation.tool.operator.swtg

import mutation.tool.context.InsertionPoint
import org.json.JSONObject
import java.io.File
import java.lang.Exception

class SWTGMapBuilder(private val jsonFile:File) {

    val map = mutableMapOf<String, List<InsertionPoint>>()

    fun build() {
        if (!jsonFile.exists() || jsonFile.isDirectory) throw Exception("invalid file")

        val content = jsonFile.readText(Charsets.UTF_8)
        val jsonObj = JSONObject(content)
        for (element in jsonObj.getJSONArray("annotations")) {
            element as JSONObject
            val key = element.getString("name")
            val insertionPointList = mutableListOf<InsertionPoint>()
            for (string in element.getJSONArray("insertionPoints")) {
                for (insertionPoint in InsertionPoint.values()){
                    if (string == insertionPoint.name)
                        insertionPointList += insertionPoint
                }
            }

            if (!insertionPointList.isEmpty())
                map.put(key, insertionPointList)
        }
    }
}