package com.booking.tripsassignment.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.booking.tripsassignment.databinding.TripsHeaderItemLayoutBinding

/**
 * Simple adapter that displays a single section header title.
 */
class SectionHeaderAdapter(
    private val title: String
): RecyclerView.Adapter<SectionHeaderAdapter.ViewHolder>(){


    inner class ViewHolder(private val binding:TripsHeaderItemLayoutBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.tripsHeader.text = title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TripsHeaderItemLayoutBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }
}