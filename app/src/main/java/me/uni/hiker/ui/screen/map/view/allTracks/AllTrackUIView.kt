package me.uni.hiker.ui.screen.map.view.allTracks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import me.uni.hiker.R
import me.uni.hiker.model.track.Track
import me.uni.hiker.ui.theme.AcceptButtonColors
import me.uni.hiker.ui.theme.AppTheme
import me.uni.hiker.ui.theme.DefaultButtonColors
import me.uni.hiker.ui.theme.HikeRTheme
import java.math.RoundingMode
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllTrackUIView(
    focusedTrack: Track?,
    unfocusTrack: () -> Unit,
    goToDetails: (Track) -> Unit,
    onShareTrack: suspend (Track) -> Unit,
    onSaveTrack: suspend (Track) -> Unit,
    isLoggedIn: Boolean,
) {
    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(focusedTrack) {
        focusedTrack?.also { bottomSheetState.show() } ?: bottomSheetState.hide()
    }

    if (focusedTrack != null) {
        ModalBottomSheet(
            sheetState = bottomSheetState,
            onDismissRequest = unfocusTrack,
            containerColor = AppTheme.colors.surface,
            contentColor = AppTheme.colors.onSurface,
            scrimColor = Color.Transparent,
        ) {
            Column (
                modifier = Modifier
                    .fillMaxHeight(.33f)
                    .padding(20.dp, 10.dp)
            ) {
                Text(
                    text = focusedTrack.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W500,
                    maxLines = 2,
                    color = AppTheme.colors.onSurface,
                )
                Text(
                    text = "${focusedTrack.length.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN)} ${stringResource(id = R.string.meter)}",
                    fontSize = 19.sp,
                    maxLines = 1,
                    color = AppTheme.colors.onSurface,
                    modifier = Modifier.padding(top = 12.dp, start = 16.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Row (
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Button(
                        colors = AcceptButtonColors,
                        onClick = {
                            goToDetails(focusedTrack)
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.view),
                            fontSize = 19.sp,
                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 2.dp),
                        )
                    }

                    if (isLoggedIn) {
                        var isButtonLoading by remember { mutableStateOf(false) }

                        if (focusedTrack.remoteId == null) {
                            Button(
                                colors = DefaultButtonColors,
                                onClick = {
                                    isButtonLoading = true
                                    coroutineScope.launch {
                                        onShareTrack(focusedTrack).also {
                                            isButtonLoading = false
                                        }
                                    }
                                }
                            ) {
                                Text(
                                    text = stringResource(R.string.share),
                                    fontSize = 19.sp,
                                    modifier = Modifier.padding(vertical = 6.dp, horizontal = 2.dp),
                                )
                            }

                        } else if (focusedTrack.id == null) {
                            Button(
                                colors = AcceptButtonColors,
                                onClick = {
                                    isButtonLoading = true
                                    coroutineScope.launch {
                                        onSaveTrack(focusedTrack).also {
                                            isButtonLoading = false
                                        }
                                    }
                                }
                            ) {
                                Text(
                                    text = stringResource(R.string.save),
                                    fontSize = 19.sp,
                                    modifier = Modifier.padding(vertical = 6.dp, horizontal = 2.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AllTrackUIViewPreview() {
    HikeRTheme {
        AllTrackUIView(
            focusedTrack = Track(
                id = 0L,
                name = "Test track",
                lat = 47.0,
                lon = 21.1,
                length = 520f,
                createdAt = LocalDate.now(),
                updatedAt = LocalDate.now(),
                remoteId = null
            ),
            unfocusTrack = {},
            goToDetails = {},
            onShareTrack = {},
            onSaveTrack = {},
            isLoggedIn = true,
        )
    }
}