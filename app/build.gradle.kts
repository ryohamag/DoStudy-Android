import java.util.Properties


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.websarva.wings.dostudy_android"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.websarva.wings.dostudy_android"
        minSdk = 26
        targetSdk = 35
        versionCode = 3
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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

//        resValue("string", "ADMOB_APP_ID", project.hasProperty("ADMOB_APP_ID").toString())
        val properties = Properties()
        val localPropertiesFile = project.rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            properties.load(localPropertiesFile.inputStream())
        }
        val admobAppId = properties.getProperty("ADMOB_APP_ID", "")
        if (admobAppId.isNotEmpty()) {
            resValue("string", "ADMOB_APP_ID", admobAppId)
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
        implementation(libs.androidx.tracing.perfetto.handshake)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        androidTestImplementation(platform(libs.androidx.compose.bom))
        androidTestImplementation(libs.androidx.ui.test.junit4)
        debugImplementation(libs.androidx.ui.tooling)
        debugImplementation(libs.androidx.ui.test.manifest)

        //Okhttp
        implementation(libs.okhttp)

        //Room
        implementation(libs.androidx.room.runtime)
        implementation(libs.androidx.room.ktx)
        ksp(libs.androidx.room.compiler)

        //DropdownMenu
        implementation (libs.material3)

        //Navigation
        implementation(libs.androidx.navigation.compose)

        //Icons
        implementation(libs.androidx.material.icons.core)

        //Retrofit
        implementation(libs.retrofit)
        implementation(libs.converter.gson)

        //PermissionRequest
        implementation (libs.accompanist.permissions)

        //hilt
        implementation(libs.hilt.android)
        ksp(libs.hilt.android.compiler)

        //DataStore
        implementation ("androidx.datastore:datastore-preferences:1.1.7")


    }
}
dependencies {
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.play.services.ads.lite)
}
