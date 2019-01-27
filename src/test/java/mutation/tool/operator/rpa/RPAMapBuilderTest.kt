package mutation.tool.operator.rpa

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

private const val FILEPATH = "./src/test/resources/configFiles/RPA_map.json"

internal class RPAMapBuilderTest {

    @Test
    fun testGetMap() {
        val builder = RPAMapBuilder(File(FILEPATH))
        builder.build()
        val map = builder.map

        assertEquals(2, map.keys.size)

        for (rep in map.getValue("Autowired"))
            assertTrue(rep == "@a" || rep == "@b(1)" || rep == "@c(name=\"aaa\")")

        for (rep in map.getValue("Qualifier"))
            assertEquals("@rrrr", rep)
    }
}