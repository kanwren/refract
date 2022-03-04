{
  description = "Template for a flake with a devShell";

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
    in
    {
      devShell = pkgs.mkShell {
        buildInputs = with pkgs; [
          openjdk11

          # linting/formatting
          checkstyle
          google-java-format
          nixpkgs-fmt
          lefthook

          # bazel
          bazel_5
          python3
        ];
      };
    });
}
