package com.github.evestera.depsize

import java.io.File
import org.gradle.testkit.runner.GradleRunner
import kotlin.test.Test
import kotlin.test.assertTrue

class DepSizePluginFunctionalTest {
    @Test
    fun `can run task (no dependencies)`() {
        // Setup the test build
        val projectDir = File("build/functionalTest")
        projectDir.mkdirs()
        projectDir.resolve("settings.gradle").writeText("")
        projectDir.resolve("build.gradle").writeText(
            """
            plugins {
                id 'application'
                id 'com.github.evestera.depsize'
            }
        """
        )

        // Run the build
        val runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("depsize", "--stacktrace")
        runner.withProjectDir(projectDir)
        val result = runner.build();

        // Verify the result
        assertTrue(result.output.contains("No dependencies found"))
    }

    @Test
    fun `can run task (with dependencies)`() {
        // Setup the test build
        val projectDir = File("build/functionalTest")
        projectDir.mkdirs()
        projectDir.resolve("settings.gradle").writeText("")
        projectDir.resolve("build.gradle").writeText(
            """
            plugins {
                id 'application'
                id 'com.github.evestera.depsize'
            }
            
            repositories {
                mavenCentral()
            }
            
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.20")
            }
        """
        )

        // Run the build
        val runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("depsize", "--stacktrace")
        runner.withProjectDir(projectDir)
        val result = runner.build();

        // Verify the result
        assertTrue(result.output.contains("kotlin-stdlib-jdk8"))
    }
}
