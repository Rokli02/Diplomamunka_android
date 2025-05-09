package me.uni.hiker.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.uni.hiker.R
import me.uni.hiker.model.track.Track
import me.uni.hiker.ui.theme.AppTheme
import me.uni.hiker.ui.theme.HikeRTheme
import java.math.RoundingMode
import java.time.LocalDate

@Composable
fun TrackItem(
    modifier: Modifier = Modifier,
    track: Track,
    onClick: () -> Unit = {}
) {
    Box(modifier = modifier.height(72.dp).clickable(onClick = onClick)) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 12.dp, top = 10.dp, end = 12.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = track.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.W500,
                color = AppTheme.colors.onBackground
            )

            Row {
                Text(
                    text = "${track.length.toInt()} ${stringResource(id = R.string.meter)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W400,
                    color = AppTheme.colors.disabled,
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "${
                        track.lat.toBigDecimal().setScale(4, RoundingMode.HALF_UP)
                    }, ${
                        track.lon.toBigDecimal().setScale(4, RoundingMode.HALF_UP)
                    }",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W400,
                    textAlign = TextAlign.Start,
                    color = AppTheme.colors.disabled,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TrackItemPreview() {
    HikeRTheme {
        TrackItem(
            track = Track(
                id = 0L,
                remoteId = null,
                name = "Test Track",
                length = 200f,
                lat = 27.4000,
                lon = 47.7000,
                createdAt = LocalDate.now(),
                updatedAt = LocalDate.now(),
            ),
            onClick = {}
        )
    }
}