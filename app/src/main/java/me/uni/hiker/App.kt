package me.uni.hiker

import android.app.Application

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        di = DependencyInjectionImpl(this)
        println("OnCreate")
    }

    companion object {
        lateinit var di: DependencyInjection
    }
}