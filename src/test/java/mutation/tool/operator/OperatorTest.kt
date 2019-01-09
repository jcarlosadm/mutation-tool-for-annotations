package mutation.tool.operator

import mutation.tool.annotation.getListOfAnnotationContext
import mutation.tool.mutant.Mutant
import mutation.tool.util.MutationToolConfig
import mutation.tool.util.getAnnotations
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

const val FILE1 = "./src/test/resources/fakeProject/src/main/java/TarefasController.java"

internal class OperatorTest {

    @Test
    fun testGetValidOperators() {
        val config = MutationToolConfig(File(""), File(""))
        config.operators += listOf(OperatorsEnum.RMA, OperatorsEnum.RMAT)
        val validOperators = getValidOperators(getListOfAnnotationContext(File(FILE1)), File(FILE1), config)

        assertEquals(19, validOperators.size)
    }

    @Test
    fun testRMA() {
        val mutants = mutableListOf<Mutant>()

        for (context in getListOfAnnotationContext(File(FILE1))) {
            val operator = RMA(context, File(FILE1))

            val annotations = getAnnotations(context)
            if (annotations.isNotEmpty()) {
                assertTrue(operator.checkContext())

                mutants.addAll(operator.mutate())
            } else {
                assertFalse(operator.checkContext())
            }
        }
        assertEquals(11, mutants.size)
    }

    @Test
    fun testRMAT() {
        val mutants = mutableListOf<Mutant>()
        for (context in getListOfAnnotationContext(File(FILE1))) {
            val operator = RMAT(context, File(FILE1))

            var count = 0
            for (annotation in getAnnotations(context)) {
                if (annotation.toString().contains(Regex("\\((.*?)\\)"))) count++
            }

            if (count > 0) {
                assertTrue(operator.checkContext())
                mutants.addAll(operator.mutate())
            } else {
                assertFalse(operator.checkContext())
            }
        }

        assertEquals(10, mutants.size)
    }

    @Test
    fun testADA() {
        val mutants = mutableListOf<Mutant>()
        for (context in getListOfAnnotationContext(File(FILE1))) {
            val operator = ADA(context, File(FILE1))
            mutants.addAll(operator.mutate())
        }

        // TODO change later
        assertEquals(0, mutants.size)
    }
}