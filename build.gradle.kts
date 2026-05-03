plugins {
    // Apply the java plugin for better toolchain detection support.
    id("java")
    // Apply the application plugin to add support for building a CLI application in Java.
    id("application")
    // Apply javafxplugin for JavaFX support.
    id("org.openjfx.javafxplugin") version "0.1.0"
    // Apply jlink for building the app.
    id("org.beryx.jlink") version "4.0.0"
    // Apply lombok.
    id("io.freefair.lombok") version "9.5.0"
}

// Project/version variables from gradle.properties
val appVersion: String by project
val javafxVersion: String by project
val jpackageFormat: String by project

version = appVersion

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.21.3")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

// Suppress module warnings such as "module name component should avoid
// terminal digits". This project is called "Connect 4", and "4" is a terminal
// digit, but it's not worth changing the module name to avoid it.
tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-Xlint:-module")
}

application {
    mainModule.set("com.megabyte6.connect4")
    mainClass.set("com.megabyte6.connect4.App")
}

javafx {
    version = javafxVersion
    modules = listOf("javafx.controls", "javafx.fxml")
}

jlink {
    options.set(listOf(
        "--strip-debug",
        "--no-header-files",
        "--no-man-pages"
    ))
    launcher {
        noConsole = true
    }

    imageZip.set(layout.buildDirectory.file("connect4.zip"))

    jpackage {
        imageName = "Connect 4"
        installerName = "connect4-installer"
        vendor = "Brayden Chan"

        installerOptions = if (jpackageFormat != "default") {
            listOf("--type", jpackageFormat)
        } else {
            emptyList()
        }

        val osName = System.getProperty("os.name").lowercase()
        when {
            "windows" in osName -> {
                icon = "src/main/resources/icon.ico"
                installerOptions.addAll(listOf(
                    "--win-dir-chooser",
                    "--win-menu",
                    "--win-menu-group", "Connect 4",
                    "--win-per-user-install",
                    "--win-shortcut",
                    "--win-shortcut-prompt",
                    "--win-update-url", "https://github.com/megabyte6/connect-4/releases/latest"
                ))
            }
            "linux" in osName -> {
                icon = "src/main/resources/icon.png"
                installerOptions.addAll(listOf(
                    "--linux-package-name", "Connect 4",
                    "--linux-menu-group", "Connect 4",
                    "--linux-shortcut"
                ))
            }
            "mac" in osName -> {
                icon = "src/main/resources/icon.icns"
            }
            else -> {
                icon = "src/main/resources/icon.png"
            }
        }
    }
}
