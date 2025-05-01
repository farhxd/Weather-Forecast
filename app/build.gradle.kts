import org.apache.tools.ant.util.JavaEnvUtils.VERSION_1_8

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.farhad.weatherforecast"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.farhad.weatherforecast"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}