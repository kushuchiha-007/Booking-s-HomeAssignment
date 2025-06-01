package com.booking.tripsassignment.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.booking.tripsassignment.databinding.TripCardItemLayoutBinding
import com.booking.tripsassignment.domain.BookingChain
import com.booking.tripsassignment.utils.DateFormatter
import com.booking.tripsassignment.utils.ImageLoader

/**
 * RecyclerView.Adapter to display a list of [BookingChain] items.
 * Each chain shows the cities, date range, booking count, and an image.
 */
class ChainAdapter: ListAdapter<BookingChain,ChainAdapter.ViewHolder>(DiffCallBack()) {

     class DiffCallBack:DiffUtil.ItemCallback<BookingChain>() {
        override fun areItemsTheSame(oldItem: BookingChain, newItem: BookingChain): Boolean {
            // need to compare any unique_identifier that we have of this Chain, since there's none
            // comparing two of the attributes in which i feel duplicacy wont be there
            return oldItem.cities == newItem.cities && oldItem.startDate == newItem.startDate
        }

        override fun areContentsTheSame(oldItem: BookingChain, newItem: BookingChain): Boolean {
            return oldItem == newItem
        }

    }

    inner class ViewHolder(private val binding: TripCardItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chain: BookingChain) {
            // Format "Trip to City1 - City2"
            ("Trip to " + chain.cities.joinToString(separator = " - " )).also { binding.cities.text = it }

            // Format dates: "MMM d, yyyy - MMM d, yyyy"
            "${DateFormatter.format(chain.startDate)} - ${DateFormatter.format(chain.endDate)}".also { binding.dates.text = it }

            // Show number of bookings
            "${chain.bookingCount} Bookings".also { binding.nights.text = it }

            // Load the chain image (first hotel photo from chain which returns an Image)
            ImageLoader.loadImage(binding.tripImage, chain.imageUrls)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TripCardItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}