load("@rules_java//java:defs.bzl", "java_binary", "java_library", "java_test")

package(default_visibility = ["//visibility:public"])

java_library(
    name = "refract",
    srcs = glob(["src/main/java/com/github/nprindle/refract/**/*.java"]),
    javacopts = ["-Xlint:unchecked"],
    # deps = ["@maven//:com_google_guava_guava"],
)

java_test(
    name = "tests",
    srcs = glob(["src/test/java/com/github/nprindle/refract/*.java"]),
    test_class = "com.github.nprindle.refract.TestMain",
    deps = [
        ":refract",
        # "@maven//:com_google_guava_guava",
        "@maven//:junit_junit",
    ],
)
