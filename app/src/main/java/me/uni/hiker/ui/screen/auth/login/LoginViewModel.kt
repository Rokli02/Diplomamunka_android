package me.uni.hiker.ui.screen.auth.login

import android.content.Context
import android.net.ConnectivityManager
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
import me.uni.hiker.model.user.Login
import me.uni.hiker.model.user.User
import me.uni.hiker.service.ConnectionService
import me.uni.hiker.utils.encrypter.Hasher
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userDAO: LocalUserDAO,
    private val hasher: Hasher,
): ViewModel() {
    private val _login = MutableStateFlow(Login("", ""))
    val login = _login.asStateFlow()

    private val errorChecker = ErrorChecker(context)
    val errors = errorChecker.errors

    fun changeLoginDetails(login: Login) { _login.update { login } }

    suspend fun onSubmit(): User? {
        errorChecker.clear()

        val login = login.value.copy()

        var areFieldsRight = !errorChecker.isFieldBlank(login.usernameOrEmail, "usernameOrEmail")

        areFieldsRight = !errorChecker.isFieldBlank(login.password, "password") &&
                errorChecker.lengthConstraintsMatch(login.password, "password", min = 8, max = 255) &&
                areFieldsRight

        println("During login areFieldsRight = $areFieldsRight")

        if (!areFieldsRight) { return null }

        // Próbáljon belépni lokálisan
        // Ha sikerült:
        //      - Próbáljon belépni a szerveren
        //        Ha nincs internet, belépett, de nincs token
        //        Ha van internet, de nem sikerült, akkor "Snackbar"-ba nem tudta a szerverhez hozzácsatolni a helyi fiókot, "action"-be ezt megteheti és átíranyít egy oldalra ahhol ez lehetséges
        //        Ha van internet és sikerült, kap tőle egy tokent
        // Ha nem sikerült:
        //      - Nézze meg szerveren létezik-e
        //        Ha nincs internet, sikertelen
        //        Ha van internet, de nincs ilyen fiók, sikertelen
        //        Ha van internet és van fiók, akkor kérje le a fiók adatait és regisztrálja be lokálisan

        val hasNetworkConnection = ConnectionService.hasConnection(context)
        val userFromDb = userDAO.findByEmailOrUsername(login.usernameOrEmail, true)

        if (userFromDb != null) {
            Log.d("LoginViewModel", "userFromDb.password = (${userFromDb.password})")
            Log.d("LoginViewModel", "login.password = (${login.password})")

            if (!hasher.verify(login.password, userFromDb.password)) {
                errors["usernameOrEmail"] = context.getString(R.string.username_or_password_is_incorrect)
                errors["password"] = context.getString(R.string.username_or_password_is_incorrect)

                return null
            }

            val loggedInUser = User.fromEntity(userFromDb)

            if (!hasNetworkConnection) {
                return loggedInUser
            }

            //TODO: Ha van szerver, ezt törölni és további ellenőrzések
            return loggedInUser
        } else {
            if (!hasNetworkConnection) {
                errors["usernameOrEmail"] = context.getString(R.string.username_or_password_is_incorrect)
                errors["password"] = context.getString(R.string.username_or_password_is_incorrect)

                return null
            }

            // TODO: Ha van szerver további ellenőrzések
        }

        return null
    }

    fun clearStates() {
        errorChecker.clear()
        _login.update { Login("", "") }
    }
}