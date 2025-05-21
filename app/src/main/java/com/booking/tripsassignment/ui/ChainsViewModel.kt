package com.booking.tripsassignment.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.booking.tripsassignment.domain.ChainCalculator
import com.booking.tripsassignment.repository.BookingRepository
import com.booking.tripsassignment.repository.MockNetworkBookingRepository
import com.booking.tripsassignment.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.joda.time.LocalDate

/*
 * ViewModel responsible for loading and partitioning booking chains.
 * Exposes a StateFlow<UiState> that the UI layer observes.
 */
class ChainsViewModel(
    private val repo:BookingRepository = MockNetworkBookingRepository(),
    private val calculator: ChainCalculator = ChainCalculator()
): ViewModel() {

    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state:StateFlow<UiState> = _state

     fun loadChains(userId: Int) {
        _state.value = UiState.Loading

        viewModelScope.launch {
            // Fetch on IO dispatcher as main thread wont return bookings from Repository
            val result = withContext(Dispatchers.IO) {
                repo.fetchBookings(userId)
            }

            when (result) {
                is Result.Success -> {
                    val chains = calculator.calculateChains(result.data)
                    val today = LocalDate.now()
                    // Partition into upcoming (endDate >= today) and past
                    val (upcoming, past) = chains.partition { it.endDate >= today }

                    _state.value = UiState.Success(
                        upcoming.sortedBy { it.endDate },
                        past.sortedByDescending { it.endDate }
                    )
                }
                is Result.Error -> {
                    _state.value = UiState.Error(result.exception.message ?: "Some Unknown error Occurred")
                }
            }
        }
    }


}