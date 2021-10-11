private object Versions {
    const val KOTLIN_VERSION = "1.5.31"
    const val COROUTINES_VERSION = "1.5.2"
    const val LIB_PHONE_NUMBER_VERSION = "8.12.33"
    const val COMMONS_IO_VERSION = "2.6"
}

object Plugins {
    const val KOTLIN_JVM = "org.jetbrains.kotlin.jvm"
    const val KOTLIN_JVM_VERSION = Versions.KOTLIN_VERSION
    const val COMPOSE = "org.jetbrains.compose"
    const val COMPOSE_VERSION = "1.0.0-alpha4-build396"
}

object Dependencies {
    const val KOTLIN_TEST = "org.jetbrains.kotlin:kotlin-test:${Versions.KOTLIN_VERSION}"
    const val COROUTINES_CORE = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES_VERSION}"
    const val LIB_PHONE_NUMBER = "com.googlecode.libphonenumber:libphonenumber:${Versions.LIB_PHONE_NUMBER_VERSION}"
    const val COMMONS_IO = "commons-io:commons-io:${Versions.COMMONS_IO_VERSION}"
}