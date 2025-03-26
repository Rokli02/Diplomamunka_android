package me.uni.hiker.ui.screen.auth.signup

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import me.uni.hiker.R
import me.uni.hiker.ui.provider.LocalNavController
import me.uni.hiker.ui.provider.LocalSnackbarContext
import me.uni.hiker.ui.screen.Screen

@SuppressLint("UnrememberedMutableState")
@Composable
fun SignUpScreen(
    signUpViewModel: SignUpViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val navController = LocalNavController
    val snackbarContext = LocalSnackbarContext
    val newUser by signUpViewModel.newUser.collectAsState()

    val onSubmit = suspend {
        val result = signUpViewModel.onSubmit()

        if (result == null) {
            snackbarContext.showSnackbar(context.getString(R.string.successful_sign_up))

            if (!navController.popBackStack(Screen.Login, inclusive = false)) navController.navigate(Screen.Login)
        } else {
            snackbarContext.showSnackbar(result)
        }
    }

    SignUpView(
        newUser = newUser,
        onChange = signUpViewModel::onChange,
        onSubmit = onSubmit,
        errors = signUpViewModel.errors,
    )
}