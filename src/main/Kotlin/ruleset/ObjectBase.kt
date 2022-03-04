package edu.udel.ruleset

import com.intellij.psi.*
import edu.udel.util.testMethods

class ObjectBase(tclass: PsiClass, fraction: Int){

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

                            is PsiAssignmentExpression -> {

                                val variable = "${expression.lExpression.toString().split(":")[1]}+${expression.rExpression.toString().split(":")[1]}"

                                total++;

                                map[variable] = map.getOrDefault(variable, 0) + 1
                            }
                        }
                    }
                    is PsiDeclarationStatement -> {

                        val declaredObject = statement.declaredElements.toList()

                        if (declaredObject.isNotEmpty()){

                            val variable = declaredObject[0] as PsiLocalVariable

                            val value = "{variable.type.presentableText}+${variable.name}"

                            total++;

                            map[value] = map.getOrDefault(value, 0) + 1
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