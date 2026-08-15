import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "ir.mehdi.imposter"
    compileSdk = 36

    // Release signing is read from local.properties (gitignored), so the
    // keystore path/password never enters version control.
    val localProps = Properties()
    val propsFile = rootProject.file("local.properties")
    if (propsFile.exists()) {
        FileInputStream(propsFile).use { localProps.load(it) }
    }
    val storeFile: String? = localProps.getProperty("RELEASE_STORE_FILE") as? String
    val storePassword: String? = localProps.getProperty("RELEASE_STORE_PASSWORD") as? String
    val keyAlias: String? = localProps.getProperty("RELEASE_KEY_ALIAS") as? String
    val keyPassword: String? = localProps.getProperty("RELEASE_KEY_PASSWORD") as? String

    signingConfigs {
        create("release") {
            if (storeFile != null && storePassword != null &&
                keyAlias != null && keyPassword != null
            ) {
                this.storeFile = File(storeFile)
                this.storePassword = storePassword
                this.keyAlias = keyAlias
                this.keyPassword = keyPassword
            }
        }
    }

    defaultConfig {
        applicationId = "ir.mehdi.imposter"
        minSdk = 24
        targetSdk = 36
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
            // Only wire the signing config once the keys are present locally.
            if (storeFile != null) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Core
    implementation(libs.androidx.core.ktx)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.foundation)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Activity Compose
    implementation(libs.androidx.activity.compose)

    // Navigation Compose
    implementation(libs.androidx.navigation.compose)

    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Window Manager (for edge-to-edge)
    implementation(libs.androidx.window.manager)

    // Splash Screen
    implementation(libs.androidx.core.splashscreen)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}
