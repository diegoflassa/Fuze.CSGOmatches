package dev.diegoflassa.buildLogic

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

object Configuration {
    const val APP_PREFIX = "fcsgo"
    const val DIEGOFLASSA_ID = "dev.diegoflassa"
    const val APPLICATION_ID = "$DIEGOFLASSA_ID.fuzecsgomatches"
    const val MINIMUM_SDK = 28
    const val COMPILE_SDK = 36
    const val TARGET_SDK = 36
    const val BUILD_TOOLS_VERSION = "36.0.0"

    private var buildCountValue: Int = -1
    private var initialized = false

    /**
     * Increment the build count by reading from version.properties, incrementing the count,
     * and writing it back. This method MUST be called from a Gradle build script
     * (e.g., app/build.gradle.kts or root build.gradle.kts) during its configuration phase.
     *
     * @param actualRootProjectDir The root directory of the main project (e.g., project.rootDir).
     */
    fun incrementBuildCount(actualRootProjectDir: File, isAssembleTask: Boolean = false) {
        println("Configuration:: initializeBuildCount CALLED with rootDir: ${actualRootProjectDir.absolutePath}")
        val versionPropsFile = File(actualRootProjectDir, "version.properties")
        val versionProps = Properties()
        println("Configuration:: Using version.properties file: ${versionPropsFile.absolutePath}")

        val currentCodeFromFile: Int
        if (versionPropsFile.exists()) {
            FileInputStream(versionPropsFile).use { fis -> versionProps.load(fis) }
            currentCodeFromFile = (versionProps["VERSION_CODE"] ?: "0").toString().toInt()
            println("Configuration:: Read from ${versionPropsFile.name}: $currentCodeFromFile")
        } else {
            versionProps["VERSION_CODE"] = "0"
            currentCodeFromFile = 0
            println("Configuration:: ${versionPropsFile.name} not found. Initial count from file: 0.")
        }
        if (isAssembleTask) {
            println("Configuration:: Assembly task. Incrementing build count from $currentCodeFromFile to ${currentCodeFromFile + 1}.")
            buildCountValue = currentCodeFromFile + 1
            versionProps["VERSION_CODE"] = buildCountValue.toString()

            FileOutputStream(versionPropsFile).use { fos ->
                versionProps.store(fos, "Build version counter")
            }
            println("Configuration:: New VERSION_CODE written to ${versionPropsFile.name}: $buildCountValue")
        } else {
            println("Configuration:: Not an assembly task. Skipping.")
        }
        initialized = true
    }

    val VERSION_CODE: Int
        get() {
            if (!initialized) {
                println("Configuration:: WARNING: VERSION_CODE accessed before initializeBuildCount was called! Returning a default.")
            }
            return (1015 * 1000) + buildCountValue
        }

    val VERSION_NAME: String
        get() {
            if (!initialized) {
                println("Configuration:: WARNING: VERSION_NAME accessed before initializeBuildCount was called! Returning a default.")
            }
            return "1.0.15-build_$buildCountValue"
        }

    fun buildAppName(name: String, versionName: String): String {
        val builtName = "${APP_PREFIX}-app-${name}-${versionName}"
        return builtName
    }
}
