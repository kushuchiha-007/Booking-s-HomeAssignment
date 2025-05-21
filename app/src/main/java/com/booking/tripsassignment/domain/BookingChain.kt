package com.booking.tripsassignment.domain

import org.joda.time.LocalDate
/**
 * Represents an ordered chain of one or more consecutive bookings.
 *
 * @property cities Distinct city names in visit order (no duplicates).
 * @property startDate Date of first check-in.
 * @property endDate Date of last check-out.
 * @property bookingCount Number of bookings in this chain.
 * @property imageUrl The URL to display for this trip (first booking's image).
 */
data class BookingChain(
    val cities: List<String>,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val bookingCount: Int,
    val imageUrls: List<String>
)