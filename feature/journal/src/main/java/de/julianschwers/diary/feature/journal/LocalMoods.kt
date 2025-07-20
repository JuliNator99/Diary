package de.julianschwers.diary.feature.journal

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import de.julianschwers.diary.data.repository.MoodRepository

@get:Composable
val LocalMoods: ProvidableCompositionLocal<MoodRepository> get() = compositionLocalOf { MoodRepository() }