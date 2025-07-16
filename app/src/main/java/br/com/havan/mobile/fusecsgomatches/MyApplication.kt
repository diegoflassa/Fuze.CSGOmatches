package br.com.havan.mobile.fusecsgomatches

import android.app.Application
import android.util.Log

class MyApplication : Application() {

    companion object {
        private val tag = MyApplication::class.simpleName
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(tag, "onCreate")
    }
}