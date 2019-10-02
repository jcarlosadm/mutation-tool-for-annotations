package mutation.tool.operator.rpa

import mutation.tool.annotation.getListOfAnnotationContext
import mutation.tool.annotation.visitor.JavaStrategy
import mutation.tool.mutant.JavaMutant
import mutation.tool.util.json.getAnnotationInfos
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

private const val FILE2 = "./src/test/resources/fakeProject/src/main/java/TarefasController2.java"

internal class RPATest {

    @Test
    fun testRPA() {
        val mutants = mutableListOf<JavaMutant>()
        val contexts = getListOfAnnotationContext(File(FILE2), JavaStrategy())
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