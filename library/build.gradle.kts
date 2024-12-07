import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "io.github.jesusdmedinac"
version = "1.0.0"

kotlin {
    jvm()
    androidTarget {
        publishLibraryVariants("release")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    linuxX64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                //put your multiplatform dependencies here
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}

android {
    namespace = "com.jesusdmedinac.json.to.compose"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(group.toString(), "json-to-compose", version.toString())

    pom {
        name = "Json to compose"
        description = "JSON to compose converter"
        inceptionYear = "2024"
        url = "https://github.com/jesusdmedinac/json-to-compose"
        licenses {
            license {
                name = "MIT License" // Reemplaza con la licencia que elijas
                url = "https://opensource.org/licenses/MIT" // Reemplaza con la URL de la licencia
                distribution = "https://opensource.org/licenses/MIT" // Reemplaza con la URL de la licencia
            }
        }
        developers {
            developer {
                id = "jesusdmedinac"
                name = "Jesús Daniel Medina Cruz"
                url = "https://github.com/jesusdmedinac/"
            }
        }
        scm {
            url = "https://github.com/jesusdmedinac/json-to-compose"
            connection = "scm:git:git://github.com/jesusdmedinac/json-to-compose.git"
            developerConnection = "scm:git:ssh://git@github.com/jesusdmedinac/json-to-compose.git"
        }
    }
}
