package me.uni.hiker.ui.screen.auth.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import me.uni.hiker.R
import me.uni.hiker.model.user.NewUser
import me.uni.hiker.ui.component.CustomInput
import me.uni.hiker.ui.component.Searchbar
import me.uni.hiker.ui.component.CustomPasswordInput
import me.uni.hiker.ui.theme.AcceptButtonColors
import me.uni.hiker.ui.theme.AppTheme
import me.uni.hiker.ui.theme.HikeRTheme

@Composable
fun SignUpView(
    newUser: NewUser,
    onChange: (NewUser) -> Unit,
    onSubmit: suspend () -> Unit,
    errors: SnapshotStateMap<String, String>,
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
            modifier = Modifier.padding(20.dp, 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            CustomInput(
                label = context.getString(R.string.name),
                name = "name",
                value = newUser.name,
                onValueChange = {
                    onChange(newUser.copy(name = it))
                },
                error = errors,
                required = true,
            )

            CustomInput(
                label = context.getString(R.string.username),
                name = "username",
                value = newUser.username,
                onValueChange = {
                    onChange(newUser.copy(username = it))
                },
                error = errors,
                required = true,
            )

            CustomInput(
                label = context.getString(R.string.email),
                value = newUser.email,
                name = "email",
                onValueChange = {
                    onChange(newUser.copy(email = it))
                },
                error = errors,
                required = true,
            )

            CustomPasswordInput(
                label = context.getString(R.string.password),
                name = "password",
                value = newUser.password,
                onValueChange = {
                    onChange(newUser.copy(password = it))
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
                    text = context.getString(R.string.signup),
                    modifier = Modifier.padding(horizontal = 8.dp),
                    fontSize = 17.sp,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview
@Composable
private fun SignUpViewPreview() {
    val error = remember { mutableStateMapOf("username" to "Required field") }

    HikeRTheme {
        SignUpView(
            newUser = NewUser(
                name = "Kis Tester",
                username = "",
                password = "123",
                email = "",
            ),
            onChange = {},
            onSubmit = {},
            errors = error,
        )
    }
}
