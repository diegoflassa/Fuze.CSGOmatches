plugins {
    id("android-library-convention")
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    alias(libs.plugins.com.google.devtools.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
}


android {
    namespace = "dev.diegoflassa.fusecsgomatches.main"
}

dependencies {
    //Módulos
    implementation(project(":feature-core"))

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

    //Paging
    implementation(libs.ax.paging.runtime.ktx)
    implementation(libs.ax.paging.compose)

    //Compose Navigation 3
    implementation(libs.ax.navigation3.runtime)
    implementation(libs.ax.navigation3.ui)
    implementation(libs.ax.navigation3.viewmodel)
    //implementation(libs.ax.navigation3.adaptive)

    //Dagger & Hilt
    implementation(libs.ax.hilt.common)
    implementation(libs.com.google.dagger.hilt.android)
    implementation(libs.ax.hilt.navigation.compose)
    ksp(libs.com.google.dagger.hilt.android.compiler)
    ksp(libs.ax.hilt.compiler)

    //OkHttp
    implementation(platform(libs.com.squareup.okhttp3.bom))
    implementation(libs.com.squareup.okhttp3)
    implementation(libs.com.squareup.okhttp3.logging.interceptor)

    //Moshi
    implementation(libs.com.squareup.moshi.kotlin)
    ksp(libs.com.squareup.moshi.kotlin.codegen)

    //Retrofit 2
    implementation(libs.com.squareup.retrofit2.retrofit)

    //Splashscreen
    implementation(libs.ax.core.splashscreen)

    //Other
    implementation(libs.io.coil.kt.coil.compose)
}
