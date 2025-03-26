package me.uni.hiker

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import dagger.hilt.android.AndroidEntryPoint
import me.uni.hiker.ui.provider.NavigationProvider
import me.uni.hiker.ui.provider.LocalNavController
import me.uni.hiker.ui.provider.SnackbarProvider
import me.uni.hiker.ui.provider.UserSessionProvider
import me.uni.hiker.ui.screen.Screen
import me.uni.hiker.ui.screen.auth.login.LoginScreen
import me.uni.hiker.ui.screen.auth.signup.SignUpScreen
import me.uni.hiker.ui.screen.main.home.HomeScreen
import me.uni.hiker.ui.screen.main.localtrack.LocalTrackScreen
import me.uni.hiker.ui.screen.main.others.OthersScreen
import me.uni.hiker.ui.screen.map.GoogleMapScreen
import me.uni.hiker.ui.theme.Black
import me.uni.hiker.ui.theme.BoneWhite
import me.uni.hiker.ui.theme.Green
import me.uni.hiker.ui.theme.HikeRTheme
import me.uni.hiker.utils.session.UserSessionViewModel
import me.uni.hiker.utils.session.getUserData
import me.uni.hiker.utils.session.getUserSharedPreferences

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var userSharedPreferences: SharedPreferences
    private lateinit var userSessionViewModel: UserSessionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Green.toArgb()),
            navigationBarStyle = SystemBarStyle.light(BoneWhite.toArgb(), Black.toArgb())
        )

        userSharedPreferences = getUserSharedPreferences()

        val usvm by viewModels<UserSessionViewModel>()
        userSessionViewModel = usvm
        userSessionViewModel.userSharedPreferences = userSharedPreferences

        userSharedPreferences.getUserData()?.also { sessionUserData ->
            userSessionViewModel.user = sessionUserData
        }

        setContent {
            HikeRTheme {
                NavigationProvider {
                    UserSessionProvider(userSessionViewModel = userSessionViewModel) {
                        SnackbarProvider {
                            NavHost(
                                navController = LocalNavController,
                                startDestination = Screen.Home,
                                enterTransition = { EnterTransition.None },
                                exitTransition = { ExitTransition.None }
                            ) {
                                navigation<Screen.Auth>(
                                    startDestination = Screen.Login,
                                ) {
                                    composable<Screen.Login> {
                                        LoginScreen()
                                    }
                                    composable<Screen.SignUp> {
                                        SignUpScreen()
                                    }
                                }

                                //TODO("A Main screeneknek bevezetni egy NavHost-ot, ami egységesen használja a BasicLayout-ot")

                                composable<Screen.Home> {
                                    HomeScreen()
                                }
                                composable<Screen.Others> {
                                    OthersScreen()
                                }
                                composable<Screen.LocalTrack> {
                                    LocalTrackScreen()
                                }
                                composable<Screen.MainMap>(
                                    enterTransition = { slideInVertically { -it } },
                                    exitTransition = { slideOutVertically { -it } },
                                    deepLinks = listOf(
                                        navDeepLink {
                                            uriPattern = "${Screen.BASE_URI}/map/record"
                                        },
                                    )
                                ) {
                                    GoogleMapScreen()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        userSharedPreferences.getUserData()?.also { sessionUserData ->
            userSessionViewModel.user = sessionUserData
        }
    }
}