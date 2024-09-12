
repositories {
    mavenCentral()
}

tasks.bootJar{
    enabled = false
}

tasks.jar{
    enabled = true
}
dependencies{
    implementation(project(":orderProduct"))
    implementation(project(":sendEmail"))
}