plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.shadow)
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "takumi3s.rcdamagecore"
version = "0.1.0-SNAPSHOT"
description = "RcDamageCore"
java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenLocal()
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://mvn.lumine.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://repo.azisaba.net/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.maven.apache.org/maven2/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.aikar.co/content/groups/aikar/")
}

dependencies {
    compileOnly(libs.paper.api)
    implementation(libs.kotlin.stdlib.jdk8)

    implementation(libs.acf.paper)

    compileOnly(libs.protocollib)
    compileOnly(libs.mythic.dist)
    compileOnly("net.azisaba.loreeditor:api:${libs.versions.loreeditor.api.get()}:all")

    testImplementation(libs.bundles.kotest)
}

val targetJvmVersion = 21
kotlin {
    jvmToolchain(targetJvmVersion)
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}

tasks.compileJava {
    options.isFork = true
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
    options.forkOptions.executable = System.getProperty("java.home") + "/bin/javac"
}

tasks.compileKotlin {
    compilerOptions.javaParameters = true
}

tasks.shadowJar {
    relocate("co.aikar.commands", "takumi3s.rcdamagecore.acf")
    relocate("co.aikar.locales", "takumi3s.rcdamagecore.locales")
}

tasks.runServer {
    minecraftVersion("1.21.1")
}

tasks.test {
    useJUnitPlatform()
}
