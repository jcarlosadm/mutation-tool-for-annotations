package mutation.tool.operator.rpav

import mutation.tool.util.json.getAnnotationInfos
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class RPAVMapBuilderTest {

    @Test
    fun testGetMap() {
        val builder = RPAVMapBuilder(getAnnotationInfos(File("./src/test/resources/configFiles/annotations.json")))
        builder.build()
        val map = builder.map

        assertEquals(1, map.keys.size)
        assertTrue(map.containsKey("@org.springframework.web.bind.annotation.RequestMapping"))
        assertEquals(3, map.getValue("@org.springframework.web.bind.annotation.RequestMapping").keys.size)

        for (attr in map.getValue("@org.springframework.web.bind.annotation.RequestMapping").getValue("value"))
            assertEquals("\"/ex/foo\"", attr)

        for (attr in map.getValue("@org.springframework.web.bind.annotation.RequestMapping").getValue("headers"))
            assertEquals("\"content-type=text/*\"", attr)

        for (attr in map.getValue("@org.springframework.web.bind.annotation.RequestMapping").getValue("method"))
            assertEquals("org.springframework.web.bind.annotation.RequestMethod.POST", attr)
    }
}