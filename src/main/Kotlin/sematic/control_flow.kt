package edu.udel.sematic

import com.intellij.psi.*

fun handleFor(
    statement: PsiForStatement
): Pair<String, String>? {

    val condition = statement.condition.toString().split(":")[1]

    if (condition.isNotEmpty()){
        return Pair(condition, "loop")
    }

    return null
}

fun handleDoWhile(
    statement: PsiDoWhileStatement
): Pair<String, String>? {

    val condition = statement.condition.toString().split(":")[1]

    if (condition.isNotEmpty()){
        return Pair(condition, "loop")
    }

    return null
}

fun handleWhile(
    statement: PsiWhileStatement
): Pair<String, String>? {

    val condition = statement.condition.toString().split(":")[1]

    if (condition.isNotEmpty()){
        return Pair(condition, "loop")
    }

    return null
}

fun handleForEach(
    statement: PsiForeachStatement
): Pair<String, String>? {

    val condition = statement.iteratedValue.toString().split(":")[1] + " under " + statement.iterationParameter.name

    if (condition.isNotEmpty()){
        return Pair(condition, "loop")
    }

    return null
}

fun handleIf(
    statement: PsiIfStatement
): Pair<String, String>? {

    val condition = statement.condition.toString().split(":")[1]

    if (condition.isNotEmpty()){
        return Pair(condition, "if")
    }

    return null
}