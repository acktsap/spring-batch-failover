plugins {
    `kotlin-dsl` // support convension plugins in kotlin
}

repositories {
    mavenCentral()
    gradlePluginPortal() // give accees to gradle community plugins
    maven {
        url = uri("https://repo.spring.io/milestone")
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10")
    implementation("org.jlleitschuh.gradle.ktlint:org.jlleitschuh.gradle.ktlint.gradle.plugin:11.6.1")
    implementation("org.springframework.boot:spring-boot-gradle-plugin:3.1.5")
    implementation("io.spring.gradle:dependency-management-plugin:1.1.3")
    implementation("org.jetbrains.kotlin:kotlin-allopen:1.9.10")
}
