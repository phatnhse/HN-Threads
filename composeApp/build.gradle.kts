import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    // alias(libs.plugins.room) // Commented until Room setup
    // alias(libs.plugins.ksp) // Commented until Room setup
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            
            // Platform-specific HTTP engine
            implementation(libs.ktor.client.okhttp)
        }
        
        iosMain.dependencies {
            // Platform-specific HTTP engine
            implementation(libs.ktor.client.darwin)
        }
        
        commonMain.dependencies {
            // Compose Multiplatform
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            
            // Lifecycle & ViewModel
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            
            // Core dependencies for now - others will be added as needed
            // implementation(libs.navigation.compose) // Commented until stable
            // implementation(libs.koin.core) // Commented until setup  
            // implementation(libs.room.runtime) // Commented until setup
            // implementation(libs.room.ktx) // Commented until setup
            
            // Basic network and serialization
            implementation(libs.ktor.client.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.napier)
        }
        
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            // implementation(libs.turbine) // Commented until setup
            // implementation(libs.koin.test) // Commented until setup
        }
    }
}

android {
    namespace = "com.phatnhse.hnthreads"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.phatnhse.hnthreads"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

// room {
//     schemaDirectory("$projectDir/schemas")
// }

dependencies {
    debugImplementation(compose.uiTooling)
    
    // Room KSP - Commented until Room is properly set up
    // add("kspCommonMainMetadata", libs.room.compiler)
    // add("kspAndroid", libs.room.compiler)
    // add("kspIosX64", libs.room.compiler)
    // add("kspIosArm64", libs.room.compiler)
    // add("kspIosSimulatorArm64", libs.room.compiler)
}

