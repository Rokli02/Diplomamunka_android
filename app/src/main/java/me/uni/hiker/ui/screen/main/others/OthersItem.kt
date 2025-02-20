package me.uni.hiker.ui.screen.main.others

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.uni.hiker.R
import me.uni.hiker.ui.theme.AppTheme
import me.uni.hiker.ui.theme.HikeRTheme

@Composable
fun OthersItem(
    text: String,
    icon: (Any)? = null,
    onClick: () -> Unit,
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp)
            .clickable(onClick = onClick)
            .padding(start = 8.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        if (icon == null) {
            Spacer(modifier = Modifier.width(32.dp))
        } else {
            when (icon) {
                is ImageVector -> {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = icon,
                        contentDescription = null,
                        tint = AppTheme.colors.onBackground,
                    )
                }
                is Painter -> {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        painter = icon,
                        contentDescription = null,
                        tint = AppTheme.colors.onBackground,
                    )
                }
                else -> {
                    Spacer(modifier = Modifier.width(32.dp))
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text,
            fontSize = 18.sp,
            color = AppTheme.colors.onBackground,
        )
    }
}

@Preview
@Composable
private fun OthersItemPreview() {
    HikeRTheme {
        OthersItem(
            text = "Others Item Preview",
        ) {

        }
    }
}