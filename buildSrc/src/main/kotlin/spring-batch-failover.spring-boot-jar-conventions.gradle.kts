import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot")
}

tasks.named<Jar>("jar") {
    enabled = false // prevent to generate plain jar
}

tasks.named<BootJar>("bootJar") {
    archiveClassifier.set("boot")
}
