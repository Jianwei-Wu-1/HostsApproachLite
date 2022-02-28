package edu.udel.sematic

import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import edu.udel.util.find

fun handleGetClass(
    test: PsiMethod
): Pair<String, String>? {

    val methodCalls = test.body?.find<PsiMethodCallExpression>().orEmpty()

    for (mc in methodCalls){

        if (mc.methodExpression.referenceName.orEmpty().startsWith("getClass")){

            return Pair(mc.methodExpression.referenceName.orEmpty(), "DataFlow")
        }
    }

    return null
}