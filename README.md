# connect-4

## About

`connect-4` is a fun clone of the board game Connect 4 written in Java.

## How to run it.

- Prerequisites:
    - JDK 17
        - [Adoptium](https://adoptium.net/temurin/releases)
        - [Adoptium GitHub](https://github.com/adoptium/temurin17-binaries/releases)
    - A bit of command line knowledge.

1. Download this repository with the green `Code` button.
1. Navigate to the project folder. You should see a `build.gradle` file.
1. Open a terminal and run `./gradlew run` (or `gradlew.bat run` if you're on Windows Command Prompt) to run the game.

## Building the project:

- Prerequisites:
    - JDK 17
        - [Adoptium](https://adoptium.net/temurin/releases)
        - [Adoptium GitHub](https://github.com/adoptium/temurin17-binaries/releases)
    - A bit of command line knowledge.

1. Download this repository with the green `Code` button.
1. Run the build command:
    - Build portable images (this one is easier):
        1. Open `build.gradle` and change the `jlinkTargetPlatform` property in the `ext` block to match your OS and architecture.
        1. Run `./gradlew jlinkZip`
        1. Check `build` for the `.zip` image.
    - Build installers & executables:
        1. Check [Oracle's website](https://docs.oracle.com/en/java/javase/14/jpackage/packaging-overview.html#GUID-786E15C0-2CE7-4BDF-9B2F-AC1C57249134:~:text=Java%20Runtime%20Requirements-,Packaging%20Pre%2DReqs,WiX%203.0%20or%20later%20is%20required.,-Application%20Preparation) for info on your system's prerequisites.
        1. Open `build.gradle` and change the `jpackageTargetPlatform` variable in the `ext` block to match your operating system and architecture.
        1. Run `./gradlew jpackage`
        1. Check `build/jpackage` for the installer(s) and `build/jpackage/Wordle` for the executable.

## License

This project uses the [MIT License](https://opensource.org/licenses/MIT).

## Thanks

This project uses [JavaFX](https://openjfx.io) as well as the [badass-jlink-plugin](https://github.com/beryx/badass-jlink-plugin) by [beryx](https://github.com/beryx). Thanks to [electronwill](https://github.com/TheElectronWill) for the making [night-config](https://github.com/TheElectronWill/night-config) which was used for reading and writing TOML settings files.