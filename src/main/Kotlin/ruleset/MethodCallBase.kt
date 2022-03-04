package edu.udel.ruleset

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiExpressionStatement
import com.intellij.psi.PsiMethodCallExpression
import edu.udel.util.testMethods

class MethodCallBase(tclass: PsiClass, fraction: Int){

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

                    is PsiExpressionStatement -> {

                        val expression = statement.expression

                        when (expression) {

                            is PsiMethodCallExpression -> {

                                val mc = expression.methodExpression.referenceName.orEmpty()

                                total++;

                                map[mc] = map.getOrDefault(mc, 0) + 1
                            }
                        }
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