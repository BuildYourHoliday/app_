plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "it.unimib.buildyourholiday"
    compileSdk = 34

    defaultConfig {
        applicationId = "it.unimib.buildyourholiday"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
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

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment:2.7.5")
    implementation("androidx.navigation:navigation-ui:2.7.5")
    implementation("commons-validator:commons-validator:1.7")
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
  //  implementation("com.squareup.retrofit2:retrofit:2.9.0")
  //  implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //implementation("com.amadeus:amadeus-android:1.3.1")
    implementation("com.amadeus:amadeus-java:8.0.0")

    implementation("io.reactivex.rxjava3:rxjava:3.1.5")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")

    implementation("com.mapbox.maps:android:10.16.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(platform("com.google.firebase:firebase-bom:32.7.1"))
    implementation("com.google.firebase:firebase-auth")

    implementation("com.google.firebase:firebase-database")
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    implementation ("de.hdodenhof:circleimageview:3.1.0")

}