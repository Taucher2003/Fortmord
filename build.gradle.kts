plugins {
    id("de.nycode.spigot-dependency-loader") version "1.1.2"
    id("io.micronaut.library") version "3.5.1"
    id("com.github.johnrengelman.shadow") version "7.1.2"
//    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

group = "club.devcord.gamejam"
version = "1.0-SNAPSHOT"
description = "fortmord"

dependencies {
    annotationProcessor("io.micronaut:micronaut-inject-java")

    spigot("io.micronaut", "micronaut-inject-java", "3.6.1")
    implementation("io.micronaut", "micronaut-runtime", "3.6.1")

    compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
}

tasks {
    shadowJar {
        mergeServiceFiles()
    }
}

micronaut {
    version("3.6.1")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

//bukkit {
//    name = "Fortmord"
//    load = net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder.STARTUP
//    main = "club.devcord.gamejam.FortMord"
//    apiVersion = "1.19"
//}
