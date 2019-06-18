plugins {
    id("com.android.application")
}
apply {
    plugin("kotlin-android")
    plugin("kotlin-android-extensions")
}

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
        getByName("release"){
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${rootProject.extra.get("kotlinVersion")}")
    implementation("androidx.appcompat:appcompat:1.0.2")
    testImplementation("junit:junit:4.12")
    implementation(project(":library"))
    androidTestImplementation("androidx.test:runner:1.2.0")
//    implementation("com.github.alishatergholi:rest-client:v1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}
