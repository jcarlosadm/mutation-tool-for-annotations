package mutation.tool.annotation

import mutation.tool.util.InsertionPoint
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.core.AnyOf
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

private const val PATH = "./src/test/resources/fakeProject/src/main/java/TarefasController.java"

internal class AnnotationContextTest {

    @Test
    fun testGetListOfAnnotationContext() {
        val file = File(PATH)
        val list = getListOfAnnotationContext(file)

        assertEquals(10, list.size)

        var classCount = 0
        var propertyCount = 0
        var methodCount = 0
        for (context in list) {
            when(context.insertionPoint) {
                InsertionPoint.CLASS -> classCount++
                InsertionPoint.METHOD -> methodCount++
                InsertionPoint.PROPERTY -> propertyCount++
            }

            MatcherAssert.assertThat(context.annotation.toString(), AnyOf.
                    anyOf(CoreMatchers.containsString("@Controller"),
                            CoreMatchers.containsString("@Qualifier"),
                            CoreMatchers.containsString("@Autowired"),
                            CoreMatchers.containsString("@RequestMapping")))
        }

        assertEquals(1, classCount)
        assertEquals(2, propertyCount)
        assertEquals(7, methodCount)
    }
}