package mutation.tool.operator.rpat

import mutation.tool.annotation.getListOfAnnotationContext
import mutation.tool.annotation.visitor.JavaStrategy
import mutation.tool.mutant.Mutant
import mutation.tool.operator.FILE1
import mutation.tool.util.json.getAnnotationInfos
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class RPATTest {

    private val map = mapOf(
            "RequestMapping" to mapOf(
                    "value" to listOf(
                            mapOf("name" to "method", "value" to "GET")
                    ),
                    "" to listOf(
                            mapOf("name" to "headers", "value" to "{\"key1=2\"}")
                    )
            )
    )

    @Test
    fun testRPAT() {
        val mutants = mutableListOf<Mutant>()

        for (context in getListOfAnnotationContext(File(FILE1), JavaStrategy())) {
            val operator = RPAT(context, File(FILE1))
            operator.map = map
            if (operator.checkContext()) mutants += operator.mutate()
        }

        assertEquals(6, mutants.size)
    }

    @Test
    fun testRPATWithFile() {
        val mutants = mutableListOf<Mutant>()
        val builder = RPATMapBuilder(getAnnotationInfos(File("./src/test/resources/configFiles/annotations.json")))
        builder.build()

        for (context in getListOfAnnotationContext(File(FILE1), JavaStrategy()
        )) {
            val operator = RPAT(context, File(FILE1))
            operator.map = builder.map
            if (operator.checkContext()) mutants += operator.mutate()
        }

        assertEquals(14, mutants.size)
    }
}