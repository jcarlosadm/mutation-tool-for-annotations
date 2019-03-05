package mutation.tool.operator.adat

import mutation.tool.util.json.getAnnotationInfos
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class ADATMapBuilderTest {

    @Test
    fun testGetMap() {
        val builder = ADATMapBuilder(getAnnotationInfos(File("./src/test/resources/configFiles/annotations.json")))
        builder.build()
        val map = builder.map

        assertEquals(1, map.keys.size)
        assertTrue(map.containsKey("RequestMapping"))
        assertEquals(3, map.getValue("RequestMapping").size)

        for (attr in map.getValue("RequestMapping")) {
            assertTrue(attr["name"] == "value" || attr["name"] == "method" || attr["name"] == "headers")
            if (attr["name"] == "value") {
                assertTrue(attr.containsKey("asSingle"))
                assertEquals("true", attr.getValue("asSingle"))
            }
        }
    }
}