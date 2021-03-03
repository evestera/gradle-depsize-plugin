package com.github.evestera.depsize

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.artifacts.ResolvedDependency
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import kotlin.collections.ArrayDeque

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
    var dependency: String = allDependencies

    @TaskAction
    fun printDependencySizes() {
        val config: Configuration = project.configurations.getByName(configuration)

        var out = "\nConfiguration name: \"${config.name}\"\n"

        var artifacts: Iterable<ResolvedArtifact> = config.resolvedConfiguration.resolvedArtifacts
        if (dependency != allDependencies) {
            val newRoot = searchDependencyTree(config.resolvedConfiguration.firstLevelModuleDependencies) {
                it.moduleName == dependency
            }
            if (newRoot == null) {
                out += "Unable to find dependency with name $dependency"
                println(out)
                return
            }
            artifacts = newRoot.allModuleArtifacts
            out += "Showing only $dependency (with children)\n\n"
        }

        val size = artifacts.map { it.file.length() / (1024.0 * 1024.0) }.sum()

        val padding = maxOf(artifacts.maxOf { it.file.name.length }, 25)

        if (size > 0) {
            out += "Total dependencies size:".padEnd(padding)
            out += "%,10.2f MB\n\n".format(size)

            for (artifact in artifacts.sortedBy { -it.file.length() }) {
                out += artifact.file.name.padEnd(padding)
                out += "%,10.2f KB\n".format(artifact.file.length() / 1024.0)
            }
        } else {
            out += "No dependencies found"
        }
        println(out)
    }

    private fun searchDependencyTree(
        dependencies: Set<ResolvedDependency>,
        predicate: (ResolvedDependency) -> Boolean
    ): ResolvedDependency? {
        val seen = mutableSetOf<String>()
        val queue = ArrayDeque(dependencies)

        while (!queue.isEmpty()) {
            val dependency = queue.removeFirst()
            if (predicate(dependency)) {
                return dependency
            }
            seen.add(dependency.moduleName)
            dependency.children.forEach {
                if (!seen.contains(it.moduleName)) {
                    queue.addLast(it)
                }
            }
        }

        return null
    }
}
