package mutation.tool.util.json

import mutation.tool.context.InsertionPoint
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

internal class AnnotationInfosBuilderKtTest {

    private val annotationNames = arrayOf("@org.springframework.web.bind.annotation.RequestMapping",
            "@org.springframework.beans.factory.annotation.Autowired",
            "@org.springframework.beans.factory.annotation.Qualifier")

    @Test
    fun testGetAnnotationInfos() {
        val infos = getAnnotationInfos(File("./src/test/resources/configFiles/annotations.json"))

        assertEquals(3, infos.size)

        for (annotationInfo in infos) {
            assertTrue(annotationNames.contains(annotationInfo.name))

            when(annotationInfo.name) {
                "@org.springframework.web.bind.annotation.RequestMapping" -> {
                    assertEquals(0, annotationInfo.replaceableBy.size)
                    assertEquals(2, annotationInfo.targets.size)
                    assertEquals(3, annotationInfo.attributes.size)

                    for (target in annotationInfo.targets)
                        assertTrue(arrayOf(InsertionPoint.CLASS, InsertionPoint.METHOD).contains(target))

                    for (attribute in annotationInfo.attributes) {
                        assertTrue(arrayOf("value", "method", "headers").contains(attribute.name))
                        assertTrue(arrayOf("java.lang.String[]",
                                "org.springframework.web.bind.annotation.RequestMethod[]",
                                "java.lang.String[]").contains(attribute.type))
                        assertTrue(arrayOf("\"/ex/foo\"", "org.springframework.web.bind.annotation.RequestMethod.POST",
                                "\"content-type=text/*\"").contains(attribute.validValues[0]))

                        if (attribute.name == "value") {
                            assertTrue(attribute.default)
                        } else {
                            assertFalse(attribute.default)
                        }
                    }
                }

                "@org.springframework.beans.factory.annotation.Autowired" -> {
                    assertEquals(1, annotationInfo.replaceableBy.size)
                    assertEquals(2, annotationInfo.targets.size)
                    assertEquals(0, annotationInfo.attributes.size)

                    for (target in annotationInfo.targets)
                        assertTrue(arrayOf(InsertionPoint.PROPERTY, InsertionPoint.METHOD).contains(target))

                    assertEquals("@org.springframework.beans.factory.annotation.Qualifier",
                            annotationInfo.replaceableBy[0])
                }

                "@org.springframework.beans.factory.annotation.Qualifier" -> {
                    assertEquals(1, annotationInfo.replaceableBy.size)
                    assertEquals(2, annotationInfo.targets.size)
                    assertEquals(0, annotationInfo.attributes.size)

                    for (target in annotationInfo.targets)
                        assertTrue(arrayOf(InsertionPoint.PROPERTY, InsertionPoint.PARAMETER).contains(target))

                    assertEquals("@org.springframework.beans.factory.annotation.Autowired",
                            annotationInfo.replaceableBy[0])
                }
            }
        }
    }
}