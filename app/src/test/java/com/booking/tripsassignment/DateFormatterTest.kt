package com.booking.tripsassignment

import com.booking.tripsassignment.utils.DateFormatter
import org.joda.time.LocalDate
import org.junit.Test
import kotlin.test.assertEquals

class DateFormatterTest {

    @Test
    fun `testing date formatter`() {
        // 2025-01-05 → "Jan 05,2025"
        val date = LocalDate.parse("2025-01-05")
        val formatted = DateFormatter.format(date)
        assertEquals("Jan 05,2025", formatted)
    }
}