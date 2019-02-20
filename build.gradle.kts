// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    extra.set("kotlinVersion"   , "1.3.20")
    extra.set("minSdk"          , 15)
    extra.set("compileSdk"      , 28)
    extra.set("targetSdk"       , 28)
    extra.set("buildTools"      , "28.0.3")
    extra.set("version_code"    , 10)
    extra.set("version_name"    , "1.1.0")

    repositories {
        google()
        jcenter()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.3.1")
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.1")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
    // Define versions in a single place
    extra.apply{
        set("minSdkVersion", 26)
        set("targetSdkVersion", 27)
    }

}



allprojects {
    repositories {
        google()
        jcenter()
        maven("https://jitpack.io")
    }


}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
