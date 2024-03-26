repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}
dependencies {
    compileOnly(project(":common"))
    compileOnly(project(":common-util"))
    compileOnly(project(":common-platform-api"))
    compileOnly(project(":module:module-chat"))
    compileOnly(project(":module:module-nms"))
    compileOnly(project(":module:module-bukkit-util"))
    compileOnly(project(":module:module-bukkit-xseries"))
    compileOnly(project(":module:module-bukkit-hook"))
    compileOnly(project(":expansion:expansion-item-tags"))
    compileOnly(project(":platform:platform-bukkit"))
    // 服务端
    compileOnly("net.kyori:adventure-text-minimessage:4.16.0")
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
}