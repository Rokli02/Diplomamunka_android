package me.uni.hiker.utils.session

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import me.uni.hiker.model.Profile
import me.uni.hiker.model.user.User

class UserSessionViewModel: ViewModel() {
    var user by mutableStateOf<User?>(null)
    val isLoggedIn: Boolean get() = user != null
    var profile: Profile? = null
    lateinit var userSharedPreferences: SharedPreferences

    fun init(userSharedPreferences: SharedPreferences, profile: Profile) {
        this.userSharedPreferences = userSharedPreferences
        this.profile = profile
    }

    fun isInitialized(): Boolean {
        return profile != null
    }

    fun login(user: User) {
        this.user = user
        profile?.setUser(user)
        userSharedPreferences.saveUserData(user)
    }

    fun logout() {
        this.user = null
        profile?.clear()
        userSharedPreferences.clearUserData()
    }

    override fun onCleared() {
        super.onCleared()

        profile = null
    }
}