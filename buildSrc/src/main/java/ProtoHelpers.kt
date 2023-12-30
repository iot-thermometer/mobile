import com.android.build.api.dsl.AndroidSourceSet
import com.google.protobuf.gradle.ProtobufExtension
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.proto
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType

fun Project.configureProtobuf() {
    extensions.findByType<ProtobufExtension>()!!.apply {
        protoc { artifact = libs.findLibrary("com-google-protobuf-protoc").get().get().toString() }

        plugins {
            id("grpc") {
                artifact = libs.findLibrary("io-grpc-protoc-gen-grpc-java").get().get().toString()
            }
            id("grpckt") {
                artifact = libs.findLibrary("io-grpc-protoc-gen-grpc-kotlin").get().get().toString()
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
        srcDir(srcPath)
    }
}
