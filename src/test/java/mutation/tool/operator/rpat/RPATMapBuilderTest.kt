package mutation.tool.operator.rpat

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class RPATMapBuilderTest {

    @Test
    fun testGetMap() {
        val builder = RPATMapBuilder(File("./src/test/resources/configFiles/RPAT_map.json"))
        builder.build()

        val map = builder.map

        assertEquals(1, map.keys.size)
        assertEquals(2, map.getValue("RequestMapping").keys.size)

        assertTrue(map.containsKey("RequestMapping"))
        assertTrue(map.getValue("RequestMapping").containsKey("value"))
        assertTrue(map.getValue("RequestMapping").containsKey(""))

        for (attr in map.getValue("RequestMapping").getValue("value")) {
            assertEquals("method", attr.getValue("name"))
            assertEquals("GET", attr.getValue("value"))
        }

        for (attr in map.getValue("RequestMapping").getValue("")) {
            assertEquals("headers", attr.getValue("name"))
            assertEquals("{\"key1=2\"}", attr.getValue("value"))
        }

    }
}