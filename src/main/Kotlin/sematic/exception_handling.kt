package edu.udel.sematic

import com.intellij.psi.PsiThrowStatement
import com.intellij.psi.PsiTryStatement

fun handleTryCatch(
    statement: PsiTryStatement
): Pair<String, String>? {

    val catchParameters = statement.catchBlockParameters.toList()

    if (catchParameters.isNotEmpty()){

        return Pair(catchParameters[0].name.orEmpty(), "exception")
    }

    return null
}

fun handleThrow(
    statement: PsiThrowStatement
): Pair<String, String>? {

    val exception = statement.exception?.type?.presentableText.orEmpty()

    if (exception != ""){

        return Pair(exception, "exception")
    }

    return null
}