{
  description = "A Nix-flake-based Java development environment";

  inputs.nixpkgs.url = "https://flakehub.com/f/NixOS/nixpkgs/0.1";

  outputs = {self, ...} @ inputs: let
    javaVersion = 25;

    supportedSystems = [
      "x86_64-linux"
      "aarch64-linux"
      "x86_64-darwin"
      "aarch64-darwin"
    ];
    forEachSupportedSystem = f:
      inputs.nixpkgs.lib.genAttrs supportedSystems (
        system:
          f {
            inherit system;
            pkgs = import inputs.nixpkgs {
              inherit system;
              overlays = [inputs.self.overlays.default];
            };
          }
      );
  in {
    overlays.default = final: prev: let
      jdk = prev."jdk${toString javaVersion}";
    in {
      inherit jdk;
      maven = prev.maven.override {jdk_headless = jdk;};
      gradle = prev.gradle.override {java = jdk;};
      lombok = prev.lombok.override {inherit jdk;};
    };

    packages = forEachSupportedSystem (
      {
        pkgs,
        system,
      }: let
        lib = pkgs.lib;
        isLinux = pkgs.stdenv.hostPlatform.isLinux;

        javaFxLibPath = with pkgs;
          lib.makeLibraryPath [
            glib
            libGL
            libxtst
            libxxf86vm
          ];

        # Fixed-output derivation that pre-fetches all Gradle dependencies
        # (both build-script plugins and project dependencies) with network
        # access.  The resulting store path is passed as GRADLE_USER_HOME in
        # the main build so it can run fully offline.
        #
        # When dependencies change, update the hash by:
        #   1. Set outputHash to lib.fakeHash below.
        #   2. Run `nix build .#packages.<system>.default 2>&1 | grep "got:"`.
        #   3. Replace lib.fakeHash with the printed sha256 hash.
        offlineDeps = pkgs.stdenv.mkDerivation {
          name = "connect-4-gradle-deps";
          src = ./.;

          nativeBuildInputs = with pkgs; [gradle jdk];

          buildPhase = ''
            export HOME="$TMPDIR"
            export GRADLE_USER_HOME="$TMPDIR/gradle-home"
            # buildEnvironment resolves buildscript plugin dependencies;
            # dependencies resolves the project's runtime dependencies.
            # No test dependencies are declared in this project.
            gradle --no-daemon buildEnvironment dependencies
          '';

          installPhase = ''
            cp -r "$TMPDIR/gradle-home" "$out"
          '';

          outputHashAlgo = "sha256";
          outputHashMode = "recursive";
          outputHash = lib.fakeHash;
        };
      in {
        default = pkgs.stdenv.mkDerivation {
          pname = "connect-4";
          version = "2.0";

          src = ./.;

          nativeBuildInputs = with pkgs; [
            gradle
            jdk
            makeWrapper
            unzip
          ];

          buildPhase = ''
            runHook preBuild

            export HOME="$TMPDIR"
            export GRADLE_USER_HOME="${offlineDeps}"
            gradle --no-daemon --offline clean jlinkZip

            runHook postBuild
          '';

          installPhase = ''
            runHook preInstall

            mkdir -p "$out"
            unzip -q build/connect4.zip -d "$out"
            appDir="$(find "$out" -mindepth 1 -maxdepth 1 -type d | head -n 1)"
            mkdir -p "$out/bin"

            makeWrapper "$appDir/bin/connect-4" "$out/bin/connect-4" \
              --set JAVA_HOME "${pkgs.jdk}" \
              ${
              if isLinux
              then "--prefix LD_LIBRARY_PATH : ${javaFxLibPath}"
              else ""
            }

            runHook postInstall
          '';

          meta = with pkgs.lib; {
            description = "A simple Connect 4 game implemented in JavaFX";
            homepage = "https://github.com/megabyte6/connect-4";
            license = licenses.mit;
            platforms = supportedSystems;
            mainProgram = "connect-4";
          };
        };

        connect-4 = self.packages.${system}.default;
      }
    );

    apps = forEachSupportedSystem (
      {system, ...}: {
        default = {
          type = "app";
          program = "${self.packages.${system}.default}/bin/connect-4";
        };
        connect-4 = self.apps.${system}.default;
      }
    );

    devShells = forEachSupportedSystem (
      {
        pkgs,
        system,
      }: let
        # JavaFX Linux runtime dependencies
        javaFxLibPath = with pkgs;
          lib.makeLibraryPath [
            glib
            libGL
            libxtst
            libxxf86vm
          ];
      in {
        default = pkgs.mkShellNoCC {
          packages = with pkgs; [
            gcc
            gradle
            jdk
            maven
            ncurses
            patchelf
            zlib
            self.formatter.${system}
          ];

          shellHook = let
            loadLombok = "-javaagent:${pkgs.lombok}/share/java/lombok.jar";
            prev = "\${JAVA_TOOL_OPTIONS:+ $JAVA_TOOL_OPTIONS}";
          in ''
            export PATH="${pkgs.jdk}/bin:$PATH"
            export JAVA_HOME="${pkgs.jdk}"
            export JAVA_TOOL_OPTIONS="${loadLombok}${prev}"
            export LD_LIBRARY_PATH="${javaFxLibPath}:''${LD_LIBRARY_PATH:-}"
          '';
        };
      }
    );

    formatter = forEachSupportedSystem ({pkgs, ...}: pkgs.nixfmt);
  };
}
