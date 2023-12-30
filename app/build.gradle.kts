import com.android.build.api.dsl.AndroidSourceSet
import com.google.protobuf.gradle.ProtobufExtension
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.proto

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.com.google.dagger.hilt.android)
    id(libs.plugins.com.google.protobuf.get().pluginId)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization)
    id("com.google.gms.google-services")
}

configureProtobuf()

android {
    namespace = "com.pawlowski.temperaturemanager"
    compileSdk = ProjectConfig.compileSdk

    sourceSets.getByName("main") {
        setProtoPath(srcPath = "src/main/proto/proto")
        java.srcDirs(
            "build/generated/source/proto/main/grpc",
            "build/generated/source/proto/main/grpckt",
            "build/generated/source/proto/main/java",
        )
    }

    defaultConfig {
        applicationId = "com.pawlowski.temperaturemanager"
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = ProjectConfig.kotlinCompilerExtensions
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(project(":libs:network"))
    implementation(project(":notificationService"))

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.firebase.messaging)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    implementation(libs.com.juul.kable.core)

    implementation(libs.accompanist.permissions)
    implementation(libs.bundles.grpc)
    implementation(libs.com.google.protobuf.protobuf.javalite)

    implementation(libs.bundles.navigation)

    implementation(platform(libs.org.jetbrains.kotlinx.kotlinx.serialization.bom))
    implementation(libs.bundles.serialization)
    implementation(libs.androidx.datastore)
    implementation(libs.security.crypto.datastore)

    implementation(libs.lottie)
}

fun Project.configureProtobuf() {
    extensions.findByType<ProtobufExtension>()!!.apply {
        protoc { artifact = libs.com.google.protobuf.protoc.get().toString() }

        plugins {
            id("grpc") {
                artifact = libs.io.grpc.protoc.gen.grpc.java.get().toString()
            }
            id("grpckt") {
                artifact = libs.io.grpc.protoc.gen.grpc.kotlin.get().toString()
            }

            generateProtoTasks {
                all().forEach { task ->
                    task.builtins {
                        id("java") {
                            option("lite")
                        }
                    }

                    task.plugins {
                        id("grpc") {
                            option("lite")
                        }
                        id("grpckt")
                    }
                }
            }
        }
    }
}

fun AndroidSourceSet.setProtoPath(srcPath: String) {
    proto {
        setSrcDirs(listOf(srcPath))
    }
}
