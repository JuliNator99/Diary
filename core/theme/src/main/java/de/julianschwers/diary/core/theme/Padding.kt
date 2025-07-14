package de.julianschwers.diary.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Padding(
    val extraExtraSmall: Dp = 2.dp,
    val extraSmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,
)

val LocalPadding = compositionLocalOf { Padding() }

@Suppress("UnusedReceiverParameter")
val MaterialTheme.padding: Padding
    @Composable get() = LocalPadding.current