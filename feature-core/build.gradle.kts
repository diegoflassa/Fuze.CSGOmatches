plugins {
    id("android-library-convention")
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.com.google.devtools.ksp)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "dev.diegoflassa.fusecsgomatches.core"
}

kotlin {
    jvmToolchain(JavaVersion.VERSION_21.toString().toInt())
}

dependencies {
    //Unit tests
    testImplementation(libs.junit)

    //Instrumented Tests
    androidTestImplementation(libs.ax.test.ext.junit.ktx)
    androidTestImplementation(libs.ax.test.expresso.core)

    //Compose
    implementation(platform(libs.ax.compose.bom))
    implementation(libs.ax.compose.ui)
    implementation(libs.ax.compose.ui.graphics)
    implementation(libs.ax.compose.ui.tooling)
    implementation(libs.ax.compose.ui.tooling.preview)
    implementation(libs.ax.compose.ui.viewbinding)
    implementation(libs.ax.compose.runtime.livedata)
    implementation(libs.ax.compose.runtime.rxjava3)
    implementation(libs.ax.compose.material3)
    implementation(libs.ax.constraintlayout.compose)
    implementation(libs.ax.compose.material.icons.core)
    implementation(libs.ax.compose.material.icons.extended)
    implementation(libs.ax.activity.compose)
    implementation(libs.ax.lifecycle.viewmodel.compose)
    implementation(libs.ax.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    //Compose Navigation 3
    implementation(libs.ax.navigation3.runtime)
    implementation(libs.ax.navigation3.ui)

    //OkHttp
    implementation(platform(libs.com.squareup.okhttp3.bom))
    implementation(libs.com.squareup.okhttp3)
    implementation(libs.com.squareup.okhttp3.logging.interceptor)

    //Moshi
    implementation(libs.com.squareup.moshi.kotlin)
    ksp(libs.com.squareup.moshi.kotlin.codegen)

    //Retrofit 2
    implementation(libs.com.squareup.retrofit2.retrofit)
    implementation(libs.com.squareup.retrofit2.converter.moshi)

    //Splashscreen
    implementation(libs.ax.core.splashscreen)
}
