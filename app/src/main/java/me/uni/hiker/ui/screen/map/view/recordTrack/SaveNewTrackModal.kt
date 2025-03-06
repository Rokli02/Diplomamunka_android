package me.uni.hiker.ui.screen.map.view.recordTrack

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import me.uni.hiker.R
import me.uni.hiker.ui.theme.AcceptButtonColors
import me.uni.hiker.ui.theme.CancelButtonColors
import me.uni.hiker.ui.theme.CustomCardColors
import me.uni.hiker.ui.theme.CustomTextFieldColors
import me.uni.hiker.ui.theme.HikeRTheme

@Composable
fun SaveNewTrackModal(
    onDismissRequest: () -> Unit,
    onSave: (String) -> Unit,
    onCancel: () -> Unit = onDismissRequest,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true,
            usePlatformDefaultWidth = true,
        )
    ) {
        DialogContent(
            onSave = onSave,
            onCancel = onCancel,
        )
    }
}

@Composable
private fun DialogContent(
    onSave: (String) -> Unit,
    onCancel: () -> Unit,
    nameArg: String = ""
) {
    var name by remember { mutableStateOf(nameArg) }
    val context = LocalContext.current

    Card (
        colors = CustomCardColors
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 12.dp)
        ) {
            Text(
                text = context.getString(R.string.save_new_track_modal_title),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.W500,
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 6.dp, bottom = 20.dp)
            )

            TextField(
                label = { Text(context.getString(R.string.name_of_track)) },
                value = name,
                onValueChange = { name = it },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 17.sp,
                ),
                colors = CustomTextFieldColors,
                modifier = Modifier.padding(bottom = 18.dp)
            )

            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                Button(
                    onClick = { onSave(name) },
                    colors = AcceptButtonColors,
                ) {
                    Text(context.getString(R.string.save))
                }
                Button(
                    onClick = onCancel,
                    colors = CancelButtonColors
                ) {
                    Text(context.getString(R.string.cancel))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DialogContentPreview() {
    HikeRTheme {
        DialogContent(
            onSave = {},
            onCancel = {},
        )
    }
}