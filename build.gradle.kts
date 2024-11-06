plugins {
    id("com.kozyr.pnomialparser.buildsrc.kotlin-convention")
}

group = "com.kozyr.pnomialparser"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(kotlin("reflect"))
}

kotlin.compilerOptions.freeCompilerArgs.add("-Xnon-local-break-continue")