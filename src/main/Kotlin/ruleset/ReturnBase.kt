package edu.udel.ruleset

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiExpressionStatement
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.PsiReturnStatement
import edu.udel.util.find
import edu.udel.util.testMethods

class ReturnBase(tclass: PsiClass, fraction: Int) {

    private val curClass = tclass

    private val fc = fraction

    fun checkCountingRule(): Set<String> {

        val result = HashSet<String>()

        var total = 0;

        val map = HashMap<String, Int>()

        for (test in this.curClass.testMethods()) {

            val statements = test.body?.statements?.asList().orEmpty()

            for (statement in statements){

                when (statement) {

                    is PsiReturnStatement -> {

                        val text = statement.returnValue?.type?.presentableText.orEmpty()

                        total++

                        map[text] = map.getOrDefault(text, 0) + 1
                    }
                }
            }
        }

        for (key in map.keys){

            if (map[key]!! >= (total / fc)){

                result.add(key)
            }
        }

        return result
    }
}
