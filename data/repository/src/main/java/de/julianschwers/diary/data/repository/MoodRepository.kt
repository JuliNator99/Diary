package de.julianschwers.diary.data.repository

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import de.julianschwers.diary.core.model.Mood

class MoodRepository {
    @ColorRes
    fun getColor(mood: Mood): Int = when (mood) {
        Mood.VERY_LOW  -> R.color.mood_color_verylow
        Mood.LOW       -> R.color.mood_color_low
        Mood.MODERATE  -> R.color.mood_color_moderate
        Mood.HIGH      -> R.color.mood_color_high
        Mood.VERY_HIGH -> R.color.mood_color_veryhigh
    }
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