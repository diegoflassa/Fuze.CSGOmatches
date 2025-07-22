package dev.diegoflassa.fuzecsgomatches

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    companion object {
        private val tag = MyApplication::class.simpleName
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(tag, "onCreate")
    }
}
