package me.uni.hiker.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Locale

object DateFormatter {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.GERMAN)
    private val dateTimeFormat = SimpleDateFormat("yyyy-MM-ddThh:mm:ss", Locale.GERMAN)

    fun format(date: LocalDate): String {
        return dateFormat.format(date)
    }

    fun format(date: String): LocalDate {
        val (year, month, day) = date.split("-").map { it.toInt() }

        return LocalDate.of(year, month - 1, day)
    }

    fun formatTime(dateTime: LocalDateTime): String {
        return dateTimeFormat.format(dateTime)
    }

    fun formatTime(dateTime: String): LocalDateTime {
        return dateTime.split("T").let { ti ->
            if (ti.size != 2) throw RuntimeException()

            val (year, month, day) = ti[0].split("-").map { it.toInt() }
            val (hour, min, sec) = ti[1].split(":").map { it.toInt() }

            return@let LocalDateTime.of(year, month, day, hour, min, sec)
        }
    }
}