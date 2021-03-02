package com.github.evestera.depsize

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

open class DepSizeTask : DefaultTask() {
    init {
        group = "help"
        description = "Calculate size of dependencies"
    }

    @field:Option(description = "Which configuration to use (default: runtimeClasspath)")
    @get:Input
    var configuration: String = "runtimeClasspath"

    private val allDependencies = "*all-dependencies*"
    @field:Option(description = "Which dependency (with children) to calculate size of (default: all dependencies)")
    @get:Input
    var dependency: String? = allDependencies

    @TaskAction
    fun printDependencySizes() {
        val config: Configuration = project.configurations.getByName(configuration)

        var out = "\nConfiguration name: \"${config.name}\"\n"

        var fileCollection: FileCollection = config
        if (dependency != allDependencies) {
            fileCollection = config.fileCollection { it.name == dependency }
            out += "Showing only $dependency (with children)\n\n"
        }

        val size = fileCollection.map { it.length() / (1024.0 * 1024.0) }.sum()

        if (size > 0) {
            out += "Total dependencies size:".padEnd(65)
            out += "%,10.2f MB\n\n".format(size)

            for (dependencyFile in fileCollection.sortedBy { -it.length() }) {
                out += dependencyFile.name.padEnd(65)
                out += "%,10.2f KB\n".format(dependencyFile.length() / 1024.0)
            }
        } else {
            out += "No dependencies found"
        }
        println(out)
    }
}
