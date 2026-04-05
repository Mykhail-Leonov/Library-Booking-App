package com.example.librarybooking

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object BookingDate {

    private val storageFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val displayFormatter = DateTimeFormatter.ofPattern("EEEE dd/MM/yyyy", Locale.ENGLISH)

    fun getAvailableDates(count: Int = 5): List<LocalDate> {
        val dates = mutableListOf<LocalDate>()
        var current = LocalDate.now()

        while (dates.size < count) {
            if (current.dayOfWeek != DayOfWeek.SATURDAY &&
                current.dayOfWeek != DayOfWeek.SUNDAY
            ) {
                dates.add(current)
            }
            current = current.plusDays(1)
        }

        return dates
    }

    fun toStorage(date: LocalDate): String {
        return date.format(storageFormatter)
    }

    fun toDisplay(date: LocalDate): String {
        return date.format(displayFormatter)
    }
}