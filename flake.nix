{
  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }: flake-utils.lib.eachSystem [ "x86_64-linux" ] (system:
    let
      pkgs = import nixpkgs {
        inherit system;
        config.allowUnfree = true;
      };

      getInputs = drv:
        builtins.concatMap (is: drv.${is} or [ ]) [ "buildInputs" "propagatedBuildInputs" "nativeBuildInputs" "propagatedNativeBuildInputs" ];

      refractVersion = "0.0.1";
      refractBazelArgs = {
        version = refractVersion;
        bazel = pkgs.bazel_5;
        buildInputs = with pkgs; [
          python3
          openjdk11
        ];
      };

      refract-lib = with pkgs; buildBazelPackage (refractBazelArgs // {
        src = ./.;
        pname = "refract";
        bazelTarget = "//:refract";
        fetchAttrs.sha256 = "sha256-900z/eboIlYFD0wJLkotAdQL7qOATe9UZLI3lCITNYM=";
        buildAttrs.installPhase = ''
          mkdir -p "$out"/target
          install -Dm0444 bazel-bin/librefract.jar "$out"/target/librefract.jar
        '';
      });
    in
    {
      defaultPackage = self.packages.${system}.refract;

      packages.refract = refract-lib;

      checks.refract-tests = with pkgs; buildBazelPackage (refractBazelArgs // {
        src = ./.;
        pname = "refract-tests";
        bazelTarget = "//:tests";
        fetchAttrs.sha256 = "sha256-GKyUkE+8Q7qlDa1bcHzVidGQJsaT4D9K8Qrf9kctkJo=";
        buildAttrs.installPhase = ''
          exec bazel-bin/tests > "$out"
        '';
      });

      devShell = pkgs.mkShell {
        buildInputs = with pkgs; lib.flatten [
          (getInputs refract-lib)

          # linting/formatting
          checkstyle
          google-java-format
          nixpkgs-fmt
          lefthook
        ];
      };
    });
}
