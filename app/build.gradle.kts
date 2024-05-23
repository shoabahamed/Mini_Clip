plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.mini_clip"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mini_clip"
        minSdk = 26
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding=true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

//    glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    //firebase ui
    implementation ("com.firebaseui:firebase-ui-firestore:8.0.2")
    implementation ("com.firebaseui:firebase-ui-storage:8.0.2")

    implementation ("androidx.recyclerview:recyclerview:1.2.1")

    implementation ("com.arthenica:ffmpeg-kit-full:4.5.LTS")


    // Unit testing dependencies
    testImplementation(libs.junit)
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.3")

    // UI testing dependencies
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation("org.mockito:mockito-core:3.12.4")
    androidTestImplementation("org.mockito:mockito-android:3.12.4")
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-idling-resource:3.5.1")

    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.2.0")
}



