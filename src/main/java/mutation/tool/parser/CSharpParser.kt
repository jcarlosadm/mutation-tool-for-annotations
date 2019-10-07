package mutation.tool.parser

import mutation.tool.context.InsertionPoint
import mutation.tool.util.xml.getAllTagNodes
import mutation.tool.util.xml.getTagNode
import org.w3c.dom.Node
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

private fun printNode(node:Node) {

    /*//println(node.nodeName)
    if (node.nodeName == "attribute") {
        val insertionPoint = findParent(node)
        println(node.textContent + " >>> " + insertionPoint)
        // find target (class, function, decl_stmt, parameter)
    }*/

    /*if (node.nodeName == "class") {
        println(getTagValue(node, "name", true))
    }*/

    if (node.nodeName == "class") {
        println(getTagNode(node, "name", false)?.attributes?.getNamedItem("pos:column")?.textContent)
    }

    /*if (node.nodeName == "parameter") {
        val list = getAllTagNodes(node, "attribute", listOf("decl_stmt", "function", "class"))
        for (item in list) {
            println(item.textContent)
        }
        if(list.isNotEmpty()) println("-----------------")
    }*/

    for (i in 0 until node.childNodes.length) {
        printNode(node.childNodes.item(i))
    }
}

fun findParent(node: Node): InsertionPoint? {
    var parent = node.parentNode
    val map = mapOf("class" to InsertionPoint.CLASS, "function" to InsertionPoint.METHOD,
            "decl_stmt" to InsertionPoint.PROPERTY, "parameter" to InsertionPoint.PARAMETER)
    while (parent != null && !map.containsKey(parent.nodeName)) {
        parent = parent.parentNode
    }

    if (parent == null) return null

    return map.get(parent.nodeName)
}

class CSharpParser(private val file:File) {

    fun processAnnotations(insertionPoints: List<InsertionPoint>, processor:AnnotationProcessor?){
        /*val destPath = file.absolutePath + ".xml"
        val process = Runtime.getRuntime().exec("srcml " + file.absolutePath + " -o " + destPath +
                " --position")
        process.waitFor()

        val xmlFile = File(destPath)*/

        val process = Runtime.getRuntime().exec("srcml " + file.absolutePath +
                " --position")
        process.waitFor()

        val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val doc = builder.parse(process.inputStream)
        for (i in 0 until doc.childNodes.length) {
            printNode(doc.childNodes.item(i))
        }

        /*val reader = BufferedReader(FileReader(xmlFile))
        for (line in reader.lines()) {
            println(line)
        }*/

    }
}

fun main() {
    val a = CSharpParser(File("src/main/resources/test.cs"))
    a.processAnnotations(listOf(InsertionPoint.CLASS), null)
}