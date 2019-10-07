package mutation.tool.operator.rpav

import mutation.tool.annotation.getListOfAnnotationContext
import mutation.tool.annotation.visitor.JavaStrategy
import mutation.tool.mutant.JavaMutant
import mutation.tool.operator.FILE1
import mutation.tool.util.json.getAnnotationInfos
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class RPAVTest {

    val map = mapOf(
            "RequestMapping" to mapOf(
                    "value" to listOf("\"aaaa\"", "\"bbbb\"", "\"cccc\""),
                    "" to listOf("\"aaaa\"", "\"bbbb\"", "\"cccc\"")
            )
    )

    /*@Test
    fun testRPAV(){
        val mutants = mutableListOf<JavaMutant>()

        for (context in getListOfAnnotationContext(File(FILE1), JavaStrategy())) {
            val operator = RPAV(context, File(FILE1))
            operator.map = map
            if (operator.checkContext()) mutants += operator.mutate()
        }

        assertEquals(21, mutants.size)
    }

    @Test
    fun testRPAVWithFile() {
        val mutants = mutableListOf<JavaMutant>()
        val builder = RPAVMapBuilder(getAnnotationInfos(File("./src/test/resources/configFiles/annotations.json")))
        builder.build()

        for (context in getListOfAnnotationContext(File(FILE1), JavaStrategy())) {
            val operator = RPAV(context, File(FILE1))
            operator.map = builder.map
            if (operator.checkContext()) mutants += operator.mutate()
        }

        assertEquals(2, mutants.size)
    }*/
}