package edu.udel

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiMethod
import edu.udel.util.testMethods
import util.testClasses
import java.util.HashSet

fun handleProject(project: Project){

    val actualTests = HashSet<PsiMethod>()

    for(Class in project.testClasses()) { actualTests.addAll(Class.testMethods()) }

    //Count tests

    var counter = 0

    for(testClass in project.testClasses()) {

        println("\n\nAnalyzing class: " + testClass.name + " ...")


    }
}

fun main(projects: Array<Project>) {
    projects.forEach { handleProject(it) }
}