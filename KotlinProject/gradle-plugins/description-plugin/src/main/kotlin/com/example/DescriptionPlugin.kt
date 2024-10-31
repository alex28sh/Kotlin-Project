package com.example
import getLatestCommitInfo
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

class DescriptionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.task("describe") {
            doLast {
                getLatestCommitInfo("alex28sh", "Kotlin-Project", "main")
                println("Number of classfiles: ${countFiles(listOf(File("composeApp/build"), File("server/build"), File("shared/src")), listOf("class"))}")
                println("Number of source files: ${countFiles(listOf(File("composeApp/src"), File("iosApp"), File("server/src"), File("shared/src")), listOf("kt", "tks"))}\n")
            }
        }
    }


    private fun countFiles(dirs: List<File>, extensions: List<String>): Int {
        return dirs.sumOf { dir -> dir.walk().filter { file -> file.isFile && extensions.any { file.name.endsWith(".$it") } }.count() }
    }
}