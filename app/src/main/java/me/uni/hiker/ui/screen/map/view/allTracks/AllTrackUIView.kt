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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.uni.hiker.R
import me.uni.hiker.model.track.Track
import me.uni.hiker.ui.theme.AcceptButtonColors
import me.uni.hiker.ui.theme.AppTheme
import me.uni.hiker.ui.theme.DefaultButtonColors
import me.uni.hiker.ui.theme.HikeRTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllTrackUIView(
    focusedTrack: Track?,
    unfocusTrack: () -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState()

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
                    text = "${focusedTrack.length} ${stringResource(id = R.string.meter)}",
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
                        onClick = { /*TODO Továbbítson a details oldalra*/ }
                    ) {
                        Text(
                            text = stringResource(R.string.view),
                            fontSize = 19.sp,
                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 2.dp),
                        )
                    }

                    // TODO: Csak akkor jelenjen meg, ha be van jelentkezve a felhasználó
                    //       Ha van remote Id, akkor "Mentés", ha nincs, akkor "Megosztás"
                    //       Ha a tiéd a túra, akkor ne jelenjen meg a mentés gomb
                    if (!focusedTrack.isShared()) {
                        Button(
                            colors = DefaultButtonColors,
                            onClick = { /*TODO Megosztani a túrát másokkal */ }
                        ) {
                            Text(
                                text = stringResource(R.string.share),
                                fontSize = 19.sp,
                                modifier = Modifier.padding(vertical = 6.dp, horizontal = 2.dp),
                            )
                        }

                    } else /* if (I'm not the owner)*/ {
                        Button(
                            colors = AcceptButtonColors,
                            onClick = { /*TODO A felhasználónak menteni a túrát */ }
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
            unfocusTrack = {}
        )
    }
}