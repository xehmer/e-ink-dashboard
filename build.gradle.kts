import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    idea

    val kotlinVersion = "2.1.0"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion

    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"

    id("ch.acanda.gradle.fabrikt") version "1.11.0"
}

group = "de.xehmer"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
//    mavenLocal()
}

dependencies {
    // Spring
    implementation("org.springframework.boot", "spring-boot-starter-web")
    implementation("org.springframework.boot", "spring-boot-starter-cache")
    implementation("org.springframework.boot", "spring-boot-starter-actuator")
    implementation("org.springframework.modulith", "spring-modulith-starter-core")

    // Jackson and others directly related to Spring
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin")
    implementation("io.freefair.okhttp-spring-boot", "okhttp-spring-boot-starter", SpringBootPlugin.BOM_COORDINATES.substringAfterLast(':'))

    // "official" kotlin additions
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx", "kotlinx-html-jvm", "0.12.0")
    implementation("org.jetbrains.kotlinx", "kotlinx-datetime", "0.6.1")
    implementation("org.jetbrains.kotlin-wrappers", "kotlin-css", "2025.1.5")

    // my custom stuff
    implementation("de.schildbach.pte", "public-transport-enabler") {
        version {
            branch = "master"
        }
    }

    // non-implementation stuff
    annotationProcessor("org.springframework.boot", "spring-boot-configuration-processor")

    // misc. non-production stuff
    developmentOnly("org.springframework.boot", "spring-boot-devtools")

    testImplementation("org.springframework.boot", "spring-boot-starter-test")
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.springframework.modulith", "spring-modulith-starter-test")
    testRuntimeOnly("org.junit.platform", "junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.modulith:spring-modulith-bom:1.3.1")
    }

    dependencies {
        dependencySet(mapOf("group" to "com.squareup.okhttp3", "version" to "4.12.0")) {
            entry("okhttp")
            entry("logging-interceptor")
        }
    }
}

fabrikt {
    defaults {
        validationLibrary = NoValidation
        model {
            generate = enabled
            serializationLibrary = Jackson
        }
        client {
            generate = enabled
            target = OkHttp
        }
        controller {
            generate = disabled
            target = Spring
        }
    }

    generate("dashboard") {
        apiFile = file("src/main/resources/dashboard-api.yaml")
        basePackage = "de.xehmer.dashboard.api"

        controller {
            generate = enabled
        }
        client {
            generate = disabled
        }
    }

//    generate("brightsky") {
//        apiFile = file("src/main/resources/apis/brightsky.yml")
//        basePackage = "brightsky.api"
//    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
