apply {
    from("$rootDir/library-build.gradle")
}

plugins {
    kotlin(KotlinPlugins.serialization) version Kotlin.version
    id(SqlDelight.plugin)
}

dependencies {
    "implementation"(project(Modules.heroDomain))

    "implementation"(Ktor.core)
    "implementation"(Ktor.clientSerialization)

    // TODO: newest version is 2.0.2
//    "implementation"(Ktor.contentNegotiation)
//    "implementation"(Ktor.serializationKotlinX)
    "implementation"(Ktor.android)

    "implementation"(SqlDelight.runtime)
}

sqldelight {
    database("HeroDatabase") {
        packageName = "de.bembelnaut.courses.modularizingapps.hero_interactors.cache"
        sourceFolders = listOf("sqldelight")
    }
}