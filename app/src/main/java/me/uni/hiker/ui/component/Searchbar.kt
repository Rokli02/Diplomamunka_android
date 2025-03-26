package me.uni.hiker.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.uni.hiker.R
import me.uni.hiker.ui.theme.AppTheme
import me.uni.hiker.ui.theme.HikeRTheme
import me.uni.hiker.ui.theme.SearchbarColors

@Composable
fun Searchbar(
    modifier: Modifier = Modifier,
    filter: String,
    onFilterChange: (String) -> Unit,
) {
    val context = LocalContext.current

    Box(modifier = modifier
        .shadow(elevation = 4.dp, shape = RoundedCornerShape(4.dp))
    ) {
        OutlinedTextField(
            placeholder = { Text(
                text = context.getString(R.string.searchbar_label)
            )},
            value = filter,
            onValueChange = {
                onFilterChange(it)
            },
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                fontSize = 16.sp,
                lineHeight = 16.sp,
            ),
            trailingIcon = { Icon(
                modifier = Modifier.size(28.dp),
                imageVector = Icons.Rounded.Search,
                contentDescription = "search",
            ) },
            colors = SearchbarColors,
            modifier = Modifier.fillMaxWidth().padding(2.dp),
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun SearchbarPreview() {
    HikeRTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(AppTheme.colors.background),
            contentAlignment = Alignment.Center,
        ) {
            Searchbar(
                filter = "vala hey ea eee eeee a",
                onFilterChange = {}
            )
        }
    }
}