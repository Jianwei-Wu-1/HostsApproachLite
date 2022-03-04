package edu.udel.sematic

import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression

fun handleMethodCall(
    expression: PsiMethodCallExpression,
    map: HashMap<Int, Pair<String, String>>,
    test: PsiMethod
): Pair<String, String>? {

    val rName = expression.methodExpression.referenceName.orEmpty()
    //val arguments = expression.argumentList.expressions.asList()

    if (map[0]?.second == "Verb"){

        val loweredMethodCall = rName.toLowerCase()

        if (loweredMethodCall.startsWith(map[0]?.first.orEmpty().toLowerCase())){

            return Pair(rName, "Same verb call")
        }
    }

    if (rName.toLowerCase() == test.name.toLowerCase()){

        return Pair(rName, "Same name call")
    }

    return Pair(rName, "Isolated call")
}