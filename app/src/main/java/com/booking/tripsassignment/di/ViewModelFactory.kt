package com.booking.tripsassignment.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.booking.tripsassignment.ui.ChainsViewModel
/**
 * We need this factory so as to pass arguments to our viewModel, the generic viewModel() wont allow any arguments and
 * directly accessing ViewModel will behave like an obj and wont actually retain through changes in screen orientation
 * Used this instead of DI since its a simple assignment and didn't need to configure it with hilt
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
