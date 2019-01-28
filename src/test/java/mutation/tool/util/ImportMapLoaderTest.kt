package mutation.tool.util

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

private const val FILEPATH = "./src/test/resources/configFiles/import_map.json"

internal class ImportMapLoaderTest {

    @Test
    fun testGetMap() {
        val builder = ImportMapBuilder(File(FILEPATH))
        builder.build()
        val map = builder.map

        assertEquals(4, map.keys.size)

        assertEquals(map.getValue("a"), "mutationtool.test.annotation.a")
        assertEquals(map.getValue("b"), "mutationtool.test.annotation.b")
        assertEquals(map.getValue("c"), "mutationtool.test.annotation.c")
        assertEquals(map.getValue("rrrr"), "mutationtool.test.annotation.rrrr")
    }
}