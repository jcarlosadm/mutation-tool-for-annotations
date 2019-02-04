package mutation.tool.operator.rpav

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class RPAVMapBuilderTest {

    @Test
    fun testGetMap() {
        val builder = RPAVMapBuilder(File("./src/test/resources/configFiles/RPAV_map.json"))
        builder.build()
        val map = builder.map

        assertEquals(1, map.keys.size)
        assertTrue(map.containsKey("RequestMapping"))
        assertEquals(2, map.getValue("RequestMapping").keys.size)

        for (attr in map.getValue("RequestMapping").getValue("value"))
            assertTrue(attr == "\"aaaa\"" || attr == "\"bbbb\"" || attr == "\"cccc\"")

        for (attr in map.getValue("RequestMapping").getValue(""))
            assertTrue(attr == "\"aaaa\"" || attr == "\"bbbb\"" || attr == "\"cccc\"")
    }
}