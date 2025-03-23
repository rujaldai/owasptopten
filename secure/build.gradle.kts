plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":libraries"))
    
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("com.mysql:mysql-connector-j:9.2.0")
    
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

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