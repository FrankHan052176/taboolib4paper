@file:Suppress("GradlePackageUpdate", "VulnerableLibrariesLocal")
repositories {
    mavenCentral()
    maven("https://maven.enginehub.org/repo/")
}

dependencies {
    compileOnly(project(":common"))
    compileOnly(project(":common-util"))
    compileOnly(project(":common-platform-api"))
    compileOnly(project(":platform:platform-bukkit"))
    // 服务端
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
    // 依赖插件
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.1.0-SNAPSHOT")
    compileOnly("io.th0rgal:oraxen:1.167.0")
    compileOnly("net.milkbowl.vault:Vault:1")
    compileOnly("public:PlaceholderAPI:2.10.9")
    compileOnly("com.ticxo.modelengine:api:R3.2.0")
    compileOnly("com.ticxo.playeranimator:PlayerAnimator:R1.2.8")
    compileOnly("net.kyori:adventure-text-minimessage:4.16.0")
    compileOnly("net.kyori:adventure-text-serializer-gson:4.16.0")
    compileOnly("net.william278.huskhomes:huskhomes-bukkit:4.6.1")
}