package me.uni.hiker.utils.session

import android.content.Context
import android.content.SharedPreferences
import me.uni.hiker.model.user.User
import me.uni.hiker.utils.DateFormatter
import androidx.core.content.edit

private const val USR_PREF_KEY = "usr_pref_key"

private const val ID_KEY = "id_key"
private const val REMOTE_ID_KEY = "rem_id_key"
private const val NAME_KEY = "name_key"
private const val USERNAME_KEY = "usr_key"
private const val EMAIL_KEY = "email_key"
private const val TOKEN_KEY = "token_key"
private const val CREATED_AT_KEY = "crt_at_key"

fun Context.getUserSharedPreferences(): SharedPreferences {
    return getSharedPreferences(USR_PREF_KEY, Context.MODE_PRIVATE)
}

fun SharedPreferences.saveUserData(user: User) {
    this.edit {
        putLong(ID_KEY, user.id)
        user.remoteId?.also { putString(REMOTE_ID_KEY, it) }
        putString(NAME_KEY, user.name)
        putString(USERNAME_KEY, user.username)
        putString(EMAIL_KEY, user.email)
        putString(TOKEN_KEY, user.token)
        putString(CREATED_AT_KEY, DateFormatter.formatDate(user.createdAt))
    }
}

fun SharedPreferences.clearUserData() {
    this.edit {
        clear()
    }
}

fun SharedPreferences.getUserData(): User? {
    val id = this.getLong(ID_KEY, -1L)

    if (id == -1L) return null

    return User(
        id = id,
        remoteId = this.getString(REMOTE_ID_KEY, null),
        name = this.getString(NAME_KEY, "") ?: return null,
        username = this.getString(USERNAME_KEY, "")  ?: return null,
        email = this.getString(EMAIL_KEY, "")  ?: return null,
        token = this.getString(TOKEN_KEY, "")  ?: return null,
        createdAt = DateFormatter.formatDate(this.getString(CREATED_AT_KEY, "")  ?: return null),
    )
}