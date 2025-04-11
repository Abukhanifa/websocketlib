import java.util.Properties

plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
}

android {
    namespace = "com.example.lab2"
    compileSdk = 35

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        minSdk = 24
        targetSdk = 35

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}


// Load local.properties
val localProps = Properties().apply {
    val localPropsFile = File(rootDir, "local.properties")
    if (localPropsFile.exists()) {
        load(localPropsFile.inputStream())
    }
}

// Safely get username and password
val mavenUsername: String = localProps.getProperty("mavenUsername") ?: "no-username"
val mavenPassword: String = localProps.getProperty("mavenPassword") ?: "no-password"

println(">>> MAVEN USERNAME: $mavenUsername")
println(">>> MAVEN PASSWORD: $mavenPassword")

// PUBLISHING CONFIG
afterEvaluate {

    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.github.Abukhanifa" // GitHub username
                artifactId = "websocketlib"        // Your library name
                version = "1.0.0"                  // Version you want to publish
            }
        }
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/Abukhanifa/websocketlib")
                credentials {
                    username = mavenUsername
                    password = mavenPassword
                }
            }
        }
    }
}
