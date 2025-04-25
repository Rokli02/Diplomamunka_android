package me.uni.hiker.utils.interceptors

import me.uni.hiker.model.Profile
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthorizationInterceptor(private val profile: Profile): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        var request: Request = originalRequest

        if (profile.hasToken()) {
            request = originalRequest
                .newBuilder()
                .addHeader("Authorization", "Bearer ${profile.getToken()}")
                .build()
        }

        val response = chain.proceed(request)

        if (response.code() == 401 && profile.user != null) {
            profile.clear()
            profile.notifySubscribers()
        }

        return response
    }
}