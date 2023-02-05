import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.serialization") version "1.8.0"
    application
    `maven-publish`
}

group = "dev.proxyfox"
version = "1.8"
val ktor_version = "2.2.3"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.isDeprecation = true
    options.release.set(17)
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    repositories {
        maven {
            url = uri("https://maven.proxyfox.dev")
            credentials.username = System.getenv("PF_MAVEN_USER")
            credentials.password = System.getenv("PF_MAVEN_PASS")
        }
    }
}
