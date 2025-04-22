package me.uni.hiker.ui.screen.auth.signup

import android.util.Log
import me.uni.hiker.api.model.RemoteUser
import me.uni.hiker.api.service.UserService
import me.uni.hiker.db.dao.LocalUserDAO
import me.uni.hiker.model.ErrorChecker
import me.uni.hiker.model.user.NewUser
import me.uni.hiker.utils.encrypter.Hasher
import javax.inject.Inject

class SignUpUseCases @Inject constructor(
    private val userDAO: LocalUserDAO,
    private val userService: UserService,
    private val hasher: Hasher,
){
    fun validateFields(newUser: NewUser, errorChecker: ErrorChecker): Boolean {
        var areFieldsRight = !errorChecker.isFieldBlank(newUser.name, "name")

        areFieldsRight = !errorChecker.isFieldBlank(newUser.username, "username") &&
                areFieldsRight

        areFieldsRight = !errorChecker.isFieldBlank(newUser.email, "email") &&
                errorChecker.isValidEmail(newUser.email, "email") &&
                areFieldsRight

        areFieldsRight = !errorChecker.isFieldBlank(newUser.password, "password") &&
                errorChecker.lengthConstraintsMatch(newUser.password, "password", min = 8, max = 255) &&
                areFieldsRight

        return areFieldsRight
    }

    suspend fun createUserOnServer(newUser: NewUser): RemoteUser? {
        try {
            val signUpResponse = userService.signUp(newUser)

            if (!signUpResponse.isSuccessful) {
                Log.w("SignUpUseCases", signUpResponse.message() ?: "Could not create user on server")

                return null
            }

            return signUpResponse.body()
        } catch (exc: Exception) {
            Log.w("SignUpUseCases", exc.message ?: "Could not create user on server")

            return null
        }
    }

    suspend fun createUserOnDevice(newUser: NewUser, remoteUser: RemoteUser? = null): SignUpError? {
        val userFromDb = userDAO.isExistsByEmailOrUsername(newUser.email, newUser.username)

        if (userFromDb != null) {
            if (userFromDb.username == newUser.username) {
                return SignUpError.USERNAME_ALREADY_USED
            }

            if (userFromDb.email == newUser.email) {
                return SignUpError.EMAIL_ALREADY_USED
            }

            return SignUpError.UNKNOWN
        }

        userDAO.insertOne(newUser.copy(
            password = hasher.hash(newUser.password)!!,
        ).toEntity(remoteUser?.id))

        return null
    }
}

enum class SignUpError {
    UNKNOWN, USERNAME_ALREADY_USED, EMAIL_ALREADY_USED
}