package com.booking.tripsassignment.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.booking.tripsassignment.data.Booking
import com.booking.tripsassignment.data.Hotel
import com.booking.tripsassignment.data.Price
import com.booking.tripsassignment.domain.BookingChain
import com.booking.tripsassignment.domain.ChainCalculator
import com.booking.tripsassignment.repository.BookingRepository
import com.booking.tripsassignment.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import org.junit.*
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class ChainsViewModelTest {
    // Use a single TestDispatcher for both Main and IO
    private val testDispatcher = StandardTestDispatcher()

    /**
     * Fake repository returns exactly the list passed in.
     */
    class FakeBookingRepository(private val bookings: List<Booking>) : BookingRepository {
        override fun fetchBookings(userId: Int) = Result.Success(bookings)

    }

    private lateinit var viewModel: ChainsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Helper: construct a Booking with minimal Hotel + Price.
     */
    private fun makeBooking(
        id: String,
        cityName: String,
        checkin: LocalDate,
        checkout: LocalDate
    ): Booking {
        val hotel = Hotel(
            id = "hotel-$id",
            name = "Hotel $cityName",
            mainPhoto = "https://example.com/$cityName.jpg",
            cityName = cityName,
            cityId = id.toInt() + 100
        )
        val price = Price(
            currency = "EUR",
            amount = java.math.BigDecimal(10000),
            scale = 100
        )
        return Booking(
            id = id,
            hotel = hotel,
            checkin = checkin,
            checkout = checkout,
            price = price
        )
    }

    @Test
    fun `loadChains partitions bookings into upcoming and past`() = runTest {
        // 1) Under UTCProvider, “today”:
        val today = LocalDate.now(DateTimeZone.UTC)

        // 2) Create one booking that checks out before today → goes to “past”
        val pastBooking = makeBooking(
            id = "1",
            cityName = "Athens",
            checkin = today.minusDays(3),
            checkout = today.minusDays(1)
        )
        // 3) Create one booking that checks out after today → goes to “upcoming”
        val upcomingBooking = makeBooking(
            id = "2",
            cityName = "Cairo",
            checkin = today.plusDays(1),
            checkout = today.plusDays(2)
        )

        // 4) Fake repo returns exactly those two bookings
        val fakeRepo = FakeBookingRepository(listOf(pastBooking, upcomingBooking))

        // 5) Create ViewModel with fakeRepo, real ChainCalculator, and override IO dispatcher
        viewModel = ChainsViewModel(
            repo = fakeRepo,
            calculator = ChainCalculator(),
            ioDispatcher = testDispatcher
        )

        // 6) Call loadChains(), then advance the testDispatcher until everything completes
        viewModel.loadChains(userId = 123)
        testDispatcher.scheduler.advanceUntilIdle()

        // 7) State must now be UiState.Success with one past chain ("Athens") and one upcoming chain ("Cairo")
        val state = viewModel.state.value
        assertTrue("Expected Success, got $state", state is UiState.Success)

        val success = state as UiState.Success
        val pastChains: List<BookingChain> = success.past
        val upcomingChains: List<BookingChain> = success.upcoming

        assertEquals(1, pastChains.size)
        assertEquals("Athens", pastChains[0].cities.single())

        assertEquals(1, upcomingChains.size)
        assertEquals("Cairo", upcomingChains[0].cities.single())
    }

    @Test
    fun `loadChains emits Error state when repository fails`() = runTest {
        // 1) Fake repo that always errors
        val fakeErrorRepo = object : BookingRepository {
            override fun fetchBookings(userId: Int) = Result.Error(Exception("Network down"))

        }

        viewModel = ChainsViewModel(
            repo = fakeErrorRepo,
            calculator = ChainCalculator(),
            ioDispatcher = testDispatcher
        )

        // 2) Call loadChains() and advance
        viewModel.loadChains(userId = 456)
        testDispatcher.scheduler.advanceUntilIdle()

        // 3) State must now be UiState.Error("Network down")
        val state = viewModel.state.value
        assertTrue("Expected Error, got $state", state is UiState.Error)
        assertEquals("Network down", (state as UiState.Error).message)
    }
}
