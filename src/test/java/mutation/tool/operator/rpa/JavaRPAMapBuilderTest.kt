package mutation.tool.operator.rpa

import mutation.tool.util.json.getAnnotationInfos
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

private const val FILEPATH = "./src/test/resources/configFiles/annotations.json"

internal class JavaRPAMapBuilderTest {

    @Test
    fun testGetMap() {
        val builder = RPAMapBuilder(getAnnotationInfos(File(FILEPATH)))
        builder.build()
        val map = builder.map

        assertEquals(2, map.keys.size)

        for (rep in map.getValue("@org.springframework.beans.factory.annotation.Autowired"))
            assertEquals("@org.springframework.beans.factory.annotation.Qualifier", rep)

        for (rep in map.getValue("@org.springframework.beans.factory.annotation.Qualifier"))
            assertEquals("@org.springframework.beans.factory.annotation.Autowired", rep)
    }
}