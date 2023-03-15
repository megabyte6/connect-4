# connect-4

## About

`connect-4` is a fun clone of the board game Connect 4 written in Java.

---

## How to run it.

### Option 1: I just wanna play it.

- Installer instructions:
    - Head to [the latest release](https://github.com/megabyte6/connect-4/releases/latest).
    - Download the latest installer for your system.
    - Run the installer.
    - Have fun! 👍
- Portable instructions:
    - Head to [the latest release](https://github.com/megabyte6/connect-4/releases/latest).
    - Download the correct `.zip` for your system.
    - Extract the files.
    - Navigate to the `bin` folder.
    - Run `connect-4` (or `connect-4.bat` if you're on Windows).

Note:

- The installers are currently only available for Windows.
- If you're on Windows and wish to use a portable build, you can use `connect4-win-x64-x.x.zip` (which ships with a JVM) or `connect4-win-x64-portable-x.x.zip` (which has a windows executable. This option is recommended).

---

### Option 2: I don't want to install stuff.

Note: This project should be built with JDK 17 but the build script will install it automatically if you don't already have a compatible JDK.

1. Download this repository with the green `Code` button.
1. Navigate to the project folder. You should see a `build.gradle` file.
1. Open a terminal in this directory and run `./gradlew run` (or `gradlew.bat run` if you're on Windows Command Prompt) to run the game.

---

## Building this project:

Note: This project should be built with JDK 17 but the build script will install it automatically if you don't already have a compatible JDK.

1. Download this repository with the green `Code` button.
1. Run the build command using one of the following options:
    - Build portable images (this one is easier):
        1. Open `gradle.properties` and change the `jlinkTargetPlatform` property to match the OS and architecture you wish to build for.
        1. Run `./gradlew jlinkZip`
        1. Check the `build` folder for the `.zip` image.
        1. Play it by extracting the zip and running the `connect-4` in the `bin` folder
    - Build installers & executables:
        1. Check [Oracle's website](https://docs.oracle.com/en/java/javase/14/jpackage/packaging-overview.html#GUID-786E15C0-2CE7-4BDF-9B2F-AC1C57249134:~:text=Java%20Runtime%20Requirements-,Packaging%20Pre%2DReqs,WiX%203.0%20or%20later%20is%20required.,-Application%20Preparation) for info on your system's prerequisites.
        1. Open `gradle.properties` and change the `jpackageTargetPlatform` property to match your operating system and architecture. This must match your current operating system and hardware.
        1. Run `./gradlew jpackage`
        1. Check `build/jpackage` for the installer(s) and `build/jpackage/Wordle` for the executable(s).

---

## License

This project uses the [MIT License](https://opensource.org/licenses/MIT).

---

## Thanks

This project was possible due to the following libraries:
- [JavaFX](https://openjfx.io)
- [badass-jlink-plugin](https://github.com/beryx/badass-jlink-plugin)
- [jackson](https://github.com/FasterXML/jackson)