package de.julianschwers.diary.core.util

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus

fun LocalDate.startOfWeek(): LocalDate = minus(DatePeriod(days = dayOfWeek.isoDayNumber - 1))