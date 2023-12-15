plugins {
    kotlin("jvm") version "1.9.21"
}

dependencies{
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
}
