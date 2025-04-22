package me.uni.hiker.ui.screen.auth.login

import android.util.Log
import me.uni.hiker.api.model.LoginResponse
import me.uni.hiker.api.model.RemoteUser
import me.uni.hiker.api.service.UserService
import me.uni.hiker.db.dao.LocalUserDAO
import me.uni.hiker.model.ErrorChecker
import me.uni.hiker.model.user.Login
import me.uni.hiker.model.user.NewUser
import me.uni.hiker.model.user.User
import me.uni.hiker.utils.encrypter.Hasher
import javax.inject.Inject

class LoginUseCases @Inject constructor(
    private val userDAO: LocalUserDAO,
    private val userService: UserService,
    private val hasher: Hasher,
) {
    fun validateField(login: Login, errorChecker: ErrorChecker): Boolean {
        var areFieldsRight = !errorChecker.isFieldBlank(login.usernameOrEmail, "usernameOrEmail")

        areFieldsRight = !errorChecker.isFieldBlank(login.password, "password") &&
                errorChecker.lengthConstraintsMatch(login.password, "password", min = 8, max = 255) &&
                areFieldsRight

        return areFieldsRight
    }

    suspend fun loginToServer(login: Login): Pair<LoginResponse?, LoginError?> {
        try {
            userService.login(login).run {
                if (isSuccessful) {
                    return Pair(body(), null)
                }

                if (code() != 400) {
                    return Pair(null, LoginError.UNKNOWN)
                }

                return when (this.errorBody()?.string()) {
                    "NOT_FOUND" -> Pair(null, LoginError.NOT_FOUND)
                    "INACTIVE_USER" -> Pair(null, LoginError.INACTIVE)
                    else -> Pair(null, LoginError.INVALID_INPUT)
                }
            }
        } catch (exc: Exception) {
            Log.w("LoginUseCases", exc.message ?: "Unknown error occurred")

            return Pair(null, LoginError.NO_SERVER)
        }
    }

    suspend fun loginLocally(login: Login): Pair<User?, LoginError?> {
        return userDAO.findByEmailOrUsername(login.usernameOrEmail, null).let {
            if (it == null) {
                return@let Pair(null, LoginError.NOT_FOUND)
            }

            if (!it.isActive) {
                return@let Pair(null, LoginError.INACTIVE)
            }

            if (!hasher.verify(login.password, it.password)) {
                return@let Pair(null, LoginError.NOT_FOUND)
            }

            return@let Pair(User.fromEntity(it), null)
        }
    }

    suspend fun signUpToServer(user: User, password: String): User {
        val newUser = NewUser(
            name = user.name,
            username = user.username,
            email = user.email,
            password = password,
        )

        try {
            val signUpResponse = userService.signUp(newUser)

            if (!signUpResponse.isSuccessful) {
                Log.w("LoginUseCases", signUpResponse.message())

                return user
            }

            val (response, error) = loginToServer(Login(user.username, password))
            if (error == null) {
                user.token = response!!.token
            }

            return user
        } catch (exc: Exception) {
            Log.w("LoginUseCases", exc.message ?: "Could not sign up user to server!")

            return user
        }
    }

    suspend fun signUpToDevice(user: RemoteUser, password: String): User? {
        val newUser = NewUser(
            name = user.name,
            username = user.username,
            email = user.email,
            password = hasher.hash(password)!!,
        )

        try {
            userDAO.insertOne(newUser.toEntity(user.id))

            val (dbUser, error) = loginLocally(Login(user.username, password))

            return if (error == null) dbUser else null
        } catch (exc: Exception) {
            Log.w("LoginUseCases", exc.message ?: "Could not sign up user on device!")

            return null
        }
    }
}

enum class LoginError {
    UNKNOWN, NOT_FOUND, INACTIVE, INVALID_INPUT, NO_SERVER
}