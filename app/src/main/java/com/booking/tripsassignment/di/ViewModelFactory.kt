package com.booking.tripsassignment.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.booking.tripsassignment.ui.ChainsViewModel
/**
 * Simple factory binding our ChainsViewModel so it can be created by the framework.
 * Add additional ViewModel types here as needed.
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ChainsViewModel::class.java) ->
                ChainsViewModel() as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
        }
    }
}
