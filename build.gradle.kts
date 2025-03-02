plugins {
    `java-library`
    alias(libs.plugins.shadow)
}

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

group = "takumi3s.rcdamagecore"
version = "0.1.0-SNAPSHOT"
description = "RcDamageCore"
java.sourceCompatibility = JavaVersion.VERSION_21

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(libs.protocollib)
    compileOnly(libs.mythic.dist)
    compileOnly("net.azisaba.loreeditor:api:${libs.versions.loreeditor.api.get()}:all")

    implementation(libs.acf.paper)
}

tasks.compileJava {
    options.isFork = true
    options.compilerArgs.add("-parameters")
    options.forkOptions.executable = System.getProperty("java.home") + "/bin/javac"
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

tasks.shadowJar {
    relocate("co.aikar.commands", "takumi3s.rcdamagecore.acf")
    relocate("co.aikar.locales", "takumi3s.rcdamagecore.locales")
}
