repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}
dependencies {
    compileOnly(project(":common"))
    compileOnly(project(":common-util"))
    compileOnly(project(":common-platform-api"))
    compileOnly(project(":module:module-configuration"))
    // 服务端
    compileOnly("org.mongodb:mongodb-driver-sync:5.0.0")
}