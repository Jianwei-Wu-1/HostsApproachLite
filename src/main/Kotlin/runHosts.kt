package edu.udel

import com.intellij.openapi.project.Project
import com.intellij.psi.*
import edu.udel.phrase.convert2Phrase
import edu.udel.phrase.updateMap
import edu.udel.sematic.*
import edu.udel.util.testMethods
import opennlp.tools.postag.POSModel
import opennlp.tools.postag.POSTaggerME
import opennlp.tools.tokenize.TokenizerME
import opennlp.tools.tokenize.TokenizerModel
import util.testClasses
import org.apache.commons.lang.*
import java.io.FileInputStream
import java.util.*
import kotlin.collections.HashMap


//Todo: Add your own path here
const val nlp_path = "/Users/wujianwei/IdeaProjects/HostsApproachLite/nlp"

fun parseNameBits(name: String): Pair<String, String> {

    val inputStream = FileInputStream("$nlp_path/en-token.bin")
    val inputStreamPOS = FileInputStream("$nlp_path/en-pos-perceptron.bin")

    val model = TokenizerModel(inputStream)
    val tokenizer = TokenizerME(model)
    val modelPOS = POSModel(inputStreamPOS)
    val tagger = POSTaggerME(modelPOS)

    val nameBitsArray = name.split("(?<![0-9])(?=[A-Z])".toRegex())
    val testName = StringUtils.join(nameBitsArray, " ").orEmpty()
    val tokens = tokenizer.tokenize(testName).orEmpty()
    val tags = tagger.tag(tokens).orEmpty()

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

    val actualTests = HashSet<PsiMethod>()

    for(Class in project.testClasses()) { actualTests.addAll(Class.testMethods()) }

    for(testClass in project.testClasses()) {

        println("\n\nAnalyzing class: " + testClass.name + " ...")

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

            println("\nwords: $words")

            for (i in 0 until words.size){

                 map[i] = parseNameBits(words[i])
            }

            //Todo: connect name logic here
            println(convert2Phrase(map))

            val getClassResult =  handleGetClass(test)

            if (getClassResult != null){

                println(getClassResult.toString())
            }

            val statements = test.body?.statements?.asList().orEmpty()

            for (statement in statements){

                if (statement is PsiExpressionStatement){

                    val expression = statement.expression

                    when (expression) {

                        is PsiMethodCallExpression -> {

                            val result = handleMethodCall(expression, updateMap(map), test)

                            if (result != null){

                                println(result.toString())
                            }
                        }
                        is PsiAssignmentExpression -> {

                            val result = handleAssignmentExpression(expression)

                            if (result != null){

                                println(result.toString())
                            }
                        }
                    }
                }
                else {

                    when (statement){

                        is PsiDeclarationStatement -> {

                            val result = handleDeclarationStatement(statement)

                            if (result != null){

                                println(result.toString())
                            }
                        }
                        is PsiReturnStatement -> {

                            val result = handleReturn(statement)

                            if (result != null){

                                println(result.toString())
                            }
                        }
                        is PsiDoWhileStatement -> {

                            val result = handleDoWhile(statement)

                            if (result != null){

                                println(result)
                            }
                        }
                        is PsiForStatement -> {

                            val result = handleFor(statement)

                            if (result != null){

                                println(result)
                            }
                        }
                        is PsiIfStatement -> {

                            val result = handleIf(statement)

                            if (result != null){

                                println(result)
                            }
                        }
                        is PsiForeachStatement -> {

                            val result = handleForEach(statement)

                            if (result != null){

                                println(result)
                            }
                        }
                        is PsiWhileStatement -> {

                            val result = handleWhile(statement)

                            if (result != null){

                                println(result)
                            }
                        }
                        is PsiThrowStatement -> {

                            val result = handleThrow(statement)

                            if (result != null){

                                println(result)
                            }
                        }
                        is PsiTryStatement -> {

                            val result = handleTryCatch(statement)

                            if (result != null){

                                println(result)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun main(projects: Array<Project>) {

    projects.forEach { handleProject(it) }
}