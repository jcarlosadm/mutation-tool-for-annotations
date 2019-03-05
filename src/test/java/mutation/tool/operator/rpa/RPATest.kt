package mutation.tool.operator.rpa

import mutation.tool.annotation.getListOfAnnotationContext
import mutation.tool.mutant.Mutant
import mutation.tool.operator.FILE1
import mutation.tool.util.json.getAnnotationInfos
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

private const val FILE2 = "./src/test/resources/fakeProject/src/main/java/TarefasController2.java"

internal class RPATest {

    @Test
    fun testRPA() {
        val mutants = mutableListOf<Mutant>()
        val contexts = getListOfAnnotationContext(File(FILE2))
        val builder = RPAMapBuilder(getAnnotationInfos(File("./src/test/resources/configFiles/annotations.json")))
        builder.build()
        val map = builder.map

        for (context in contexts) {
            val operator = RPA(context, File(FILE2))
            operator.switchMap = map
            if (operator.checkContext())
                mutants += operator.mutate()
        }

        assertEquals(2, mutants.size)
    }
}