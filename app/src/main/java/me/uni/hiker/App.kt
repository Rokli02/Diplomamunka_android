package me.uni.hiker

import android.app.Application

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        println("OnCreate")
    }

    override fun onTerminate() {
        super.onTerminate()
        println("OnTerminate")
    }

}