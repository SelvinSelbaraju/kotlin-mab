plugins {
    kotlin("jvm") version "1.9.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:dataframe:0.11.1")
    implementation("org.jetbrains.lets-plot:lets-plot-kotlin-jvm:4.4.1")
}