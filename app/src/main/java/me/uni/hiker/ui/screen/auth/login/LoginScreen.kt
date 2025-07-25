package me.uni.hiker.ui.screen.auth.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import me.uni.hiker.R
import me.uni.hiker.ui.provider.LocalNavController
import me.uni.hiker.ui.provider.LocalSnackbarContext
import me.uni.hiker.ui.provider.UserContext
import me.uni.hiker.ui.screen.Screen

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val userContext = UserContext
    val navController = LocalNavController
    val snacbarContext = LocalSnackbarContext
    val login by loginViewModel.login.collectAsState()

    val submit = suspend {
        val user = loginViewModel.onSubmit()
        if (user == null) {
            snacbarContext.showSnackbar(
                message = context.getString(R.string.unsuccessful_login)
            )
        } else {
            snacbarContext.showSnackbar(
                message = context.getString(R.string.successful_login)
            )
            userContext.login(user)

            if (!navController.popBackStack(Screen.Home, inclusive = false))
                navController.navigate(Screen.Home)
        }

    }


    LoginView(
        login = login,
        onChange = loginViewModel::changeLoginDetails,
        onSubmit = submit,
        onSignUp = {
            loginViewModel.clearStates()
            navController.navigate(Screen.SignUp)
        },
        errors = loginViewModel.errors,
    )
}