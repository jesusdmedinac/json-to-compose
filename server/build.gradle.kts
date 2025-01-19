plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
}

tasks {
    create("stage").dependsOn("installDist")
}

group = "com.jesusdmedinac.compose.sdui"
version = "1.0.0"
application {
    mainClass.set("com.jesusdmedinac.compose.sdui.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    testImplementation(libs.kotlin.test.junit)
}