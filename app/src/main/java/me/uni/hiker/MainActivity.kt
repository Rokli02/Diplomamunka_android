package me.uni.hiker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import me.uni.hiker.ui.provider.NavigationProvider
import me.uni.hiker.ui.provider.LocalNavController
import me.uni.hiker.ui.provider.SnackbarProvider
import me.uni.hiker.ui.screen.Screen
import me.uni.hiker.ui.screen.auth.login.LoginScreen
import me.uni.hiker.ui.screen.auth.signup.SignUpScreen
import me.uni.hiker.ui.screen.main.home.HomeScreen
import me.uni.hiker.ui.screen.main.others.OthersScreen
import me.uni.hiker.ui.screen.map.GoogleMapScreen
import me.uni.hiker.ui.screen.map.model.MapViewType
import me.uni.hiker.ui.theme.Black
import me.uni.hiker.ui.theme.BoneWhite
import me.uni.hiker.ui.theme.Green
import me.uni.hiker.ui.theme.HikeRTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Green.toArgb()),
            navigationBarStyle = SystemBarStyle.light(BoneWhite.toArgb(), Black.toArgb())
        )

        setContent {
            HikeRTheme {
                NavigationProvider {
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

                            composable<Screen.Home> {
                                HomeScreen()
                            }
                            composable<Screen.Others> {
                                OthersScreen()
                            }
                            composable<Screen.MainMap>(
                                enterTransition = { slideInVertically { -it } },
                                exitTransition = { slideOutVertically { -it } },
                                deepLinks = listOf(
                                    navDeepLink {
                                        uriPattern = "${Screen.BASE_URI}/map/{initialScreen}"
                                    },
                                )
                            ) { entry ->
                                val initialScreen = entry.toRoute<Screen.MainMap>().initialScreen.let { MapViewType.valueOf(it) }

                                GoogleMapScreen(initialScreen)
                            }
                        }
                    }
                }
            }
        }
    }
}