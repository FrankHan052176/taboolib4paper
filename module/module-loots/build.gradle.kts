@file:Suppress("GradlePackageUpdate", "VulnerableLibrariesLocal")
repositories {
    mavenCentral()
}

dependencies {
    compileOnly(project(":common"))
    compileOnly(project(":common-util"))
    compileOnly(project(":common-platform-api"))
    compileOnly(project(":module:module-configuration"))
    compileOnly(project(":module:module-bukkit-hook"))
    compileOnly(project(":module:module-bukkit-util"))
    compileOnly(project(":platform:platform-bukkit"))
    // 服务端
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
}