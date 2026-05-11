plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("org.beryx.jlink") version "4.0.0"
    id("io.freefair.lombok") version "9.5.0"
}

val appVersion: String by project
val javafxVersion: String by project
val jpackageFormat: String by project

version = appVersion

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.21.3")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
    modularity.inferModulePath.set(true)
}

// Prevent Gradle from adding its own `--module-path` for the "run" task
tasks.named<JavaExec>("run") {
    modularity.inferModulePath.set(false)
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

    imageZip.set(layout.buildDirectory.file("connect-4.zip"))

    jpackage {
        imageName = "Connect 4"
        installerName = "connect-4"
        vendor = "Brayden Chan"
        installerOptions = if (jpackageFormat != "default") {
            listOf("--type", jpackageFormat)
        } else {
            emptyList()
        }
        val osName = System.getProperty("os.name").lowercase()
        when {
            "windows" in osName -> {
                icon = "src/main/resources/logo.ico"
                installerOptions.addAll(listOf(
                    "--win-dir-chooser",
                    "--win-help-url", "https://github.com/megabyte6/connect-4/issues",
                    "--win-menu",
                    "--win-menu-group", "Connect 4",
                    "--win-per-user-install",
                    "--win-shortcut",
                    "--win-shortcut-prompt",
                    "--win-update-url", "https://github.com/megabyte6/connect-4/releases/latest"
                ))
            }
            "linux" in osName -> {
                icon = "src/main/resources/logo.png"
                installerOptions.addAll(listOf(
                    "--linux-package-name", "connect-4",
                    "--linux-menu-group", "Connect 4",
                    "--linux-shortcut"
                ))
            }
            "mac" in osName -> {
                icon = "src/main/resources/logo.icns"
            }
            else -> {
                icon = "src/main/resources/icon.png"
            }
        }
    }
}
