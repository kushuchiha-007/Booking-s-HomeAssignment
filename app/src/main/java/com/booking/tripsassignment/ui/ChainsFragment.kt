package com.booking.tripsassignment.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.booking.tripsassignment.databinding.TripsListScreenBinding
import com.booking.tripsassignment.di.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * A [Fragment] that displays upcoming and past booking chains.
 * It observes [ChainsViewModel.state] and updates the UI accordingly.
 */
class ChainsFragment:Fragment(){
    private val TAG = "ChainsFragment"
    // Use a simple ViewModelFactory to supply our ViewModel
    private val viewModel: ChainsViewModel by viewModels { ViewModelFactory() }
    private var mBinding: TripsListScreenBinding? = null
    private val binding get() = mBinding!!
    // Default mock user ID (can be swapped via proper UI in future)
    private val defaultUserId = 7984567


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = TripsListScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.retryButton.setOnClickListener { viewModel.loadChains(defaultUserId) }

        // Needed this since flows are not lifecycle aware thus this ensures that collection happens only when
        // fragment's view is not destroyed without this chances of mem leak occurs, livedata is lifecycle aware as in it
        // is only triggered when when lifecycle is atleast started or resumed
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                when(state) {
                    is UiState.Loading -> {
                        Log.i(TAG,"Loading")
                        showLoading()
                    }
                    is UiState.Error -> {
                        Log.i(TAG,"Error")
                        showError(state.message)
                    }
                    is UiState.Success -> {
                        Log.i(TAG,"Success")
                        showSuccess()
                        val adapters = mutableListOf<RecyclerView.Adapter<*>>()
                        if(state.upcoming.isNotEmpty()) {
                            Log.i(TAG,"Future Data is present")
                            adapters += SectionHeaderAdapter("Upcoming Trips")
                            val chainAdapter = ChainAdapter()
                            chainAdapter.submitList(state.upcoming)
                            adapters += chainAdapter
                        }
                         if(state.past.isNotEmpty()) {
                            Log.i(TAG,"Past Data is present")
                            adapters += SectionHeaderAdapter("Past Trips")
                             val chainAdapter = ChainAdapter()
                             chainAdapter.submitList(state.past)
                             adapters += chainAdapter
                        }
                        recyclerView.adapter = ConcatAdapter(adapters)
                        // optimizes scrolling performance since height and width of items in recycler view doesn't change
                        // we can only use this bcz of above condition, recycler view skips layout recalculations due to this.
                        recyclerView.setHasFixedSize(true)
                    }
                }
            }
        }

        // Kicking off the first load
        viewModel.loadChains(defaultUserId)
    }

    // Important to remove binding to avoid memory leak
    override fun onDestroyView() {
       mBinding = null
        super.onDestroyView()
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerview.visibility = View.GONE
        binding.errorView.visibility = View.GONE
    }
    private fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.recyclerview.visibility = View.GONE
        binding.errorView.visibility = View.VISIBLE
        binding.errorText.text = message
    }
    private fun showSuccess() {
        binding.progressBar.visibility = View.GONE
        binding.errorView.visibility = View.GONE
        binding.recyclerview.visibility = View.VISIBLE
    }
}

// If user wants to refresh data then we can wrap recycler view inside swiperefreshlayout and then
// on its onclicklistener call viewmodel.loadchains(id)