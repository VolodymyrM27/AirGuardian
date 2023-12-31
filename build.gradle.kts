import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.0"
    id("com.google.protobuf") version "0.9.4"
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

    dependencies {
        implementation("com.google.protobuf:protobuf-java:3.24.3")
        implementation("io.projectreactor:reactor-core:3.5.11")

        implementation("io.grpc:grpc-core:1.59.0")
        implementation("io.grpc:grpc-protobuf:1.59.0")
        implementation("io.grpc:grpc-netty:1.59.0")
        implementation("io.grpc:grpc-stub:1.59.0")

        implementation("com.salesforce.servicelibs:reactor-grpc:1.2.4")
        implementation("com.salesforce.servicelibs:reactive-grpc-common:1.2.4")
        implementation("com.salesforce.servicelibs:reactor-grpc-stub:1.2.4")
    }

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
