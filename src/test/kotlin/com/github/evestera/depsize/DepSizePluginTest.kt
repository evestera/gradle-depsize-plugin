package com.github.evestera.depsize

import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull

class DepSizePluginTest {
    @Test
    fun `plugin registers task`() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("com.github.evestera.depsize")

        // Verify the result
        assertNotNull(project.tasks.findByName("depsize"))
    }
}
