package me.uni.hiker.ui.screen.auth.login

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.uni.hiker.R
import me.uni.hiker.model.ErrorChecker
import me.uni.hiker.model.user.Login
import me.uni.hiker.model.user.User
import me.uni.hiker.service.ConnectionService
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val loginUseCases: LoginUseCases,
): ViewModel() {
    private val _login = MutableStateFlow(Login("", ""))
    val login = _login.asStateFlow()

    private val errorChecker = ErrorChecker(context)
    val errors = errorChecker.errors

    fun changeLoginDetails(login: Login) { _login.update { login } }

    suspend fun onSubmit(): User? {
        errorChecker.clear()

        val login = login.value.copy()

        // Check validity of fields
        if (!loginUseCases.validateField(login, errorChecker)) { return null }

        // Check for local user
        val (localUser, localError) = loginUseCases.loginLocally(login)
        var user = localUser?.let { User.fromEntity(it) }
        val hasNetworkConnection = ConnectionService.hasConnection(context)

        if ((localError == LoginError.NOT_FOUND || localError == LoginError.INVALID_INPUT) && !hasNetworkConnection ) {
            errors["usernameOrEmail"] = context.getString(R.string.username_or_password_is_incorrect)
            errors["password"] = context.getString(R.string.username_or_password_is_incorrect)

            return null
        }

        // If no critical error occurred check for an online profile
        val (userFromServer, serverError) = loginUseCases.loginToServer(login)

        when {
            serverError == LoginError.NO_SERVER || serverError == LoginError.UNKNOWN -> {
                if (localError == LoginError.INACTIVE) {
                    errors["usernameOrEmail"] = context.getString(R.string.user_is_inactive)
                }

                if (localError != null) {
                    errors["usernameOrEmail"] = context.getString(R.string.username_or_password_is_incorrect)
                    errors["password"] = context.getString(R.string.username_or_password_is_incorrect)

                    return null
                }

                return user
            }
            serverError == LoginError.INACTIVE -> {
                errors["usernameOrEmail"] = context.getString(R.string.user_is_inactive)

                // Deactivate LocalUser
                localUser?.also {
                    loginUseCases.modifyLocalUserDate(it.copy(isActive = false))
                }

                return null
            }
            serverError == null && localError == null -> {
                // Check for difference between profile from server and the one stored locally
                val ufs = userFromServer!!.user
                val difference = ufs.compareToLocalUser(localUser!!)
                if (difference != 0) {
                    val modifiedLUD = loginUseCases.modifyLocalUserDate(localUser.copy(
                        name = if (difference and 0b1 != 0) ufs.name else localUser.name,
                        username = if (difference and 0b10 != 0) ufs.username else localUser.username,
                        email = if (difference and 0b100 != 0) ufs.email else localUser.email,
                    ))

                    if (modifiedLUD != null) {
                        user = User.fromEntity(modifiedLUD)
                    }

                }

                user!!.token = userFromServer.token

                return user
            }
            serverError == LoginError.NOT_FOUND && localError == null -> {
                return user?.let { loginUseCases.signUpToServer(it, login.password) }
            }
            serverError == null && localError == LoginError.NOT_FOUND -> {
                userFromServer?.also {
                    loginUseCases.signUpToDevice(it.user, login.password)?.also { newUser ->
                        newUser.token = it.token

                        return newUser
                    }
                }

                return null
            }
            serverError == null && localError == LoginError.INVALID_INPUT -> {
                if (localUser != null && userFromServer != null) {
                    // Modify changed datas locally and set new password
                    val ufs = userFromServer.user
                    val difference = ufs.compareToLocalUser(localUser)
                    val modifiedLUD = loginUseCases.modifyLocalUserDate(user = localUser.copy(
                        name = if (difference and 0b1 != 0) ufs.name else localUser.name,
                        username = if (difference and 0b10 != 0) ufs.username else localUser.username,
                        email = if (difference and 0b100 != 0) ufs.email else localUser.email,
                    ), password = login.password)

                    if (modifiedLUD != null) {
                        user = User.fromEntity(modifiedLUD)
                    }

                    user!!.token = userFromServer.token
                    return user
                }

                return null
            }
        }

        errors["usernameOrEmail"] = context.getString(R.string.username_or_password_is_incorrect)
        errors["password"] = context.getString(R.string.username_or_password_is_incorrect)

        return null
    }

    fun clearStates() {
        errorChecker.clear()
        _login.update { Login("", "") }
    }
}