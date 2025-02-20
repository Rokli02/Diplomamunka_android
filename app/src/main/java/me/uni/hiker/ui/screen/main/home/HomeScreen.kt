package me.uni.hiker.ui.screen.main.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import me.uni.hiker.R
import me.uni.hiker.ui.layout.BasicLayout
import me.uni.hiker.ui.layout.component.TopBarTitle

@Composable
fun HomeScreen() {
    BasicLayout(
        topBarTitle = TopBarTitle(stringResource(id = R.string.home_page), Icons.Filled.Home),
    ) {
    }
}