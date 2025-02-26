package me.uni.hiker.ui.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.uni.hiker.R
import me.uni.hiker.ui.theme.AppTheme
import me.uni.hiker.ui.theme.HikeRTheme

@Composable
fun AuthLayout(
    topBarProps: TopBarProps,
    content: @Composable () -> Unit,
) {
    Scaffold (
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        topBar = { TopBar(topBarProps) }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = AppTheme.colors.background,
            contentColor = AppTheme.colors.onBackground,
        ) {
            content()
        }
    }
}

@Immutable
data class TopBarProps(val title: String, val goBack: () -> Unit)

@Composable
private fun TopBar(props: TopBarProps) {
    Surface (
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp),
        color = AppTheme.colors.bar,
        contentColor = AppTheme.colors.onBar,
    ) {
        Row (
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp, 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(
                modifier = Modifier
                    .size(32.dp)
                    .padding(start = 4.dp),
                onClick = props.goBack
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go Back")
            }

            Text(
                modifier = Modifier.requiredWidth(IntrinsicSize.Max),
                text = stringResource(id = R.string.login),
                textAlign = TextAlign.Center,
                fontSize = 19.sp,
                fontWeight = FontWeight.W500,
            )

            Spacer(modifier = Modifier.width(36.dp))
        }
    }
}

@Preview
@Composable
private fun AuthLayoutPreview() {
    HikeRTheme {
        AuthLayout (
            topBarProps = TopBarProps(
                title = "Login",
                goBack = {},
            ),
        ) {
            Text("Auth Layout Preview")
        }
    }
}