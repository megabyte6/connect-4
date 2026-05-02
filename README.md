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
- Nix (with flakes):
    - To just run `connect-4`: `nix run github:megabyte6/connect-4`.
    - To install `connect-4` at the user-level: `nix profile install github:megabyte6/connect-4`.
    - To install `connect-4` system-wide, you can use something akin to the following example flake:
      ```nix
      {
        description = "Example flake that includes connect-4 as a package";

        inputs = {
          nixpkgs.url = "github:NixOS/nixpkgs/nixos-24.11";
          connect4.url = "github:megabyte6/connect-4";
        };

        outputs = { self, nixpkgs, connect4, ... }:
        let
          system = "x86_64-linux";
        in {
          nixosConfigurations.myHost = nixpkgs.lib.nixosSystem {
          inherit system;
          modules = [
            ({ pkgs, ... }: {
              environment.systemPackages = [
                connect4.packages.${system}.default
              ];
            })
          ];
          };
        };
      }
      ```

Note:

- The installers are currently only available for Windows.
- If you're on Windows and wish to use a portable build, you can use `connect4-win-x64-x.x.zip` (which ships with a JVM) or `connect4-win-x64-portable-x.x.zip` (which has a windows executable. This option is recommended).

---

### Option 2: I don't want to install stuff.

Note: This project should be built with JDK 25 but the build script will install it automatically if you don't already have a compatible JDK.

1. Download this repository with the green `Code` button.
1. Navigate to the project folder. You should see a `build.gradle` file.
1. Open a terminal in this directory and run `./gradlew run` (or `gradlew.bat run` on Windows) to run the game.

---

## Building this project:

Note: This project should be built with JDK 25.

1. Download this repository with the green `Code` button.
1. Run the build command using one of the following options:
    - Build portable images (this one is easier):
        1. Run `./gradlew jlinkZip`
        1. Check the `build` folder for the `.zip` image.
        1. Play it by extracting the zip and running the `connect-4` in the `bin` folder
    - Build installers & executables:
        1. Check [Oracle's website](https://docs.oracle.com/en/java/javase/14/jpackage/packaging-overview.html#GUID-786E15C0-2CE7-4BDF-9B2F-AC1C57249134:~:text=Java%20Runtime%20Requirements-,Packaging%20Pre%2DReqs,WiX%203.0%20or%20later%20is%20required.,-Application%20Preparation) for info on your system's prerequisites.
        1. Optionally change the `jpackageFormat` option in `gradle.properties` if you want to build a different type of installer/executable. By default, it will choose a format based on your current operating system. Note that you can only build for the operating system you're on (e.g. you can only build a Windows installer on Windows).
        1. Run `./gradlew jpackage`
        1. Check `build/jpackage` for the installer(s) and `build/jpackage/Connect 4` for the executable(s).

### Nix (flakes)

Building can be done with `nix build` once the project is cloned.

For NixOS users, the nix package is recommended as it has trouble finding the correct libraries when using the pre-built jlink image.

---

## Contributing

Contributions are welcome! [Create an issue](https://github.com/megabyte6/connect-4/issues/new/choose) or open a [pull request](https://github.com/megabyte6/connect-4/compare)! If you are interested in contributing but don't know where to start, check out the [existing issues](https://github.com/megabyte6/connect-4/issues) for some ideas.

If you are on NixOS, you can use the dev shell to set up a development environment with all the necessary dependencies. To enter the dev shell, run `nix develop` in the project directory. This works well with `direnv` if you wish to use it. **If you plan to update any Gradle dependencies/plugins or similar, remember to delete `gradle.lock` and regenerate it with `nix run github:tadfisher/gradle2nix/v2 -- --task=jlinkZip` so Nix can correctly pre-download the correct versions.**

Note:
If editing on VSCode, run `./gradlew eclipse` to fix errors in `module-info.java` that occur due to a bug in the Java language server extension.

---

## License

This project uses the [MIT License](https://opensource.org/licenses/MIT).

---

## Thanks

This project was possible due to the following projects:
- [JavaFX](https://openjfx.io)
- [badass-jlink-plugin](https://github.com/beryx/badass-jlink-plugin)
- [jackson](https://github.com/FasterXML/jackson)
- [gradle2nix](https://github.com/tadfisher/gradle2nix)