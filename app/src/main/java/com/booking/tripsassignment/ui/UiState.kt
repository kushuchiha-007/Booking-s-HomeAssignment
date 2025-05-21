package com.booking.tripsassignment.ui

import com.booking.tripsassignment.domain.BookingChain

/**
 * Represents the different UI states for the chains screen.
 */
sealed class UiState {
    /** Loading in progress */
    data object Loading : UiState()

    /** Data loaded successfully */
    data class Success(
        val upcoming: List<BookingChain>,
        val past: List<BookingChain>
    ) : UiState()

    /** An error occurred */
    data class Error(val message: String) : UiState()
}
