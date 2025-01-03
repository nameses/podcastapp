plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.podcastapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.podcastapp"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
            signingConfig = signingConfigs.getByName("debug")
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.hilt.android)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)

    implementation(project(":core:common"))
    implementation(project(":core:network"))
    implementation(project(":core:feature_api"))
    implementation(project(":features:main:ui"))
    implementation(project(":features:main:data"))
    implementation(project(":features:main:domain"))
    implementation(project(":features:auth:data"))
    implementation(project(":features:auth:domain"))
    implementation(project(":features:auth:ui"))
    implementation(project(":features:profile:data"))
    implementation(project(":features:profile:domain"))
    implementation(project(":features:profile:ui"))
    implementation(project(":features:podcast_details:data"))
    implementation(project(":features:podcast_details:domain"))
    implementation(project(":features:podcast_details:ui"))
    implementation(project(":features:player:data"))
    implementation(project(":features:player:domain"))
    implementation(project(":features:player:ui"))
    implementation(project(":core:commonui"))
    implementation(libs.timber)
    implementation(project(":features:episode_details:data"))
    implementation(project(":features:episode_details:domain"))
    implementation(project(":features:episode_details:ui"))
    implementation(project(":features:search:data"))
    implementation(project(":features:search:domain"))
    implementation(project(":features:search:ui"))

    kapt(libs.dagger.hilt.android.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
