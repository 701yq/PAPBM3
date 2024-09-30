// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}
// Root-level build.gradle.kts (Project level)
buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:8.0.2")
        classpath("com.google.gms:google-services:4.3.14") // Add this line
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}