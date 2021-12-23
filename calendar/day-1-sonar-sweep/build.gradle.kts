val kotlinStdLibVersion: String by project.rootProject.extra

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${property("kotlinStdLibVersion")}")
}