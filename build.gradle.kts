import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.0"
    id("com.google.protobuf") version "0.9.4" apply false
}

allprojects {
    group = "systems.ajax.motrechko"
    version = "0.0.1-SNAPSHOT"
    repositories {
        maven {
            url = uri("https://packages.confluent.io/maven/")
        }
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "com.google.protobuf")

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "17"
        }
    }
    java {
        sourceCompatibility = JavaVersion.VERSION_17
    }
    dependencies {
        implementation("com.google.api.grpc:proto-google-common-protos:2.26.0")
    }
}
