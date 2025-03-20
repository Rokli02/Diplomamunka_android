package me.uni.hiker.utils.session

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import me.uni.hiker.model.user.User

class UserSessionViewModel: ViewModel() {
    var user by mutableStateOf<User?>(null)
    lateinit var userSharedPreferences: SharedPreferences

    val isLoggedIn: Boolean get() = user != null

    fun login(user: User) {
        this.user = user
        userSharedPreferences.saveUserData(user)
    }

    fun logout() {
        this.user = null
        userSharedPreferences.clearUserData()
    }
}