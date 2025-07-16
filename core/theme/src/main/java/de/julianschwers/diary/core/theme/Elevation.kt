package de.julianschwers.diary.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val Elevation.dialog get() = high
val Elevation.card get() = low

data class Elevation(
    val extraLow: Dp = 1.dp,
    val low: Dp = 2.dp,
    val medium: Dp = 4.dp,
    val high: Dp = 8.dp,
    val extraHigh: Dp = 16.dp,
)

val LocalElevation = compositionLocalOf { Elevation() }

@Suppress("UnusedReceiverParameter")
val MaterialTheme.elevation: Elevation
    @Composable get() = LocalElevation.current