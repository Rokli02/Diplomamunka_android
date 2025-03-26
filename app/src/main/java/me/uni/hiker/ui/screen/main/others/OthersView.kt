package me.uni.hiker.ui.screen.main.others

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.uni.hiker.R
import me.uni.hiker.model.user.User
import me.uni.hiker.ui.component.CustomIcon
import me.uni.hiker.ui.component.MenuItem
import me.uni.hiker.ui.layout.BasicLayout
import me.uni.hiker.ui.layout.component.TopBarIcon
import me.uni.hiker.ui.layout.component.TopBarTitle
import me.uni.hiker.ui.provider.NavigationProvider
import me.uni.hiker.ui.provider.SnackbarProvider
import me.uni.hiker.ui.theme.AppTheme
import me.uni.hiker.ui.theme.Blue
import me.uni.hiker.ui.theme.HikeRTheme
import me.uni.hiker.ui.theme.specifiedCustomColors
import java.time.LocalDate

@Composable
fun OthersView(
    isLoggedIn: Boolean,
    user: User?,
    logoutUser: () -> Unit,
    loginUser: () -> Unit,
) {
    val context = LocalContext.current

    val topBarIcons = remember (isLoggedIn) {
        val result = mutableListOf<TopBarIcon>()

        if (isLoggedIn) {
            result.add(
                TopBarIcon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    description = context.getString(R.string.logout),
                    onClick = logoutUser
                )
            )
        }

        result
    }

    BasicLayout (
        topBarTitle = TopBarTitle(
            context.getString(R.string.settings_page),
            imageVector = Icons.Default.Settings
        ),
        topBarIcons = topBarIcons,
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            if (isLoggedIn) {
                ProfileMenuItem(user = user!!, logoutUser = logoutUser)
            } else {
                MenuItem(
                    modifier = Modifier.padding(bottom = 6.dp),
                    icon = Icons.Default.AccountCircle,
                    text = context.getString(R.string.login),
                    onClick = loginUser
                )
            }

            HorizontalDivider(
                color = AppTheme.colors.separator
            )
        }
    }
}

@Preview
@Composable
private fun OthersViewPreview() {
    val user = User(
        id = 1L,
        name = "Tester",
        email = "ahha@freemail.hu",
        username = "test1",
        token = null,
        createdAt = LocalDate.now(),
    )

    HikeRTheme {
        SnackbarProvider{
            NavigationProvider {
                OthersView(
                    isLoggedIn = false,
                    user = user,
                    loginUser = {},
                    logoutUser = {},
                )
            }
        }
    }
}

@Composable
fun ProfileMenuItem(
    modifier: Modifier = Modifier,
    user: User,
    logoutUser: () -> Unit
) {
    val context = LocalContext.current

    Row (
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .padding(start = 8.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        CustomIcon(
            modifier = Modifier.size(44.dp),
            icon = Icons.Filled.Person,
            tint = AppTheme.colors.onBackground,
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column (
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = user.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.W500,
                color = AppTheme.colors.onBackground,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
            Text(
                text = user.username,
                fontSize = 16.sp,
                color = AppTheme.colors.onBackground,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }

        Button(
            onClick = logoutUser,
            modifier = Modifier.height(36.dp),
            colors = ButtonColors(
                containerColor = Color.Transparent,
                contentColor = AppTheme.colors.onBackgroundSecondary,
                disabledContainerColor = Blue.copy(.12f),
                disabledContentColor = specifiedCustomColors.disabled,
            )
        ) {
            Text(
                text = context.getString(R.string.logout),
                maxLines = 1,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.width(4.dp))
    }
}