repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}
dependencies {
    compileOnly(project(":common"))
    compileOnly(project(":common-util"))
    compileOnly(project(":common-platform-api"))
    compileOnly(project(":module:module-configuration"))
    // 服务端
    compileOnly("redis.clients:jedis:5.1.2")
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
}