package mutation.tool.operator.adat

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class ADATMapBuilderTest {

    @Test
    fun testGetMap() {
        val builder = ADATMapBuilder(File("./src/test/resources/configFiles/ADAT_map.json"))
        builder.build()
        val map = builder.map

        assertEquals(1, map.keys.size)
        assertTrue(map.containsKey("RequestMapping"))
        assertEquals(3, map.getValue("RequestMapping").size)

        for (attr in map.getValue("RequestMapping")) {
            assertTrue(attr["name"] == "value" || attr["name"] == "method" || attr["name"] == "headers")
            if (attr["name"] == "value") {
                assertTrue(attr.containsKey("asSingle"))
            }
        }
    }
}