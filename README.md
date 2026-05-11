# Connect 4

## About

`connect-4` is a fun clone of the board game Connect 4 written in Java.

---

## Running Connect 4:

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
    - Run `connect-4`.
- Nix (with flakes):
    - To just run `connect-4`: `nix run github:megabyte6/connect-4`.
    - To install `connect-4` at the user-level: `nix profile install github:megabyte6/connect-4`.
    - To install `connect-4` system-wide, you can use something akin to the following example flake:
      ```nix
      {
        description = "Example flake that includes connect-4 as a package";

        inputs = {
          nixpkgs.url = "github:NixOS/nixpkgs/nixos-25.11";
          connect-4.url = "github:megabyte6/connect-4";
        };

        outputs = { self, nixpkgs, connect-4, ... }:
        let
          system = "x86_64-linux";
        in {
          nixosConfigurations.myHost = nixpkgs.lib.nixosSystem {
          inherit system;
          modules = [
            ({ pkgs, ... }: {
              environment.systemPackages = [
                connect-4.packages.${system}.default
              ];
            })
          ];
          };
        };
      }
      ```

---

### Option 2: I don't want to install stuff.

Note: This project should be built with JDK 25 of later.

1. Download this repository with the green `Code` button.
1. Navigate to the project folder. You should see a `build.gradle` file.
1. Open a terminal in this directory and run `./gradlew run` (or `gradlew.bat run` on Windows) to run the game.

---

## Building this project:

Note: This project should be built with JDK 25 or later.

### Build installers & executables (recommended)

1. Download this repository with the green `Code` button. 
1. Check [Oracle's website](https://docs.oracle.com/en/java/javase/14/jpackage/packaging-overview.html#GUID-786E15C0-2CE7-4BDF-9B2F-AC1C57249134:~:text=Java%20Runtime%20Requirements-,Packaging%20Pre%2DReqs,WiX%203.0%20or%20later%20is%20required.,-Application%20Preparation) for info on your system's prerequisites.
1. Optionally change the `jpackageFormat` option in `gradle.properties` if you want to build a different type of installer. By default, it will choose a format based on your current operating system. Note that you can only build for the operating system you're on (e.g. you can only build a Windows installer on Windows).
1. Run `./gradlew jpackage`
1. Check `build/jpackage` for the installer(s) and `build/jpackage/Connect 4` for the executable(s).

### Build portable image with custom JRE

This option is provided in case the above option does not work. Some Linux distros like NixOS may have trouble building using jpackage.

1. Download this repository with the green `Code` button.
1. Run `./gradlew jlinkZip`
1. Check the `build` folder for the `.zip` image.
1. Play it by extracting the zip and running the `connect-4` in the `bin` folder

### Nix (flakes)

For NixOS users, the nix package is recommended as the app has trouble finding the correct libraries when using the pre-built jlink image.

1. Download this repository with the green `Code` button.
1. Run `nix build`.
1. Check the `result` folder for the build image. The entrypoint is located at `result/bin/connect-4`.

---

## Contributing

Contributions are welcome! [Create an issue](https://github.com/megabyte6/connect-4/issues/new/choose) or open a [pull request](https://github.com/megabyte6/connect-4/compare)! If you are interested in contributing but don't know where to start, check out the [existing issues](https://github.com/megabyte6/connect-4/issues) for some ideas.

If you are on NixOS, you can use the dev shell to set up a development environment with all the necessary dependencies. To enter the dev shell, run `nix develop` in the project directory. This works well with `direnv` if you wish to use it. **If you plan to update any Gradle dependencies, plugins or similar, remember to delete `gradle.lock` and regenerate it with `nix run github:tadfisher/gradle2nix/v2 -- --task=jlinkZip` so Nix can correctly pre-download the correct versions.**

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
