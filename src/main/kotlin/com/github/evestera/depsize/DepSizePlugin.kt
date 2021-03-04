package com.github.evestera.depsize

import org.gradle.api.Plugin
import org.gradle.api.Project

class DepSizePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("depsize", DepSizeTask::class.java)
    }
}
