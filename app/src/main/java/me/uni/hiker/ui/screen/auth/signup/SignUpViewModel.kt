package me.uni.hiker.ui.screen.auth.signup

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.uni.hiker.R
import me.uni.hiker.db.dao.LocalUserDAO
import me.uni.hiker.model.ErrorChecker
import me.uni.hiker.model.user.NewUser
import me.uni.hiker.utils.encrypter.Hasher
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userDAO: LocalUserDAO,
    private val hasher: Hasher,
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
        var areFieldsRight = !errorChecker.isFieldBlank(newUser.name, "name")

        areFieldsRight = !errorChecker.isFieldBlank(newUser.username, "username") &&
                areFieldsRight

        areFieldsRight = !errorChecker.isFieldBlank(newUser.email, "email") &&
                errorChecker.isValidEmail(newUser.email, "email") &&
                areFieldsRight

        areFieldsRight = !errorChecker.isFieldBlank(newUser.password, "password") &&
                areFieldsRight

        if (!areFieldsRight) { return context.getString(R.string.unsuccessful_sign_up) }
        // TODO: Try to create new user on the server
        //       Wait for ack

        // Try to create new user locally
        val userFromDb = userDAO.isExistsByEmailOrUsername(newUser.email, newUser.username)

        if (userFromDb != null) {
            if (userFromDb.username == newUser.username) {
                errors["username"] = context.getString(R.string.unsuccessful_sign_up_username_already_occupied)
                return context.getString(R.string.unsuccessful_sign_up_username_already_occupied)
            }

            if (userFromDb.email == newUser.email) {
                errors["email"] = context.getString(R.string.unsuccessful_sign_up_email_already_occupied)
                return context.getString(R.string.unsuccessful_sign_up_email_already_occupied)
            }

            return context.getString(R.string.unsuccessful_sign_up)
        }

        val hashedPassword = hasher.hash(newUser.password) ?: return null
        Log.d("SignUpViewModel", "newUser.password = (${newUser.password})")
        Log.d("SignUpViewModel", "hashedPassword = (${hashedPassword})")
        userDAO.insertOne(newUser.copy(
            password = hashedPassword,
        ).toEntity())

        return null
    }
}