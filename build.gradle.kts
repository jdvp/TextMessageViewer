import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal object App {
    const val VERSION = "2.0.0"
    const val PACKAGE_NAME = "Text Message Viewer"
    const val MAIN_CLASS = "MainKt"
    const val JVM_TARGET = "15"
}

plugins {
    id(Plugins.KOTLIN_JVM) version Plugins.KOTLIN_JVM_VERSION
    id(Plugins.COMPOSE) version Plugins.COMPOSE_VERSION
}

group = "me.jdvp"
version = App.VERSION

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    testImplementation(Dependencies.KOTLIN_TEST)
    implementation(compose.desktop.currentOs)
    implementation(Dependencies.COROUTINES_CORE)
    implementation(Dependencies.LIB_PHONE_NUMBER)
    implementation(Dependencies.COMMONS_IO)
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = App.JVM_TARGET
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}

compose.desktop {
    application {
        mainClass = App.MAIN_CLASS
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = App.PACKAGE_NAME
            packageVersion = App.VERSION
        }
    }
}