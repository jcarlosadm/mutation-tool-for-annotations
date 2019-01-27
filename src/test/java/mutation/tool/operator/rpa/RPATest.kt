package mutation.tool.operator.rpa

import mutation.tool.annotation.getListOfAnnotationContext
import mutation.tool.mutant.Mutant
import mutation.tool.operator.FILE1
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class RPATest {

    @Test
    fun testRPA() {
        val mutants = mutableListOf<Mutant>()
        val contexts = getListOfAnnotationContext(File(FILE1))
        val map = mapOf(
                "Autowired" to listOf("@a", "@b(1)", "@c(name=\"aaa\")"),
                "Qualifier" to listOf("@rrrr")
        )
        val importMap = mapOf(
                "a" to "mutationtool.test.annotation.a",
                "b" to "mutationtool.test.annotation.b",
                "c" to "mutationtool.test.annotation.c",
                "rrrr" to "mutationtool.test.annotation.rrrr"
        )

        for (context in contexts) {
            val operator = RPA(context, File(FILE1))
            operator.switchMap = map
            operator.importMap = importMap
            if (operator.checkContext())
                mutants += operator.mutate()
        }

        assertEquals(4, mutants.size)
    }
}