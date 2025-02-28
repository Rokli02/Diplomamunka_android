package me.uni.hiker.ui.layout.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.AccountCircle
import androidx.compose.material.icons.sharp.Build
import androidx.compose.material.icons.sharp.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.uni.hiker.ui.theme.AppTheme
import me.uni.hiker.ui.theme.CustomIconButtonColors
import me.uni.hiker.ui.theme.HikeRTheme

@Composable
fun TopBar(
    title: TopBarTitle,
    icons: List<TopBarIcon> = listOf(),
) {
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
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f, true)
                    .padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                if (title.imageVector != null) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = title.imageVector,
                        contentDescription = title.text
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }

                Text(
                    text = title.text,
                    color = AppTheme.colors.onBar,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            if (icons.isNotEmpty()) {
                Row (
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    icons.forEach { icon ->
                        IconButton(
                            modifier = Modifier.padding(horizontal = 2.dp).aspectRatio(1f),
                            onClick = icon.onClick,
                            enabled = icon.enabled,
                            colors = CustomIconButtonColors
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = icon.imageVector,
                                contentDescription = icon.description
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TopBarPreview() {
    HikeRTheme {
        TopBar(
            title = TopBarTitle("Teszt szoveg helye", Icons.Sharp.Favorite),
            icons = listOf(
                TopBarIcon(
                    imageVector = Icons.Sharp.AccountCircle,
                    description = "Account",
                    onClick = {}
                ),
                TopBarIcon(
                    imageVector = Icons.Sharp.Build,
                    description = "Fogaskerek",
                    onClick = {}
                ),
            )
        )
    }
}

@Immutable
data class TopBarIcon (
    val imageVector: ImageVector,
    val description: String,
    val onClick: () -> Unit,
    val enabled: Boolean = true,
)

@Immutable
data class TopBarTitle (
    val text: String,
    val imageVector: ImageVector? = null,
)