package com.booking.tripsassignment

import com.booking.tripsassignment.data.Booking
import com.booking.tripsassignment.data.Hotel
import com.booking.tripsassignment.data.Price
import com.booking.tripsassignment.domain.ChainCalculator
import org.joda.time.LocalDate
import org.junit.Assert.assertEquals
import java.math.BigDecimal
import kotlin.test.Test

class ChainCalculatorTest {
    private val calculator = ChainCalculator()

    private val booking1 = Booking(
        id = "1",
        hotel = Hotel(
            id = "hotel1",
            name = "Taj",
            mainPhoto = "dummy.jpeg",
            cityId = 123,
            cityName = "London"
        ),
        checkout = LocalDate.parse("2025-01-12"),
        checkin = LocalDate.parse("2025-01-10"),
        price = Price(
            currency = "INR",
            amount = BigDecimal(10000),
            scale = 100,
        )
    )

    val booking2 = Booking(
        id = "2",
        hotel = Hotel(
            id = "hotel2",
            name = "Taj",
            mainPhoto = "dummy.jpeg",
            cityId = 125,
            cityName = "Paris"
        ),
        checkout = LocalDate.parse("2025-01-15"),
        checkin = LocalDate.parse("2025-01-12"),
        price = Price(
            currency = "INR",
            amount = BigDecimal(10000),
            scale = 100,
        )
    )

    val booking3 = Booking(
        id = "3",
        hotel = Hotel(
            id = "hotel3",
            name = "Hilton",
            mainPhoto = "dummy.jpeg",
            cityId = 12233,
            cityName = "Mumbai"
        ),
        checkout = LocalDate.parse("2025-02-15"),
        checkin = LocalDate.parse("2025-02-15"),
        price = Price(
            currency = "INR",
            amount = BigDecimal(10000),
            scale = 100,
        )
    )

    @Test
    fun `single booking gets single chain`() {
        val chain = calculator.calculateChains(listOf(booking1))
        assertEquals(listOf("London"), chain[0].cities)
        assertEquals(1,chain.size)
    }

    @Test
    fun `consecutive booking gets single chain`() {
        val chain = calculator.calculateChains(listOf(booking1,booking2))
        assertEquals(LocalDate.parse("2025-01-15"), chain[0].endDate)
        assertEquals(1,chain.size)
    }

    @Test
    fun `non consecutive booking gets multiple chain`() {
        val chain = calculator.calculateChains(listOf(booking1,booking3))
        assertEquals(2,chain.size)
    }


}