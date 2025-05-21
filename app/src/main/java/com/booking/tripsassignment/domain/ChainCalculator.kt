package com.booking.tripsassignment.domain

import com.booking.tripsassignment.data.Booking
import org.joda.time.LocalDate
/**
 * Splits a flat list of bookings into chronological chains.
 * Consecutive bookings are chained when checkout == next checkin.
 */
class ChainCalculator {
    fun calculateChains(bookings: List<Booking>): List<BookingChain> {
        if (bookings.isEmpty()) return emptyList()

        // Sort by check-in date
        val sorted = bookings.sortedBy { it.checkin }

        val chains = mutableListOf<MutableList<Booking>>()
        var currentChain = mutableListOf<Booking>()

        // Build chains by walking sorted bookings
        for (booking in sorted) {
            if (currentChain.isEmpty() ||
                currentChain.last().checkout == booking.checkin
            ) {
                currentChain.add(booking)
            } else {
                chains.add(currentChain)
                currentChain = mutableListOf(booking)
            }
        }
        // Add the final chain
        if (currentChain.isNotEmpty()) chains.add(currentChain)

        // Map each raw list of bookings to a BookingChain
        return chains.map { rawChain ->
            // Remove duplicate city names if user stays consecutive nights
            val distinctCities = rawChain.map { it.hotel.cityName }.distinct()

            BookingChain(
                cities       = distinctCities,
                startDate    = rawChain.first().checkin,
                endDate      = rawChain.last().checkout,
                bookingCount = rawChain.size,
                imageUrls    = rawChain.map { it.hotel.mainPhoto }.distinct()
            )
        }
    }
}
