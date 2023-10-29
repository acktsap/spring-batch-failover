plugins {
    id("spring-batch-failover.kotlin-conventions")
    id("spring-batch-failover.spring-conventions")
}

dependencies {
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.batch)
    implementation(libs.spring.boot.starter.batch.plus)
    implementation(libs.spring.boot.starter.log4j2)
    implementation(libs.kotlin.logging)
    runtimeOnly(libs.h2)
}
