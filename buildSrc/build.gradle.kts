plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(libs.com.android.tools.build.gradle)
    implementation(libs.org.jetbrains.kotlin.gradle.plugin)
    implementation(libs.com.squareup.javapoet) // Fix hilt issue
    implementation(libs.com.google.protobuf.gradle.plugin)
}
