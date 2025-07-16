package de.julianschwers.diary.core.theme

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.WindowInsetsController
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode

private fun createLightColorScheme(
    context: Context,
    materialYou: Boolean,
): ColorScheme {
    val theme = when (materialYou) {
        true  -> dynamicLightColorScheme(context)
        false -> lightColorScheme()
    }
    return theme.copy(surface = theme.background.copy(alpha = 0.95f).compositeOver(theme.onBackground))
}

private fun createDarkColorScheme(
    context: Context,
    materialYou: Boolean,
    blackTheme: Boolean,
): ColorScheme {
    val generalTheme = when (materialYou) {
        true  -> dynamicDarkColorScheme(context)
        false -> darkColorScheme()
    }
    val theme = generalTheme.let { theme ->
        when (blackTheme) {
            false -> theme.copy(surface = theme.background.copy(alpha = 0.95f).compositeOver(theme.onBackground))
            true  -> theme.copy(background = Color.Black, surface = theme.background.copy(alpha = 0.8f).compositeOver(Color.Black))
        }
    }
    return theme
}

private fun createColorScheme(
    context: Context,
    materialYou: Boolean,
    darkTheme: Boolean,
    blackDarkTheme: Boolean,
): ColorScheme {
    val theme = when (darkTheme) {
        false -> createLightColorScheme(context = context, materialYou = materialYou)
        true  -> createDarkColorScheme(context = context, materialYou = materialYou, blackTheme = blackDarkTheme)
    }
    return theme
}

@SuppressLint("ContextCastToActivity")
@Composable
fun ThemeLayer(
    modifier: Modifier = Modifier,
    darkTheme: Boolean = isSystemInDarkTheme(),
    blackDarkTheme: Boolean = false,
    materialYou: Boolean = false,
    colorScheme: ColorScheme = createColorScheme(context = LocalContext.current, materialYou = materialYou, darkTheme = darkTheme, blackDarkTheme = blackDarkTheme),
    typography: Typography = Typography,
    shapes: Shapes = RoundedShapes,
    padding: Padding = Padding(),
    elevation: Elevation = Elevation(),
    content: @Composable () -> Unit,
) {
    if (!LocalInspectionMode.current) {
        val activity = LocalContext.current as Activity
        val window = activity.window
        window.insetsController?.setSystemBarsAppearance(
            if (!darkTheme) WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS else 0,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
        window.insetsController?.setSystemBarsAppearance(
            if (!darkTheme) WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS else 0,
            WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
        )
    }
    
    CompositionLocalProvider(
        LocalPadding provides padding,
        LocalElevation provides elevation,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            shapes = shapes
        ) {
            Surface(
                color = colorScheme.background,
                content = content,
                modifier = modifier
            )
        }
    }
}