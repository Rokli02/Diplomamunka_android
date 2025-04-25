package me.uni.hiker.service

import android.content.Context
import android.net.ConnectivityManager
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

object ConnectionService {
    fun getOnlineObserverFlow(context: Context, observeInterval: Long = 3000): Flow<Boolean> {
        return flow {
            var previousValue: Boolean? = null

            while (currentCoroutineContext().isActive) {
                val currentValue = (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?)?.activeNetwork != null

                if (currentValue != previousValue) {
                    previousValue = currentValue

                    emit(currentValue)
                }

                delay(observeInterval)
            }
        }
    }

    fun hasConnection(context: Context): Boolean {
        return (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?)?.activeNetwork != null
    }
}