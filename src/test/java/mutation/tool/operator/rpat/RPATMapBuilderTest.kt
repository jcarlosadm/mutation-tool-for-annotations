package mutation.tool.operator.rpat

import mutation.tool.util.json.getAnnotationInfos
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class RPATMapBuilderTest {

    @Test
    fun testGetMap() {
        val builder = RPATMapBuilder(getAnnotationInfos(File("./src/test/resources/configFiles/annotations.json")))
        builder.build()

        val map = builder.map

        assertEquals(1, map.keys.size)
        assertEquals(3, map.getValue("RequestMapping").keys.size)

        assertTrue(map.containsKey("RequestMapping"))
        assertTrue(map.getValue("RequestMapping").containsKey("value"))
        assertTrue(map.getValue("RequestMapping").containsKey("method"))
        assertTrue(map.getValue("RequestMapping").containsKey("headers"))

        for (attr in map.getValue("RequestMapping").getValue("value")) {
            assertTrue(attr.getValue("name") == "method" || attr.getValue("name") == "headers")
            assertTrue(attr.getValue("value") == "org.springframework.web.bind.annotation.RequestMethod.POST" ||
                    attr.getValue("value") == "\"content-type=text/*\"")
        }

        for (attr in map.getValue("RequestMapping").getValue("method")) {
            assertTrue(attr.getValue("name") == "value" || attr.getValue("name") == "headers")
        }

        for (attr in map.getValue("RequestMapping").getValue("headers")) {
            assertTrue(attr.getValue("name") == "method" || attr.getValue("name") == "value")
        }

    }
}