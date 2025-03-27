package me.uni.hiker.ui.screen.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.uni.hiker.R
import me.uni.hiker.model.track.Track
import me.uni.hiker.model.user.User
import me.uni.hiker.ui.component.CustomIcon
import me.uni.hiker.ui.component.TrackItem
import me.uni.hiker.ui.theme.AppTheme
import me.uni.hiker.ui.theme.CustomIconButtonColors

@Composable
fun Profile(
    user: User,
    navigateToMap: () -> Unit,
) {
    val context = LocalContext.current

    Row (
        modifier = Modifier
            .fillMaxWidth(.95f)
            .height(72.dp)
            .padding(start = 8.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Column (
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "${context.getString(R.string.welcome_prefix)}, ${user.name}!",
                fontSize = 18.sp,
                fontWeight = FontWeight.W500,
                color = AppTheme.colors.onBackground,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )

            Text(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = navigateToMap)
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                text = context.getString(R.string.welcome_suffix),
                fontSize = 16.sp,
                color = AppTheme.colors.link,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        CustomIcon(
            modifier = Modifier
                .padding(start = 6.dp)
                .size(58.dp)
                .padding(7.dp)
                .clip(RoundedCornerShape(12.dp)),
            icon = Icons.Filled.Person,
            tint = AppTheme.colors.onBackground,
        )
    }
}

@Composable
fun LoginAdviser(
    navigateToLogin: () -> Unit
) {
    val context = LocalContext.current

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .height(72.dp)
            .padding(start = 8.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(
            onClick = navigateToLogin,
            colors = CustomIconButtonColors,
        ) {
            CustomIcon(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp),
                icon = painterResource(R.drawable.login),
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            modifier = Modifier.weight(1f),
            text = context.getString(R.string.login_for_better_experience),
            fontSize = 16.sp,
            fontWeight = FontWeight.W400,
            color = AppTheme.colors.onBackground,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun LocalTracksSurface(
    localTracks: List<Track>,
    navigateToLocalTrack: () -> Unit,
) {
    val context = LocalContext.current

    Box(modifier = Modifier
        .padding(vertical = 12.dp)
        .fillMaxWidth(.96f)
        .shadow(elevation = 2.dp, shape = RoundedCornerShape(6.dp))) {
        Column (
            modifier = Modifier
                .padding(1.dp)
                .background(AppTheme.colors.background, RoundedCornerShape(6.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            for ((i, track) in localTracks.withIndex()) {
                TrackItem(track = track)

                if (localTracks.size - 1 != i) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 9.dp),
                        color = AppTheme.colors.onBackgroundSecondary.copy(alpha = .45f)
                    )
                }
            }

            Text(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(6.dp))
                    .clickable(onClick = navigateToLocalTrack)
                    .padding(horizontal = 8.dp, vertical = 3.dp),
                text = context.getString(R.string.more),
                color = AppTheme.colors.link,
                fontSize = 15.sp,
                fontWeight = FontWeight.W500,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}