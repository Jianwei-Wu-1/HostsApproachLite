package edu.udel.sematic

import com.intellij.psi.PsiReturnStatement

fun handleReturn(
    statement: PsiReturnStatement
): Pair<String, String>? {

    val returnType = statement.returnValue?.type

    if (returnType != null)
        return Pair(statement.returnValue?.text.orEmpty(), returnType.presentableText)

    return null
}
