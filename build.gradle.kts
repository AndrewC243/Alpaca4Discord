import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

plugins {
    kotlin("jvm") version "1.9.23"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

application {
    mainClass.set("discord.MainKt")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.23")
    implementation("net.jacobpeterson.alpaca:alpaca-java:10.0.1")
    implementation("org.jetbrains.exposed:exposed-core:0.50.1")
    implementation("org.jetbrains.exposed", "exposed-dao", "0.50.0")
    implementation("org.jetbrains.exposed", "exposed-jdbc", "0.50.0")
    implementation("com.h2database:h2:2.2.220")
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0")
}

//jar {
//
//}
//
//val runWithExec = tasks.register<JavaExec>("runWithExec") {
//    dependsOn("jar")
//    group = "Execution"
//    description = "Test"
//    exec { commandLine("java", "-jar", ) }
//}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}