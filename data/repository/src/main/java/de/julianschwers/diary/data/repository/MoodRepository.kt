package de.julianschwers.diary.data.repository

import de.julianschwers.diary.core.model.Mood

class MoodRepository {
    fun getEmoji(mood: Mood): String =
        when (mood) {
            Mood.VERY_LOW  -> "D:"
            Mood.LOW       -> ":("
            Mood.MODERATE  -> ":|"
            Mood.HIGH      -> ":)"
            Mood.VERY_HIGH -> ":D"
        }
}