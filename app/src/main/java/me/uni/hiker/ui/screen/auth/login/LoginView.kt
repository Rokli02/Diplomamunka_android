package me.uni.hiker.ui.screen.auth.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import me.uni.hiker.R
import me.uni.hiker.model.user.Login
import me.uni.hiker.ui.component.CustomInput
import me.uni.hiker.ui.component.CustomPasswordInput
import me.uni.hiker.ui.theme.AcceptButtonColors
import me.uni.hiker.ui.theme.AppTheme
import me.uni.hiker.ui.theme.HikeRTheme

@Composable
fun LoginView(
    login: Login,
    onChange: (Login) -> Unit,
    onSubmit: suspend () -> Unit,
    onSignUp: () -> Unit,
    errors: SnapshotStateMap<String, String>
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var loading by remember { mutableStateOf(false) }

    Surface (
        modifier = Modifier.fillMaxSize(),
        color = AppTheme.colors.background,
        contentColor = AppTheme.colors.onBackground,
    ) {
        Column (
            modifier = Modifier
                .padding(20.dp, 12.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            CustomInput(
                label = context.getString(R.string.username_or_email),
                value = login.usernameOrEmail,
                name = "usernameOrEmail",
                onValueChange = {
                    onChange(login.copy( usernameOrEmail = it ))
                },
                error = errors,
                required = true,
            )

            CustomPasswordInput(
                label = context.getString(R.string.password),
                value = login.password,
                name = "password",
                onValueChange = {
                    onChange(login.copy( password = it ))
                },
                error = errors,
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    loading = true

                    coroutineScope.launch {
                        onSubmit().also {
                            loading = false
                        }

                    }
                },
                colors = AcceptButtonColors,
                enabled = !loading
            ) {
                Text(
                    text = context.getString(R.string.login),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    fontSize = 17.sp,
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onSignUp),
                text = context.getString(R.string.no_profile_create_one),
                fontSize = 15.sp,
                fontWeight = FontWeight.W400,
                color = AppTheme.colors.link,
            )

            Spacer(modifier = Modifier.height(24.dp))

        }
    }
}

@Preview
@Composable
private fun LoginViewPreview() {
    val loginError = remember {
        mutableStateMapOf(
            "password" to "Nincs titkosítva"
        )
    }
    HikeRTheme {
        LoginView(
            login = Login("", "passw"),
            onChange = {},
            onSubmit = {},
            onSignUp = {},
            errors = loginError,
        )
    }
}