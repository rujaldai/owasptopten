plugins {
    id("java-library")
}

group = "com.owasptopten.libraries"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api(platform(libs.spring.boot.dependencies.platform))
    api(libs.bundles.project.basic)
}
