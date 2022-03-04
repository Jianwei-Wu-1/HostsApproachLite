package edu.udel.ruleset

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiReturnStatement
import com.intellij.psi.PsiThrowStatement
import com.intellij.psi.PsiTryStatement
import edu.udel.util.testMethods

class ExceptionBase(tclass: PsiClass, fraction: Int) {

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

                    is PsiTryStatement -> {

                        val catchParameters = statement.catchBlockParameters.toList()

                        if (catchParameters.isNotEmpty()) {

                            val name = catchParameters[0].name.orEmpty()

                            total++

                            map[name] = map.getOrDefault(name, 0) + 1
                        }
                    }
                    is PsiThrowStatement -> {

                        val exception = statement.exception?.type?.presentableText.orEmpty()

                        total++

                        map[exception] = map.getOrDefault(exception, 0) + 1
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