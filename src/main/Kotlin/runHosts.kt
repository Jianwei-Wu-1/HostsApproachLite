package edu.udel

import com.intellij.openapi.project.Project
import com.intellij.psi.*
import edu.udel.phrase.convert2Phrase
import edu.udel.phrase.updateMap
import edu.udel.ruleset.*
import edu.udel.sematic.*
import edu.udel.util.testMethods
import opennlp.tools.postag.POSModel
import opennlp.tools.postag.POSTaggerME
import opennlp.tools.tokenize.TokenizerME
import opennlp.tools.tokenize.TokenizerModel
import util.testClasses
import org.apache.commons.lang.*
import java.io.File
import java.io.FileInputStream
import java.util.*
import kotlin.collections.HashMap
import java.io.FileOutputStream
import java.io.PrintStream




//Todo: Add your own path here
const val NLP_PATH = "/Users/wujianwei/IdeaProjects/HostsApproachLite/nlp"
const val FRACTION = Int.MAX_VALUE

fun parseNameBits(name: String): Pair<String, String> {

    val inputStream = FileInputStream("$NLP_PATH/en-token.bin")
    val inputStreamPOS = FileInputStream("$NLP_PATH/en-pos-perceptron.bin")

    val model = TokenizerModel(inputStream)
    val tokenizer = TokenizerME(model)
    val modelPOS = POSModel(inputStreamPOS)
    val tagger = POSTaggerME(modelPOS)

    val nameBitsArray = name.split("(?<![0-9])(?=[A-Z])".toRegex())
    val testName = StringUtils.join(nameBitsArray, " ").orEmpty()
    val tokens = tokenizer.tokenize(testName).orEmpty()
    val tags = tagger.tag(tokens).orEmpty()

    if (tokens.isEmpty() || tags.isEmpty()){
        return Pair("", "")
    }

    val word = tokens[0].trim()
    val tag = tags[0].trim()

    inputStream.close()

    return Pair(word, tag)
}

fun parseTestName(name:  String): MutableList<String> {

    if (name.contains("_")){

        return name.split("_").toMutableList()
    }
    //Snake case
    else if (name.contains("-")){

        return name.split("-").toMutableList()
    }
    //Kebab case

    return StringUtils.splitByCharacterTypeCamelCase(name).toMutableList()
    //Camel case
}

fun handleProject(project: Project){

//    val out = File("/Users/wujianwei/IdeaProjects/HostsApproachLite/output.txt")
//    val pt = PrintStream(out)
//    println(out.absolutePath)
//    System.setOut(pt)

    val actualTests = HashSet<PsiMethod>()

    for(Class in project.testClasses()) { actualTests.addAll(Class.testMethods()) }

    for(testClass in project.testClasses()) {

        println("\n\nAnalyzing class: " + testClass.name + " ...")

        val methodCallBase = MethodCallBase(testClass, FRACTION).checkCountingRule()
        val objectBase = ObjectBase(testClass, FRACTION).checkCountingRule()
        val getDataBase = GetDataBase(testClass, FRACTION).checkCountingRule()
        val loopBase = LoopBase(testClass, FRACTION).checkCountingRule()
        val returnBase = ReturnBase(testClass, FRACTION).checkCountingRule()
        val exceptionBase = ExceptionBase(testClass, FRACTION).checkCountingRule()

        for (test in testClass.testMethods()) {

            println("\ntest: ${test.name}")

            val words = parseTestName(test.name)
            val map = HashMap<Int, Pair<String, String>>()

            if (words.size >= 1) {

                if (words[0].trim() == "test" || words[0].trim() == "Test") {
                    words.removeAt(0)
                }
                else if (words[words.size - 1].trim() == "test" || words[words.size - 1].trim() == "Test") {
                    words.removeAt(words.size - 1)
                }
            }

            //println("\nwords: $words")

            for (i in 0 until words.size){

                 map[i] = parseNameBits(words[i])
            }

            val existingName = convert2Phrase(map)

            println(existingName)

            var implementationName = ""

            val getClassResult = handleGetClass(test)

            if (getClassResult != null && getDataBase.contains(getClassResult.first)){

                implementationName += getClassResult.first
            }

            val statements = test.body?.statements?.asList().orEmpty()

            for (statement in statements){

                if (statement is PsiExpressionStatement){

                    val expression = statement.expression

                    when (expression) {

                        is PsiMethodCallExpression -> {

                            val result = handleMethodCall(expression, updateMap(map), test)

                            if (result != null && methodCallBase.contains(result.first)){

                                implementationName += if (implementationName.contains(result.first)) "" else result.first
                            }
                        }
                        is PsiAssignmentExpression -> {

                            val result = handleAssignmentExpression(expression)

                            if (result != null && objectBase.contains(result.first)){

                                implementationName += if (implementationName.contains(result.first)) "" else result.first
                            }
                        }
                    }
                }
                else {

                    when (statement){

                        is PsiDeclarationStatement -> {

                            val result = handleDeclarationStatement(statement)

                            if (result != null && objectBase.contains(result.first)){

                                implementationName += if (implementationName.contains(result.first)) "" else result.first
                            }
                        }
                        is PsiReturnStatement -> {

                            val result = handleReturn(statement)

                            if (result != null && returnBase.contains(result.first)){

                                implementationName += if (implementationName.contains(result.first)) "" else result.first
                            }
                        }
                        is PsiDoWhileStatement -> {

                            val result = handleDoWhile(statement)

                            if (result != null && loopBase.contains(result.first)){

                                implementationName += if (implementationName.contains(result.first)) "" else result.first
                            }
                        }
                        is PsiForStatement -> {

                            val result = handleFor(statement)

                            if (result != null && loopBase.contains(result.first)){

                                implementationName += if (implementationName.contains(result.first)) "" else result.first
                            }
                        }
                        is PsiIfStatement -> {

                            val result = handleIf(statement)

                            if (result != null && loopBase.contains(result.first)){

                                implementationName += if (implementationName.contains(result.first)) "" else result.first
                            }
                        }
                        is PsiForeachStatement -> {

                            val result = handleForEach(statement)

                            if (result != null && loopBase.contains(result.first)){

                                implementationName += if (implementationName.contains(result.first)) "" else result.first
                            }
                        }
                        is PsiWhileStatement -> {

                            val result = handleWhile(statement)

                            if (result != null && loopBase.contains(result.first)){

                                implementationName += if (implementationName.contains(result.first)) "" else result.first
                            }
                        }
                        is PsiThrowStatement -> {

                            val result = handleThrow(statement)

                            if (result != null && exceptionBase.contains(result.first)){

                                implementationName += if (implementationName.contains(result.first)) "" else result.first
                            }
                        }
                        is PsiTryStatement -> {

                            val result = handleTryCatch(statement)

                            if (result != null && exceptionBase.contains(result.first)){

                                implementationName += if (implementationName.contains(result.first)) "" else result.first
                            }
                        }
                    }
                }
            }

            println("Produced Name: $implementationName for ${test.name}")
        }
    }
}

fun main(projects: Array<Project>) {

    projects.forEach { handleProject(it) }
}