val appVersion: String by project.rootProject.extra

plugins {
    kotlin("jvm") version "1.9.21"
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
    }

    group = "com.martkans"
    version = appVersion
}