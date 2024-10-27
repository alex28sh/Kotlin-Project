plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:okhttp-urlconnection:4.11.0")
    implementation("com.squareup.moshi:moshi:1.15.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0") // Include this for Kotlin support
}

gradlePlugin {
    plugins {
        create("description-plugin") {
            id = "com.example.description-plugin"
            implementationClass = "com.example.DescriptionPlugin"
        }
    }
}