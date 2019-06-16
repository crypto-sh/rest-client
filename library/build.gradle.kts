plugins {
    id("com.android.library")
    id("com.github.dcendents.android-maven") // ADD THIS
}

group   = "com.github.rest-client"
version = rootProject.extra.get("version_name")!!

android {
    compileSdkVersion(rootProject.extra.get("compileSdk") as Int)
    defaultConfig {
        minSdkVersion(rootProject.extra.get("minSdk") as Int)
        targetSdkVersion(rootProject.extra.get("targetSdk") as Int)
        buildToolsVersion   = rootProject.extra.get("buildTools") as String
        versionCode         = rootProject.extra.get("version_code") as Int
        versionName         = rootProject.extra.get("version_name") as String
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("debug"){
            buildConfigField("Boolean","enableDebugLogging", "true")
        }
        getByName("release"){
            isMinifyEnabled = false
            buildConfigField("Boolean","enableDebugLogging", "true")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility  = JavaVersion.VERSION_1_8
        targetCompatibility  = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${rootProject.extra.get("kotlinVersion")}")
    implementation("androidx.appcompat:appcompat:1.0.2")
    implementation("com.squareup.okhttp3:okhttp:3.14.2")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")

}
