package edu.udel.ruleset

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiExpressionStatement
import com.intellij.psi.PsiMethodCallExpression
import edu.udel.util.find
import edu.udel.util.testMethods

class GetDataBase(tclass: PsiClass, fraction: Int) {

    private val curClass = tclass

    private val fc = fraction

    fun checkCountingRule(): Set<String> {

        val result = HashSet<String>()

        var total = 0;

        val map = HashMap<String, Int>()

        for (test in this.curClass.testMethods()) {

            val methodCalls = test.body?.find<PsiMethodCallExpression>().orEmpty()

            for (mc in methodCalls){

                if (mc.methodExpression.referenceName.orEmpty().startsWith("getClass")){

                    val text = mc.methodExpression.referenceName.orEmpty()

                    total++

                    map[text] = map.getOrDefault(text, 0) + 1
                }
            }
        }

        for (key in map.keys) {

            if (map[key]!! >= (total / fc)) {

                result.add(key)
            }
        }

        return result
    }
}
