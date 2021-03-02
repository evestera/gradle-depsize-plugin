plugins {
    `java-gradle-plugin`
    id("org.jetbrains.kotlin.jvm") version "1.4.20"
    `maven-publish`
    id("com.gradle.plugin-publish") version "0.13.0"
}

group = "com.github.evestera"
version = "0.2.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

gradlePlugin {
    plugins {
        create("depsize") {
            id = "com.github.evestera.depsize"
            implementationClass = "com.github.evestera.depsize.DepSizePlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/evestera/gradle-depsize-plugin"
    vcsUrl = "https://github.com/evestera/gradle-depsize-plugin"
    tags = listOf("dependency", "size", "task")
    description = """
        Plugin that adds a task "depsize" which calculates and shows dependency sizes.
        For further documentation, see github repository.
        """.trimIndent()

    (plugins) {
        "depsize" {
            displayName = "Dependency Size Listing plugin"
        }
    }
}

val functionalTestSourceSet = sourceSets.create("functionalTest")

gradlePlugin.testSourceSets(functionalTestSourceSet)
configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])

val functionalTest by tasks.registering(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
}

tasks.check {
    dependsOn(functionalTest)
}
