package mutation.tool.operator.swtg

import mutation.tool.context.InsertionPoint
import mutation.tool.util.json.getAnnotationInfos
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class SWTGMapBuilderTest {

    @Test
    fun testGetMap() {
        val builder = SWTGMapBuilder(getAnnotationInfos(File("./src/test/resources/configFiles/annotations.json")))
        builder.build()
        assertFalse(builder.map.isEmpty())

        assertTrue(builder.map.containsKey("Autowired"))
        assertTrue(builder.map.containsKey("RequestMapping"))
        assertTrue(builder.map.containsKey("Qualifier"))

        for (insertionPoint in builder.map.getValue("Autowired")) {
            assertTrue(insertionPoint == InsertionPoint.PROPERTY || insertionPoint == InsertionPoint.METHOD)
        }

        for (insertionPoint in builder.map.getValue("RequestMapping")) {
            assertTrue(insertionPoint == InsertionPoint.CLASS || insertionPoint == InsertionPoint.METHOD)
        }

        for (insertionPoint in builder.map.getValue("Qualifier")) {
            assertTrue(insertionPoint == InsertionPoint.PROPERTY || insertionPoint == InsertionPoint.PARAMETER)
        }
    }
}