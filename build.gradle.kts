plugins {
    id ("java")
    id ("org.springframework.boot") version "3.4.2"
    id ("io.spring.dependency-management") version "1.1.6"
    id ("org.jetbrains.kotlin.jvm")
    id ("io.freefair.lombok") version "8.10.2"
}

group = "com.barracuda"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation ("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation ("org.springframework.boot:spring-boot-starter-jdbc")
    implementation ("org.springframework.boot:spring-boot-starter-security")
    implementation ("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation ("org.springframework.boot:spring-boot-starter-web")
    implementation ("org.springframework.boot:spring-boot-starter-hateoas")
    implementation ("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    implementation ("org.liquibase:liquibase-core")
    implementation ("org.springframework.boot:spring-boot-starter-validation")

    developmentOnly ("org.springframework.boot:spring-boot-devtools")
    developmentOnly ("org.springframework.boot:spring-boot-docker-compose")
    runtimeOnly ("com.mysql:mysql-connector-j")

    testImplementation ("org.springframework.boot:spring-boot-starter-test")
    testImplementation ("org.springframework.boot:spring-boot-testcontainers")
    testImplementation ("org.springframework.security:spring-security-test")
    testImplementation ("org.testcontainers:junit-jupiter")
    testImplementation ("org.testcontainers:mysql")
    testImplementation ("org.junit.jupiter:junit-jupiter-params")
    testRuntimeOnly ("org.junit.platform:junit-platform-launcher")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    compileOnly("org.projectlombok:lombok:1.18.34")

}

tasks.test{
    useJUnitPlatform()
}
