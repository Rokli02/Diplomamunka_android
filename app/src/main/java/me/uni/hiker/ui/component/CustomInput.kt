package me.uni.hiker.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.uni.hiker.R
import me.uni.hiker.ui.theme.AppTheme
import me.uni.hiker.ui.theme.CustomIconButtonColors
import me.uni.hiker.ui.theme.CustomTextFieldColors
import me.uni.hiker.ui.theme.HikeRTheme


@Composable
fun CustomInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    name: String,
    error: SnapshotStateMap<String, String>,
    required: Boolean = false,
) {
    TextField(
        label = { Text(if (required) "*$label" else label) },
        value = value,
        onValueChange = {
            onValueChange(it)
            if (error[name] != null) error.remove(name)
        },
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            fontSize = 17.sp,
        ),
        colors = CustomTextFieldColors,
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
    )

    error[name]?.also {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp),
            text = it,
            color = AppTheme.colors.error,
            fontSize = 15.sp,
            fontWeight = FontWeight.W400,
        )
    } ?: Spacer(modifier = Modifier.height(12.dp))

    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun CustomPasswordInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    name: String,
    error: SnapshotStateMap<String, String>,
    initVisibility: Boolean = false,
) {
    var isVisible by remember { mutableStateOf(initVisibility) }

    TextField(
        label = { Text("*$label") },
        value = value,
        onValueChange = {
            onValueChange(it)
            if (error[name] != null) error.remove(name)
        },
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            fontSize = 17.sp,
        ),
        visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
        colors = CustomTextFieldColors,
        trailingIcon = {
            IconButton(
                onClick = { isVisible = !isVisible },
                colors = CustomIconButtonColors,
            ) {
                if (isVisible) {
                    Icon(painter = painterResource(id = R.drawable.visibility), contentDescription = "shown")
                } else {
                    Icon(painter = painterResource(id = R.drawable.visibility_off), contentDescription = "hidden")
                }
            }
        },
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
    )

    error[name]?.also {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp),
            text = it,
            color = AppTheme.colors.error,
            fontSize = 15.sp,
            fontWeight = FontWeight.W400,
        )
    } ?: Spacer(modifier = Modifier.height(12.dp))

    Spacer(modifier = Modifier.height(12.dp))
}

@Preview(showBackground = true)
@Composable
private fun CustomPasswordInputPreview() {
    HikeRTheme {
        CustomPasswordInput(
            label = "Password",
            value = "123",
            onValueChange = {},
            name = "password",
            error = remember { mutableStateMapOf() },
            initVisibility = true
        )
    }
}