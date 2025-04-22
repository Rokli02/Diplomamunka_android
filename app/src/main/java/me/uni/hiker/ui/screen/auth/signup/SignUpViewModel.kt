package me.uni.hiker.ui.screen.auth.signup

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.uni.hiker.R
import me.uni.hiker.api.model.RemoteUser
import me.uni.hiker.model.ErrorChecker
import me.uni.hiker.model.user.NewUser
import me.uni.hiker.service.ConnectionService
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val signUpUseCases: SignUpUseCases,
): ViewModel() {
    private val _newUser = MutableStateFlow(NewUser("", "", "", ""))
    val newUser = _newUser.asStateFlow()

    private val errorChecker = ErrorChecker(context)
    val errors = errorChecker.errors

    fun onChange(newUser: NewUser) { _newUser.update { newUser } }

    suspend fun onSubmit(): String? {
        errorChecker.clear()

        val newUser = newUser.value

        // Check if fields are right
        if (!signUpUseCases.validateFields(newUser, errorChecker)) { return context.getString(R.string.unsuccessful_sign_up) }

        var remoteUser: RemoteUser? = null
        if (ConnectionService.hasConnection(context)) {
            remoteUser = signUpUseCases.createUserOnServer(newUser)
        }

        // Try to create new user locally
        val signUpError = signUpUseCases.createUserOnDevice(newUser, remoteUser)
        when (signUpError) {
            null -> return null
            SignUpError.EMAIL_ALREADY_USED -> {
                errors["email"] = context.getString(R.string.unsuccessful_sign_up_email_already_occupied)

                return context.getString(R.string.unsuccessful_sign_up_email_already_occupied)
            }
            SignUpError.USERNAME_ALREADY_USED -> {
                errors["username"] = context.getString(R.string.unsuccessful_sign_up_username_already_occupied)

                return context.getString(R.string.unsuccessful_sign_up_username_already_occupied)
            }
            else -> return context.getString(R.string.unsuccessful_sign_up)
        }
    }
}