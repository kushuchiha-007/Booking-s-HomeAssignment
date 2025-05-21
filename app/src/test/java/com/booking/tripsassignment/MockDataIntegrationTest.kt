package com.booking.tripsassignment

import com.booking.tripsassignment.domain.ChainCalculator
import com.booking.tripsassignment.repository.MockNetworkBookingRepository
import com.booking.tripsassignment.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@LooperMode(LooperMode.Mode.LEGACY)
class MockDataIntegrationTest {

    private val repo = MockNetworkBookingRepository()
    private val calculator = ChainCalculator()

    private val testIds = listOf(
        899848,    // 1 past + 1 future chain
        48098,     // multiple chains past & future
        8984747,   // mix of singleton bookings + chains
        5678923,   // all single-booking “chains”
        99999,     // empty list
        940940     // non-existent → should produce Error
    )

    @Test
    fun `all mock-data IDs should fetch and calculate without crashing`() = runBlocking(Dispatchers.IO) {
        for (id in testIds) {
            val result = repo.fetchBookings(id)

            when (id) {
                940940 -> {
                    // non-existent user → expect Error
                    assertTrue("Expected Result.Error for user $id", result is Result.Error)
                }

                99999 -> {
                    // empty list → Success(empty)
                    assertTrue(
                        "Expected Success(empty) for user $id",
                        result is Result.Success && (result as Result.Success).data.isEmpty()
                    )
                }

                else -> {
                    // everything else → Success(non-empty)
                    assertTrue("Expected Result.Success for user $id", result is Result.Success)

                    // now verify we can calculate at least a non-null chain list
                    val bookings = (result as Result.Success).data
                    val chains = calculator.calculateChains(bookings)
                    assertNotNull("calculateChains returned null for user $id", chains)
                }
            }
        }
    }
}
