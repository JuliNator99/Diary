package de.julianschwers.diary.core.common
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atDate
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.offsetAt
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

import java.text.SimpleDateFormat
import java.time.chrono.Chronology
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit
import java.util.Date
import java.util.Locale
import kotlin.time.Clock
import kotlin.time.ComparableTimeMark
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.toJavaInstant
import kotlin.time.toKotlinInstant

@OptIn(ExperimentalTime::class)
fun Instant.truncatedTo(unit: TemporalUnit): Instant = toJavaInstant().truncatedTo(unit).toKotlinInstant()
@OptIn(ExperimentalTime::class)
fun Instant.truncatedToSeconds(): Instant = truncatedTo(ChronoUnit.SECONDS)
@OptIn(ExperimentalTime::class)
fun Instant.truncatedToMinutes(): Instant = truncatedTo(ChronoUnit.MINUTES)

@OptIn(ExperimentalTime::class)
fun Instant.getDisplayName(
    dateStyle: FormatStyle? = null,
    timeStyle: FormatStyle? = null,
    locale: Locale = Locale.getDefault(),
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): String {
    val pattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
        dateStyle,
        timeStyle,
        Chronology.ofLocale(locale),
        locale
    )
    val dateFormat = SimpleDateFormat(
        pattern,
        locale
    )
    
    val millisOffsetFromCurrentTimeZone = (TimeZone.currentSystemDefault().offsetAt(this).totalSeconds - timeZone.offsetAt(this).totalSeconds) * 1000
    val javaDate = Date(toEpochMilliseconds() - millisOffsetFromCurrentTimeZone)
    
    return dateFormat.format(javaDate)
}

@OptIn(ExperimentalTime::class)
fun Instant.atTimeIn(time: LocalTime, timeZone: TimeZone): Instant {
    val localDateTime = toLocalDateTime(timeZone)
    val atTime = localDateTime.date.atTime(time)
    return atTime.toInstant(timeZone)
}

@OptIn(ExperimentalTime::class)
fun Instant.atDateIn(date: LocalDate, timeZone: TimeZone): Instant {
    val localDateTime = toLocalDateTime(timeZone)
    val atDate = localDateTime.time.atDate(date)
    return atDate.toInstant(timeZone)
}


@OptIn(ExperimentalTime::class)
fun Instant.atStartOfDayIn(timeZone: TimeZone = TimeZone.currentSystemDefault()): Instant = toLocalDateTime(timeZone).date.atStartOfDayIn(timeZone)


@OptIn(ExperimentalTime::class)
fun Clock.nowAsFlow(
    interval: Duration = 250.milliseconds,
    provider: Clock.() -> Instant = { now() },
): Flow<Instant> = flow {
    while (true) {
        emit(provider())
        delay(interval)
    }
}

fun ComparableTimeMark.elapsedNowFlow(interval: Duration = 25.milliseconds): Flow<Duration> = flow {
    while (true) {
        emit(elapsedNow())
        delay(interval)
    }
}