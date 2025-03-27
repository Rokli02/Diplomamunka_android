package me.uni.hiker.ui.screen.main.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.uni.hiker.model.track.Track
import me.uni.hiker.model.user.User
import me.uni.hiker.ui.theme.HikeRTheme
import java.time.LocalDate

@Composable
fun HomeView(
    isLoggedIn: Boolean,
    user: User?,
    localTracks: List<Track>?,
    navigateToLocalTrack: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToMap: () -> Unit,
) {
    LazyColumn (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Spacer(modifier = Modifier.height(6.dp))

            if (isLoggedIn) {
                Profile(
                    user = user!!,
                    navigateToMap = navigateToMap,
                )
            } else {
                LoginAdviser(navigateToLogin)
            }
        }

        // Mentett, később végigjárandó túra

        // x Random telefonon tárolt túra
        if (localTracks == null) {
            //TODO("Skeleton itemek")
        } else {
            item {
                LocalTracksSurface(
                    localTracks = localTracks,
                    navigateToLocalTrack = navigateToLocalTrack,
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8FDEB)
@Composable
private fun HomeViewPreview() {
    HikeRTheme {
        HomeView(
            isLoggedIn = true,
            user = User(
                id = 0L,
                name = "Tester",
                username = "test1",
                email = "test@gmail.com",
                token = null,
                createdAt = LocalDate.now(),
            ),
            localTracks = listOf(
                Track(
                    id = 0L,
                    remoteId = null,
                    name = "Test Track",
                    length = 202.6f,
                    lat = 27.4000,
                    lon = 47.7000,
                    createdAt = LocalDate.now(),
                    updatedAt = LocalDate.now(),
                ),
                Track(
                    id = 1L,
                    remoteId = null,
                    name = "Test Track",
                    length = 200f,
                    lat = 27.4000,
                    lon = 47.7000,
                    createdAt = LocalDate.now(),
                    updatedAt = LocalDate.now(),
                ),
                Track(
                    id = 2L,
                    remoteId = null,
                    name = "Test Track",
                    length = 200f,
                    lat = 27.4000,
                    lon = 47.7000,
                    createdAt = LocalDate.now(),
                    updatedAt = LocalDate.now(),
                ),
            ),
            navigateToLogin = {},
            navigateToMap = {},
            navigateToLocalTrack = {},
        )
    }
}