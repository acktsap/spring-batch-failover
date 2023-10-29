rootProject.name = "spring-batch-failover"

include("spring-batch-failover-kotlin")

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        maven {
            url = uri("https://repo.spring.io/milestone")
        }
    }

    versionCatalogs {
        create("libs") {
            library(
                "spring-boot-starter",
                "org.springframework.boot",
                "spring-boot-starter"
            ).withoutVersion()
            library(
                "spring-boot-starter-web",
                "org.springframework.boot",
                "spring-boot-starter-web"
            ).withoutVersion()
            library(
                "spring-boot-starter-batch",
                "org.springframework.boot",
                "spring-boot-starter-batch"
            ).withoutVersion()
            library(
                "spring-boot-starter-log4j2",
                "org.springframework.boot",
                "spring-boot-starter-log4j2"
            ).withoutVersion()

            library(
                "spring-boot-starter-batch-plus",
                "com.navercorp.spring:spring-boot-starter-batch-plus-kotlin:1.0.1"
            )
            library(
                "kotlin-logging",
                "io.github.oshai:kotlin-logging-jvm:5.1.0"
            )
            library(
                "h2",
                "com.h2database:h2:2.1.214"
            )
        }
    }
}
