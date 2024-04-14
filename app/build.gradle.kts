plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.gradle.secrets)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.example.wakatime"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.wakatime"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

//        buildConfigField("String", "WAKA_API_KEY", WAKA_API_KEY)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.retrofit)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.kotlinx.serialization)
    implementation(libs.retrofit.converter)
    implementation(libs.okhttp)
    implementation(libs.androidx.lifecycle)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.navigation)
    implementation(libs.gson)
    implementation(libs.gson.converter)
    implementation(libs.androidx.glance)
    implementation(libs.androidx.glance.m2)
    implementation(libs.androidx.glance.m3)
    implementation(libs.androidx.datastore)
    implementation(libs.retrofit.converter.scalars)
    implementation(libs.androidx.compose.navigation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // work manager
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.androidx.work)
    implementation(libs.androidx.hilt.work)

    // hilt
    implementation(libs.dagger.hilt)
    ksp(libs.dagger.hilt.compiler)
    ksp(libs.androidx.hilt.compiler)
}