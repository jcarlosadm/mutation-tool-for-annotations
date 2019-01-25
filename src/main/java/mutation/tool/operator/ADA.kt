package mutation.tool.operator

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.Parameter
import mutation.tool.context.Context
import mutation.tool.mutant.Mutant
import mutation.tool.mutant.MutateVisitor
import mutation.tool.util.isSameClass
import mutation.tool.util.isSameMethod
import mutation.tool.util.isSameParameter
import mutation.tool.util.isSameProp
import java.io.File

/**
 * Add Annotation
 */
class ADA(context: Context, file:File): Operator(context, file) {

    var annotation:String? = null
    var mutant = Mutant(OperatorsEnum.ADA)
    var locked = false

    override fun checkContext(): Boolean = true

    override fun mutate(): List<Mutant> {
        if (annotation == null) throw Exception("ADA with null annotation")

        val mutateVisitor = MutateVisitor(this)
        val compUnit = JavaParser.parse(file)
        val newCompUnit = compUnit.clone()

        mutateVisitor.visit(newCompUnit, null)
        mutant.compilationUnit = newCompUnit

        return listOf(mutant)
    }

    override fun visit(n: ClassOrInterfaceDeclaration?, arg: Any?) {
        super.visit(n, arg)
        if (locked || n == null || !isSameClass(context, n)) return
        insertAnnotation(n, null, null, null)
    }

    override fun visit(n: MethodDeclaration?, arg: Any?) {
        super.visit(n, arg)
        if (locked || n == null || !isSameMethod(context, n)) return
        insertAnnotation(null, n, null, null)
    }

    override fun visit(n: FieldDeclaration?, arg: Any?) {
        super.visit(n, arg)
        if (locked || n == null || !isSameProp(context, n)) return
        insertAnnotation(null, null, n, null)
    }

    override fun visit(n: Parameter?, arg: Any?) {
        super.visit(n, arg)
        if (locked || n == null || !isSameParameter(context, n)) return
        insertAnnotation(null, null, null, n)
    }

    private fun insertAnnotation(
            classOrInterfaceDeclaration: ClassOrInterfaceDeclaration?,
            methodDeclaration: MethodDeclaration?,
            fieldDeclaration: FieldDeclaration?,
            parameter: Parameter?
    ) {
        if (classOrInterfaceDeclaration != null)
            classOrInterfaceDeclaration.addAnnotation(annotation)
        else if (methodDeclaration != null)
            methodDeclaration.addAnnotation(annotation)
        else if (fieldDeclaration != null)
            fieldDeclaration.addAnnotation(annotation)
        else if (parameter != null)
            parameter.addAnnotation(annotation)
        else
            throw Exception("ADA insertion annotation error")

        locked = true
    }
}