@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id(libs.plugins.org.jetbrains.kotlin.android.get().pluginId)
    kotlin("kapt")
    alias(libs.plugins.com.google.dagger.hilt.android)
    id(libs.plugins.com.android.library.get().pluginId)
}

android {
    namespace = "com.pawlowski.notificationservice"
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":libs:dataStore"))

    implementation(libs.firebase.messaging)
    implementation(libs.hilt.android)
    implementation(libs.hilt.workmanager)
    implementation(project(":libs:network"))
    kapt(libs.hilt.workmanager.compiler)
    implementation(libs.androidx.work.runtime.ktx)
    kapt(libs.hilt.android.compiler)
    implementation(libs.bundles.grpc)
}
