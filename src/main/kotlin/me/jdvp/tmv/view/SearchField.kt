package me.jdvp.tmv.view

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun searchField(searchText: String, action: (String) -> Unit) {
    TextField(
        value = searchText,
        onValueChange = action,
        label = { Text("Search / Filter", modifier = Modifier.background(Color.Transparent)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        colors = object: TextFieldColors {
            @Composable
            override fun backgroundColor(enabled: Boolean) =
                mutableStateOf(MaterialTheme.colors.background)

            @Composable
            override fun cursorColor(isError: Boolean) =
                mutableStateOf(MaterialTheme.colors.primary)

            @Composable
            override fun indicatorColor(
                enabled: Boolean,
                isError: Boolean,
                interactionSource: InteractionSource
            ) = mutableStateOf(MaterialTheme.colors.primary)

            @Composable
            override fun labelColor(
                enabled: Boolean,
                error: Boolean,
                interactionSource: InteractionSource
            ) = mutableStateOf(MaterialTheme.colors.primary)

            @Composable
            override fun leadingIconColor(enabled: Boolean, isError: Boolean) =
                mutableStateOf(MaterialTheme.colors.primary)

            @Composable
            override fun placeholderColor(enabled: Boolean) =
                mutableStateOf(MaterialTheme.colors.onBackground)

            @Composable
            override fun textColor(enabled: Boolean) =
                mutableStateOf(MaterialTheme.colors.onBackground)

            @Composable
            override fun trailingIconColor(enabled: Boolean, isError: Boolean) =
                mutableStateOf(MaterialTheme.colors.primary)
        }
    )
}