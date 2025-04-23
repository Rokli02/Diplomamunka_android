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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import dagger.hilt.android.AndroidEntryPoint
import me.uni.hiker.model.Profile
import me.uni.hiker.ui.layout.AuthLayout
import me.uni.hiker.ui.layout.BasicLayout
import me.uni.hiker.ui.layout.TopBarProps
import me.uni.hiker.ui.layout.component.TopBarIcon
import me.uni.hiker.ui.layout.component.TopBarTitle
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
import me.uni.hiker.ui.screen.main.sharedtrack.SharedTrackScreen
import me.uni.hiker.ui.screen.map.GoogleMapScreen
import me.uni.hiker.ui.theme.Black
import me.uni.hiker.ui.theme.BoneWhite
import me.uni.hiker.ui.theme.Green
import me.uni.hiker.ui.theme.HikeRTheme
import me.uni.hiker.utils.session.UserSessionViewModel
import me.uni.hiker.utils.session.getUserData
import me.uni.hiker.utils.session.getUserSharedPreferences
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var profile: Profile
    private lateinit var userSharedPreferences: SharedPreferences
    private lateinit var userSessionViewModel: UserSessionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Green.toArgb()),
            navigationBarStyle = SystemBarStyle.light(BoneWhite.toArgb(), Black.toArgb())
        )

        val context = this

        userSharedPreferences = getUserSharedPreferences()

        val usvm by viewModels<UserSessionViewModel>()
        userSessionViewModel = usvm
        userSessionViewModel.init(userSharedPreferences, profile)

        userSharedPreferences.getUserData()?.also { sessionUserData ->
            profile.setUser(sessionUserData)
            userSessionViewModel.user = sessionUserData
        }

        setContent {
            HikeRTheme {
                SnackbarProvider {
                    NavigationProvider {
                        UserSessionProvider(userSessionViewModel = userSessionViewModel) {
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
                                        val navController = LocalNavController

                                        AuthLayout(
                                            topBarProps = TopBarProps(
                                                title = context.getString(R.string.login),
                                                goBack = {
                                                    navController.popBackStack()
                                                }
                                            )
                                        ) {
                                            LoginScreen()
                                        }
                                    }

                                    composable<Screen.SignUp> {
                                        val navController = LocalNavController

                                        AuthLayout(
                                            topBarProps = TopBarProps(
                                                title = context.getString(R.string.signup),
                                                goBack = {
                                                    navController.popBackStack()
                                                }
                                            )
                                        ) {
                                            SignUpScreen()
                                        }
                                    }
                                }

                                composable<Screen.Home> {
                                    BasicLayout (
                                        topBarTitle = TopBarTitle(context.getString(R.string.home_page), Icons.Filled.Home),
                                    ) {
                                        HomeScreen()
                                    }
                                }

                                composable<Screen.Others> {
                                    val logoutPainter = painterResource(id = R.drawable.logout)
                                    val topBarIcons = remember (userSessionViewModel.isLoggedIn) {
                                        val result = mutableListOf<TopBarIcon>()

                                        if (userSessionViewModel.isLoggedIn) {
                                            result.add(
                                                TopBarIcon(
                                                    painter = logoutPainter,
                                                    description = context.getString(R.string.logout),
                                                    onClick = userSessionViewModel::logout,
                                                )
                                            )
                                        }

                                        result
                                    }

                                    BasicLayout (
                                        topBarTitle = TopBarTitle(context.getString(R.string.settings_page), imageVector = Icons.Default.Settings),
                                        topBarIcons = topBarIcons
                                    ) {
                                        OthersScreen()
                                    }
                                }

                                composable<Screen.LocalTrack> {
                                    BasicLayout (
                                        topBarTitle = TopBarTitle(context.getString(R.string.local_tracks), Icons.Filled.Place),
                                    ) {
                                        LocalTrackScreen()
                                    }
                                }

                                composable<Screen.SharedTrack> {
                                    BasicLayout (
                                        topBarTitle = TopBarTitle(context.getString(R.string.shared_tracks), Icons.Default.Share),
                                    ) {
                                        SharedTrackScreen()
                                    }
                                }

                                composable<Screen.GoogleMap>(
                                    enterTransition = { slideInVertically { -it } },
                                    exitTransition = { slideOutVertically { -it } },
                                    deepLinks = listOf(
                                        navDeepLink {
                                            uriPattern = Screen.RECORD_TRACK_URI
                                        },
                                        navDeepLink {
                                            uriPattern = Screen.TRACK_DETAILS_URI
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

        if (!userSessionViewModel.isInitialized()) {
            userSessionViewModel.init(userSharedPreferences, profile)
        }

        userSharedPreferences.getUserData()?.also { sessionUserData ->
            profile.setUser(sessionUserData)
            userSessionViewModel.user = sessionUserData
        }
    }
}