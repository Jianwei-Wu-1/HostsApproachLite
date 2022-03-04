package edu.udel.ruleset

import com.intellij.psi.*
import edu.udel.util.testMethods

class LoopBase(tclass: PsiClass, fraction: Int) {

    private val curClass = tclass

    private val fc = fraction

    fun checkCountingRule(): Set<String> {

        val result = HashSet<String>()

        var total = 0;

        val map = HashMap<String, Int>()

        for (test in this.curClass.testMethods()) {

            val statements = test.body?.statements?.asList().orEmpty()

            for (statement in statements) {

                when (statement) {

                    is PsiDoWhileStatement -> {

                        val condition = statement.condition.toString().split(":")[1]

                        map[condition] = map.getOrDefault(condition, 0) + 1

                        total++
                    }
                    is PsiForStatement -> {

                        val condition = statement.condition.toString().split(":")[1]

                        map[condition] = map.getOrDefault(condition, 0) + 1

                        total++;
                    }
                    is PsiForeachStatement -> {

                        val condition = statement.iteratedValue.toString().split(":")[1] + " under " + statement.iterationParameter.name

                        map[condition] = map.getOrDefault(condition, 0) + 1

                        total++;
                    }
                    is PsiWhileStatement -> {

                        val condition = statement.condition.toString().split(":")[1]

                        map[condition] = map.getOrDefault(condition, 0) + 1

                        total++;
                    }
                    is PsiIfStatement -> {

                        val condition = statement.condition.toString().split(":")[1]

                        map[condition] = map.getOrDefault(condition, 0) + 1

                        total++;
                    }
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
