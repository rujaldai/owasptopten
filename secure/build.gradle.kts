plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":libraries"))
    implementation("com.mysql:mysql-connector-j:9.2.0")
    implementation("org.jsoup:jsoup:1.19.1")

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}