plugins {
    id("com.android.application")
}

android {
    compileSdkVersion(rootProject.extra.get("compileSdk") as Int)
    defaultConfig {
        minSdkVersion(rootProject.extra.get("minSdk") as Int)
        targetSdkVersion(rootProject.extra.get("targetSdk") as Int)
        buildToolsVersion = rootProject.extra.get("buildTools") as String
        versionCode = rootProject.extra.get("version_code") as Int
        versionName = rootProject.extra.get("version_name") as String
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))

    implementation("androidx.appcompat:appcompat:1.0.2")

    implementation(project(":library"))

    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}



