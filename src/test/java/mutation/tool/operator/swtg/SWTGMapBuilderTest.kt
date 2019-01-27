package mutation.tool.operator.swtg

import mutation.tool.context.InsertionPoint
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class SWTGMapBuilderTest {

    private val FILE = File("./src/test/resources/configFiles/SWTG_map.json")

    @Test
    fun testGetMap() {
        val builder = SWTGMapBuilder(FILE)
        builder.build()
        assertFalse(builder.map.isEmpty())

        assertTrue(builder.map.containsKey("Autowired"))
        assertTrue(builder.map.containsKey("RequestMapping"))

        for (insertionPoint in builder.map.getValue("Autowired")) {
            assertTrue(insertionPoint == InsertionPoint.PROPERTY || insertionPoint == InsertionPoint.METHOD)
        }

        for (insertionPoint in builder.map.getValue("RequestMapping")) {
            assertTrue(insertionPoint == InsertionPoint.CLASS || insertionPoint == InsertionPoint.METHOD)
        }
    }
}