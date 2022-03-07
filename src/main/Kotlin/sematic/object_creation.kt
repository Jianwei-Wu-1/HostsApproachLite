package edu.udel.sematic

import com.intellij.psi.*

fun handleAssignmentExpression(
    statement: PsiAssignmentExpression
): Pair<String, String>? {

    val variable = "${statement.lExpression.toString().split(":")[1]}+${statement.rExpression.toString().split(":")[1]}"

    return Pair(variable, "Assignment")
}

fun handleDeclarationStatement(
    statement: PsiDeclarationStatement
): Pair<String, String>? {

    val declaredObject = statement.declaredElements.toList()

    if (declaredObject.isNotEmpty() && (declaredObject[0] is PsiLocalVariable)){

        val variable = declaredObject[0] as PsiLocalVariable

        return Pair("${variable.type.presentableText}+${variable.name}", "Declaration")
    }

    return null
}

