import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.0-SNAPSHOT"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.spring") version "1.6.10"
}

group = "nl.rijksoverheid.minienw.validation-service"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
	//Platform
	implementation("org.springframework.boot:spring-boot-starter-web:2.6.4")
	implementation("org.springframework.boot:spring-boot-starter-validation:2.6.4")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf:2.6.4")
	implementation("org.springframework.boot:spring-boot-devtools")

	testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.4")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:2.6.4")

	implementation (files("lib/onlineverification_messages-0.0.2-SNAPSHOT.jar"))
	testImplementation (files("lib/onlineverification_messages-0.0.2-SNAPSHOT.jar"))

	//Language
	implementation ("org.jetbrains.kotlin:kotlin-stdlib")
	implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	//Logging
	implementation("org.apache.logging.log4j:log4j-core:2.17.2")
	implementation("org.apache.logging.log4j:log4j-api:2.17.2")

	//Binary references
	implementation (files("lib/certlogic-kotlin-v0.9.0-kotlin.jar"))

	//Persistence
	implementation("redis.clients:jedis:4.1.1")


	//data formats
	implementation ("com.google.code.gson:gson:2.9.0")
	//implementation ("org.json:json:20211205")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin") //:2.13.1
	implementation("com.fasterxml.jackson.core:jackson-databind")
//	compileOnly ("org.projectlombok:lombok:1.18.22")

	//crypto
	implementation ("org.bouncycastle:bcprov-jdk15on:1.70")
	implementation ("org.bouncycastle:bcprov-ext-jdk15on:1.70")
	implementation ("org.bouncycastle:bcpkix-jdk15on:1.70")

	testImplementation ("org.bouncycastle:bcprov-jdk15on:1.70")
	testImplementation ("org.bouncycastle:bcprov-ext-jdk15on:1.70")
	testImplementation ("org.bouncycastle:bcpkix-jdk15on:1.70")

	implementation ("io.jsonwebtoken:jjwt-api:0.11.2")
	runtimeOnly ("io.jsonwebtoken:jjwt-impl:0.11.2")
	//runtimeOnly ("io.jsonwebtoken:jjwt-jackson:0.11.2")
	//-OR-
	runtimeOnly ("io.jsonwebtoken:jjwt-gson:0.11.2")// for gson

	//Backend/imported projects
//	implementation ("com.squareup.okio:okio:3.0.0")
//	implementation ("com.squareup.moshi:moshi:1.13.0")
//	implementation ("com.squareup.okhttp3:okhttp:4.9.3")
//	implementation ("com.squareup.retrofit2:retrofit:2.9.0")


	//Decoding HCERT
	implementation("com.augustcellars.cose:cose-java:1.1.0")
	implementation("io.github.ehn-digital-green-development:base45:0.0.2")

	//Decoding CBOR - fails
	//implementation("se.digg.dgc:dgc-schema:1.0.1")
	//implementation("se.digg.dgc:dgc-create-callback:0.0.2")

	//Swagger
	implementation ("io.springfox:springfox-boot-starter:3.0.0")

}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
