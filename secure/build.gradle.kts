plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":libraries"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}