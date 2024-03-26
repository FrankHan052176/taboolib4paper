@file:Suppress("GradlePackageUpdate", "VulnerableLibrariesLocal")

repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    compileOnly(project(":module:module-chat"))
    compileOnly(project(":module:module-configuration"))
    compileOnly(project(":platform:platform-bukkit"))
    // 服务端
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
}