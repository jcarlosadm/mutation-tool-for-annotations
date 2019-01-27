package mutation.tool.operator.swtg

import mutation.tool.annotation.getListOfAnnotationContext
import mutation.tool.context.InsertionPoint
import mutation.tool.mutant.Mutant
import mutation.tool.operator.FILE1
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class SWTGTest {

    @Test
    fun testSWTG() {
        val mutants = mutableListOf<Mutant>()
        val contexts = getListOfAnnotationContext(File(FILE1))
        val map = mapOf(
                "Autowired" to listOf(InsertionPoint.PROPERTY, InsertionPoint.METHOD),
                "RequestMapping" to listOf(InsertionPoint.CLASS, InsertionPoint.METHOD)
        )

        for (context in contexts) {
            val operator = SWTG(context, File(FILE1), contexts)
            operator.mapContextType = map
            if (operator.checkContext())
                mutants += operator.mutate()
        }

        assertEquals(14, mutants.size)
    }
}