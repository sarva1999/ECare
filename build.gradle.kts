buildscript {

    dependencies {
        // Add the dependency for the Google services Gradle plugin
        classpath("com.google.gms:google-services:4.3.15")
    }
}

plugins {
    id("com.android.application") version "7.4.2" apply false
    id("com.android.library") version "7.4.2" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}

allprojects {

}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
//dependencies {
    //implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.8.0")
//}