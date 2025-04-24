package me.uni.hiker.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

object DateFormatter {
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.GERMAN)
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.GERMAN)

    fun formatDate(date: LocalDate): String {
        return date.format(dateFormatter)
    }

    fun formatDate(date: String): LocalDate {
        return LocalDate.parse(date, dateFormatter)
    }

    fun formatTime(dateTime: LocalDateTime): String {
        return dateTime.format(dateTimeFormatter)
    }

    fun formatTime(dateTime: String): LocalDateTime {
        return LocalDateTime.from(dateTimeFormatter.parse(dateTime))
    }
}

fun Date.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this.time)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}