// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    var kotlin_version: String by extra
    kotlin_version = "1.3.31"

    extra.set("kotlinVersion"   , "1.3.20")
    extra.set("minSdk"          , 15)
    extra.set("compileSdk"      , 28)
    extra.set("targetSdk"       , 28)
    extra.set("buildTools"      , "28.0.3")
    extra.set("version_code"    , 13)
    extra.set("version_name"    , "1.1.3")

    repositories {
        google()
        jcenter()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.4.1")
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.1")
        classpath(kotlin("gradle-plugin", version = kotlin_version))
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven("https://jitpack.io")
        mavenCentral()
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
