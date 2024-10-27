package com.example
import getLatestCommitInfo
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

class DescriptionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.task("describe") {
            doLast {
                getLatestCommitInfo("alex28sh", "Terms-Reduction", "master")
                println("Number of classfiles: ${getClassFiles(project)}")
                println("Number of source files: ${countSourceFiles(listOf(File("composeApp/src"), File("iosApp"), File("server/src"), File("shared/src")), listOf("kt", "tks"))}\n")
            }
        }
    }

    private fun getClassFiles(project: Project): Int {
        val classesDir = project.buildDir
        return classesDir.walk().filter { it.isFile && it.extension == "class" }.count()
    }

    private fun countSourceFiles(dirs: List<File>, extensions: List<String>): Int {
        var totalSourceFiles = 0

        dirs.map {it.listFiles().asIterable()}.flatMap {it}.forEach { file ->
            if (file.isDirectory) {
                totalSourceFiles += countSourceFiles(listOf(file), extensions)
            } else if (file.isFile && extensions.any { file.name.endsWith(".$it") }) {
                totalSourceFiles++
            }
        }

        return totalSourceFiles
    }
}