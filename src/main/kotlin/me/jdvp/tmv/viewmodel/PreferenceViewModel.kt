package me.jdvp.tmv.viewmodel

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import java.util.prefs.Preferences

class PreferenceViewModel(
    private val preferences: Preferences,
    private val isSystemDarkMode : Boolean
) {
    private val lightColors = lightColors(
        primary = Color(0xff1b87e5),
        primaryVariant = Color(0xff005bb2),
        secondary = Color(0xff9ccc65),
        secondaryVariant = Color(0xff7cb342),
        background = Color.White,
        surface = Color(0xffeeeeee)
    )

    private val darkColors = darkColors(
        primary = Color(0xff1b87e5),
        primaryVariant = Color(0xff005bb2),
        secondary = Color(0xff9ccc65),
        secondaryVariant = Color(0xff7cb342),
        background = Color.Black,
        surface = Color.Black
    )

    val colors = mutableStateOf(getUsedColors())

    private fun getUsedColors(): Colors {
        return when(getUserSelectedTheme()) {
            SELECTED_THEME_LIGHT -> lightColors
            SELECTED_THEME_DARK -> darkColors
            else -> when(isSystemDarkMode) {
                true -> darkColors
                false -> lightColors
            }
        }
    }

    private fun getUserSelectedTheme(): String? {
        return preferences.get(USER_SELECTED_THEME, null)
    }

    fun toggleTheme() {
        if (colors.value == lightColors) {
            preferences.put(USER_SELECTED_THEME, SELECTED_THEME_DARK)
            colors.value = darkColors
        } else {
            preferences.put(USER_SELECTED_THEME, SELECTED_THEME_LIGHT)
            colors.value = lightColors
        }
    }

    companion object {
        private const val USER_SELECTED_THEME = "USER_SELECTED_THEME"
        private const val SELECTED_THEME_LIGHT = "SELECTED_THEME_LIGHT"
        private const val SELECTED_THEME_DARK = "SELECTED_THEME_DARK"
    }
}