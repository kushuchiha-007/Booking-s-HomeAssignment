package com.booking.tripsassignment.utils

import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

/*
 * Utility to format LocalDate instances into human-readable strings.
 * Uses Joda-Time for compatibility across API levels as suggested in web(wont matter if use Java.time as well).
 */
object DateFormatter {
    private const val PATTERN = "MMM dd,yyyy"
    private val formatter = DateTimeFormat.forPattern(PATTERN)

    /**
     * Formats a [LocalDate] as "MMM dd, yyyy", e.g. "Apr 01, 2025".
     */
    fun format(date:LocalDate): String = formatter.print(date)
}