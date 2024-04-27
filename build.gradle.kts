import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `maven-publish`
    java
    id("org.jetbrains.kotlin.jvm") version "1.9.22" apply false
    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "maven-publish")

    repositories {
        mavenCentral()
        maven("https://maven.enginehub.org/repo/")
        maven("https://libraries.minecraft.net")
        maven("https://repo1.maven.org/maven2")
        maven("https://maven.aliyun.com/repository/central")
        maven("https://repo.codemc.io/repository/nms/")
        maven("http://frankhan.top:8081/repository/maven-releases") {
            isAllowInsecureProtocol = true
        }
        maven("http://sacredcraft.cn:8081/repository/releases") {
            isAllowInsecureProtocol = true
        }
        maven("https://repo.oraxen.com/releases")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.william278.net/releases")
        maven("https://mvn.lumine.io/repository/maven/")
        maven(url = "https://mvn.lumine.io/repository/maven-public/")
        maven { url = uri("https://jitpack.io") }
        mavenLocal()
    }

    dependencies {
        compileOnly(kotlin("stdlib"))
        compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
        compileOnly("com.google.guava:guava:21.0")
        compileOnly("com.google.code.gson:gson:2.8.7")
        compileOnly("org.apache.commons:commons-lang3:3.5")
        compileOnly("org.tabooproject.reflex:reflex:1.0.23")
        compileOnly("org.tabooproject.reflex:analyser:1.0.23")
    }

    java {
         withSourcesJar()
    }

    tasks.withType<ShadowJar> {
        archiveClassifier.set("")
        relocate("org.tabooproject", "taboolib.library")
    }

    tasks.build {
        dependsOn("shadowJar")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf("-XDenableSunApiLintControl"))
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
            freeCompilerArgs = listOf("-Xjvm-default=all")
        }
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

subprojects
    .filter { it.name != "module" && it.name != "platform" && it.name != "expansion" }
    .forEach { proj ->
        proj.publishing { applyToSub(proj) }
    }

fun PublishingExtension.applyToSub(subProject: Project) {
    repositories {
        maven("http://frankhan.top:8081/repository/maven-releases") {
            isAllowInsecureProtocol = true
            credentials {
                username = "admin"
                password = "HanJiaLe052176abc!@"
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
        mavenLocal()
    }
    publications {
        repositories {
            maven("http://frankhan.top:8081/repository/maven-releases") {
                isAllowInsecureProtocol = true
            }
        }
        create<MavenPublication>("maven") {
            artifactId = subProject.name
            groupId = "io.izzel.taboolib"
            version = (if (project.hasProperty("devLocal")) {
                "${project.version}-local-dev"
            } else if (project.hasProperty("dev")) {
                "${project.version}-dev"
            } else {
                "${project.version}"
            })
            artifact(subProject.tasks["kotlinSourcesJar"])
            artifact(subProject.tasks["shadowJar"])
            println("> Apply \"$groupId:$artifactId:$version\"")
        }
    }
}