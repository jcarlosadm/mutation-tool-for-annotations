package mutation.tool.annotation

import com.github.javaparser.ast.expr.AnnotationExpr
import mutation.tool.annotation.visitor.JavaStrategy
import mutation.tool.context.*
import mutation.tool.context.adapter.AnnotationAdapter
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
        val list = getListOfAnnotationContext(file, JavaStrategy())

        assertEquals(19, list.size)

        var classCount = 0
        var propertyCount = 0
        var methodCount = 0
        var parameterCount = 0

        for (context in list) {
            val annotations:List<AnnotationAdapter> = context.annotations
            when(context.getInsertionPoint()) {
                InsertionPoint.CLASS -> classCount++
                InsertionPoint.METHOD -> methodCount++
                InsertionPoint.PROPERTY -> propertyCount++
                InsertionPoint.PARAMETER -> parameterCount++
            }

            for (annotation in annotations){
                MatcherAssert.assertThat(annotation.name, AnyOf.
                        anyOf(CoreMatchers.containsString("Controller"),
                                CoreMatchers.containsString("Qualifier"),
                                CoreMatchers.containsString("Autowired"),
                                CoreMatchers.containsString("RequestMapping"),
                                CoreMatchers.containsString("Valid")))
            }
        }

        assertEquals(1, classCount)
        assertEquals(2, propertyCount)
        assertEquals(7, methodCount)
        assertEquals(9, parameterCount)
    }
}