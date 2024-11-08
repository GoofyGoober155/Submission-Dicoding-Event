package com.submission.aplikasidicodingevent.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.submission.aplikasidicodingevent.databinding.RvItemEventBinding
import com.submission.aplikasidicodingevent.response.ListEventsItem

class EventAdapter(private val onItemClick: (ListEventsItem) -> Unit) : ListAdapter<ListEventsItem, EventAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RvItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, onItemClick)
    }

    class MyViewHolder(private val binding: RvItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem, onItemClick: (ListEventsItem) -> Unit) {
            binding.tvEventName.text = event.name
            binding.tvEventDetail.text = event.summary

            Glide.with(binding.imageViewEvent.context)
                .load(event.mediaCover)
                .into(binding.imageViewEvent)

            binding.root.setOnClickListener {
                onItemClick(event)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}