package me.uni.hiker.service

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

object ConnectionService {
    fun getOnlineObserverFlow(context: Context): Flow<Boolean> {
        return flow {
            var previousValue: Boolean? = null

            while (currentCoroutineContext().isActive) {
                val currentValue = (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?)?.activeNetwork != null

                if (currentValue != previousValue) {
                    previousValue = currentValue

                    emit(currentValue)
                }

                Log.d("TEST", "onlineObserverFlow emited")
                delay(3000)
            }
        }
    }

    fun hasConnection(context: Context): Boolean {
        return (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?)?.activeNetwork != null
    }
}