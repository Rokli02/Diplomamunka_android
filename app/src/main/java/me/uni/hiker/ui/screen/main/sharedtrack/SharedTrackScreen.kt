package me.uni.hiker.ui.screen.main.sharedtrack

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import me.uni.hiker.R
import me.uni.hiker.ui.provider.UserContext
import me.uni.hiker.ui.screen.Screen

@Composable
fun SharedTrackScreen(sharedTrackViewModel: SharedTrackViewModel = hiltViewModel()) {
    val userContext = UserContext
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    if (!sharedTrackViewModel.isOnline && sharedTrackViewModel.trackList.isEmpty()) {
        Text(
            modifier = Modifier.fillMaxWidth(.95f).padding(top = 24.dp),
            text = context.getString(R.string.no_internet),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.W400,
        )

        return
    }

    if (!userContext.isLoggedIn) {
        Text(
            modifier = Modifier.fillMaxWidth(.95f).padding(top = 24.dp),
            text = context.getString(R.string.login_to_see_shared_tracks),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.W400,
        )

        return
    }

    val filter by sharedTrackViewModel.filterFlow.collectAsState()

    SharedTrackView(
        filter = filter,
        onFilterChange = sharedTrackViewModel::onFilterChange,
        tracks = sharedTrackViewModel.trackList,
        totalCount = sharedTrackViewModel.totalCount,
        onLoadMore = {
            coroutineScope.launch {
                sharedTrackViewModel.appendTrackList()
            }
        },
        onItemClick = {
            val trackDetailsIntent = Intent(
                Intent.ACTION_VIEW,
                "${Screen.BASE_URI}/map/details?remoteId=$it".toUri()
            )

            context.startActivity(trackDetailsIntent)
        },
    )
}