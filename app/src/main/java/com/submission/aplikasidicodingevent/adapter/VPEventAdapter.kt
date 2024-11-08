package com.submission.aplikasidicodingevent.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.submission.aplikasidicodingevent.databinding.ItemUpcomingEventBinding
import com.submission.aplikasidicodingevent.response.ListEventsItem

class VPEventAdapter(private val onItemClick: (ListEventsItem) -> Unit) : RecyclerView.Adapter<VPEventAdapter.UpcomingEventViewHolder>() {

    private var eventList: List<ListEventsItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingEventViewHolder {
        // Inflate the layout using View Binding
        val binding = ItemUpcomingEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UpcomingEventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UpcomingEventViewHolder, position: Int) {
        val event = eventList[position]
        holder.bind(event, onItemClick)
    }

    override fun getItemCount(): Int = eventList.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(events: List<ListEventsItem>) {
        eventList = events
        notifyDataSetChanged()
    }

    class UpcomingEventViewHolder(private val binding: ItemUpcomingEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem, onItemClick: (ListEventsItem) -> Unit) {
            binding.eventTitle.text = event.name

            Glide.with(binding.root.context)
                .load(event.mediaCover)
                .into(binding.eventImage)

            binding.root.setOnClickListener {
                onItemClick(event)
            }
        }
    }
}

