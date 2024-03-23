dependencies {
    compileOnly("ink.ptms.core:v10900:10900")
}
tasks.named("publishMavenPublicationToMavenRepository").configure { dependsOn("jar") }