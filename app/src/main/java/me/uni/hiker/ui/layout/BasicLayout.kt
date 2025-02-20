package me.uni.hiker.ui.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.uni.hiker.R
import me.uni.hiker.ui.layout.component.BottomBar
import me.uni.hiker.ui.layout.component.TopBar
import me.uni.hiker.ui.layout.component.TopBarIcon
import me.uni.hiker.ui.layout.component.TopBarTitle
import me.uni.hiker.ui.theme.AppTheme
import me.uni.hiker.ui.theme.HikeRTheme

@Composable
fun BasicLayout(
    topBarTitle: TopBarTitle = TopBarTitle(""),
    topBarIcons: List<TopBarIcon> = listOf(),
    content: @Composable () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize().safeDrawingPadding(),
        topBar = { TopBar(title = topBarTitle, icons = topBarIcons) },
        bottomBar = { BottomBar() },
    ) { innerPadding ->
        Surface(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            color = AppTheme.colors.background,
            contentColor = AppTheme.colors.onBackground,
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BasicLayoutPreview() {
    HikeRTheme {
        BasicLayout(
            topBarTitle = TopBarTitle(stringResource(id = R.string.home_page), Icons.Filled.Home),
        ) {
            Box(modifier = Modifier.fillMaxSize().padding(top = 24.dp), contentAlignment = Alignment.TopCenter) {
                Text("Basic Layout Preview", fontSize = 24.sp)
            }
        }
    }
}
