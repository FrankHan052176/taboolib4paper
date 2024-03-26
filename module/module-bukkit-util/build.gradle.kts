@file:Suppress("GradlePackageUpdate", "VulnerableLibrariesLocal")

repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    compileOnly(project(":common"))
    compileOnly(project(":common-legacy-api"))
    compileOnly(project(":common-platform-api"))
    compileOnly(project(":common-util"))
    compileOnly(project(":module:module-chat"))
    compileOnly(project(":module:module-lang"))
    compileOnly(project(":module:module-configuration"))
    compileOnly(project(":module:module-bukkit-xseries"))
    compileOnly(project(":platform:platform-bukkit"))
    // 服务端
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
}