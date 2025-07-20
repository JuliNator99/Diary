package de.julianschwers.diary.data.repository

import androidx.annotation.StringRes
import de.julianschwers.diary.core.model.Mood

class MoodRepository {
    @StringRes
    fun getName(mood: Mood): Int = when (mood) {
        Mood.VERY_LOW  -> R.string.mood_name_verylow
        Mood.LOW       -> R.string.mood_name_low
        Mood.MODERATE  -> R.string.mood_name_moderate
        Mood.HIGH      -> R.string.mood_name_high
        Mood.VERY_HIGH -> R.string.mood_name_veryhigh
    }
    
    fun getEmoji(mood: Mood): String = when (mood) {
        Mood.VERY_LOW  -> "D:"
        Mood.LOW       -> ":("
        Mood.MODERATE  -> ":|"
        Mood.HIGH      -> ":)"
        Mood.VERY_HIGH -> ":D"
    }
    
}