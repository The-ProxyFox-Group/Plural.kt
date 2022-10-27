import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

application {
    mainClass.set("com.example.projectname.MainKt")
}