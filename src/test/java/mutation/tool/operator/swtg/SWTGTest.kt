package mutation.tool.operator.swtg

import mutation.tool.annotation.getListOfAnnotationContext
import mutation.tool.annotation.visitor.JavaStrategy
import mutation.tool.context.InsertionPoint
import mutation.tool.mutant.Mutant
import mutation.tool.operator.FILE1
import mutation.tool.util.json.getAnnotationInfos
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class SWTGTest {

    @Test
    fun testSWTG() {
        val mutants = mutableListOf<Mutant>()
        val contexts = getListOfAnnotationContext(File(FILE1), JavaStrategy())
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

    @Test
    fun testSWTGWithFile() {
        val mutants = mutableListOf<Mutant>()
        val contexts = getListOfAnnotationContext(File(FILE1), JavaStrategy())
        val builder = SWTGMapBuilder(getAnnotationInfos(File("./src/test/resources/configFiles/annotations.json")))
        builder.build()

        for (context in contexts) {
            val operator = SWTG(context, File(FILE1), contexts)
            operator.mapContextType = builder.map
            if (operator.checkContext())
                mutants += operator.mutate()
        }

        assertEquals(23, mutants.size)
    }
}