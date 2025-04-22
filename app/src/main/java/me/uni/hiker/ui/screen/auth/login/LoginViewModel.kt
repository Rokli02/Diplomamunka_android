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
        var areFieldsRight = !errorChecker.isFieldBlank(login.usernameOrEmail, "usernameOrEmail")

        areFieldsRight = !errorChecker.isFieldBlank(login.password, "password") &&
                errorChecker.lengthConstraintsMatch(login.password, "password", min = 8, max = 255) &&
                areFieldsRight

        if (!areFieldsRight) { return null }

        // Check for local user
        val (localUser, localError) = loginUseCases.loginLocally(login)
        val hasNetworkConnection = ConnectionService.hasConnection(context)

        when {
            localError == LoginError.INACTIVE -> {
                errors["usernameOrEmail"] = context.getString(R.string.user_is_inactive)
                errors["password"] = context.getString(R.string.user_is_inactive)

                return null
            }
            localError == LoginError.NOT_FOUND && !hasNetworkConnection -> {
                errors["usernameOrEmail"] = context.getString(R.string.username_or_password_is_incorrect)
                errors["password"] = context.getString(R.string.username_or_password_is_incorrect)

                return null
            }
        }

        // If no critical error occurred check for an online profile
        val (userFromServer, serverError) = loginUseCases.loginToServer(login)

        when {
            serverError == LoginError.NO_SERVER || serverError == LoginError.UNKNOWN -> return localUser
            serverError == LoginError.INACTIVE -> {
                errors["usernameOrEmail"] = context.getString(R.string.user_is_inactive)
                errors["password"] = context.getString(R.string.user_is_inactive)

                return null
            }
            serverError == null && localError == null -> {
                localUser!!.token = userFromServer!!.token

                return localUser
            }
            serverError == LoginError.NOT_FOUND && localError == null -> {
                return localUser?.let { loginUseCases.signUpToServer(it, login.password) }
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