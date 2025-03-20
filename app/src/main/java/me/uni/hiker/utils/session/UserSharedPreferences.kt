package me.uni.hiker.utils.session

import android.content.Context
import android.content.SharedPreferences
import me.uni.hiker.model.user.User
import me.uni.hiker.utils.DateFormatter

private const val USR_PREF_KEY = "usr_pref_key"

private const val ID_KEY = "id_key"
private const val NAME_KEY = "name_key"
private const val USERNAME_KEY = "usr_key"
private const val EMAIL_KEY = "email_key"
private const val TOKEN_KEY = "token_key"
private const val CREATED_AT_KEY = "crt_at_key"

fun Context.getUserSharedPreferences(): SharedPreferences {
    return getSharedPreferences(USR_PREF_KEY, Context.MODE_PRIVATE)
}

fun SharedPreferences.saveUserData(user: User) {
    val editor = this.edit()

    editor.putLong(ID_KEY, user.id)
    editor.putString(NAME_KEY, user.name)
    editor.putString(USERNAME_KEY, user.username)
    editor.putString(EMAIL_KEY, user.email)
    editor.putString(TOKEN_KEY, user.token)
    editor.putString(CREATED_AT_KEY, DateFormatter.formatDate(user.createdAt))

    editor.apply()
}

fun SharedPreferences.clearUserData() {
    val editor = this.edit()

    editor.clear()

    editor.apply()
}

fun SharedPreferences.getUserData(): User? {
    val id = this.getLong(ID_KEY, -1L)

    if (id == -1L) return null

    return User(
        id = id,
        name = this.getString(NAME_KEY, "") ?: return null,
        username = this.getString(USERNAME_KEY, "")  ?: return null,
        email = this.getString(EMAIL_KEY, "")  ?: return null,
        token = this.getString(TOKEN_KEY, "")  ?: return null,
        createdAt = DateFormatter.formatDate(this.getString(CREATED_AT_KEY, "")  ?: return null),
    )
}