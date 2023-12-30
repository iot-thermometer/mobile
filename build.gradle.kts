// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.google.dagger.hilt.android) apply false
//    alias(libs.plugins.com.android.library) apply false
}
true // Needed to make the Suppress annotation work for the plugins block

buildscript {
    dependencies {
        classpath(libs.com.android.tools.build.gradle)
        classpath(libs.org.jetbrains.kotlin.gradle.plugin)
        classpath(libs.com.google.protobuf.gradle.plugin)
        classpath("com.google.gms:google-services:4.4.0")
    }
}
