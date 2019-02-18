plugins {
    id("com.android.library")
    id("com.github.dcendents.android-maven") // ADD THIS
}

group   = "com.github.rest-client"
version = "1.0.9"

android {
    compileSdkVersion(28)
    defaultConfig {
        minSdkVersion(15)
        targetSdkVersion(28)
        buildToolsVersion = "28.0.3"
        versionCode = 9
        versionName = "1.0.9"
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
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.20")
    implementation("androidx.appcompat:appcompat:1.0.2")
    implementation("com.squareup.okhttp3:okhttp:3.12.0")
    //implementation("androidx.annotation:annotation:1.0.1")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test:runner:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.1.1")
}
