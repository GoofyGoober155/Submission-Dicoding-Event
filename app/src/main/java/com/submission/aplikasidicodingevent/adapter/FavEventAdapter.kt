package com.submission.aplikasidicodingevent.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.submission.aplikasidicodingevent.databinding.RvItemEventBinding
import com.submission.aplikasidicodingevent.database.FavoriteEvent

class FavEventAdapter(private val onItemClick: (FavoriteEvent) -> Unit) :
    ListAdapter<FavoriteEvent, FavEventAdapter.FavoriteViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = RvItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favoriteEvent = getItem(position)
        holder.bind(favoriteEvent, onItemClick)
    }

    class FavoriteViewHolder(private val binding: RvItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteEvent: FavoriteEvent, onItemClick: (FavoriteEvent) -> Unit) {
            binding.tvEventName.text = favoriteEvent.name
            binding.tvEventDetail.text = "Event Favorite"  // Atau keterangan tambahan

            Glide.with(binding.imageViewEvent.context)
                .load(favoriteEvent.mediaCover)
                .into(binding.imageViewEvent)

            binding.root.setOnClickListener {
                onItemClick(favoriteEvent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteEvent>() {
            override fun areItemsTheSame(oldItem: FavoriteEvent, newItem: FavoriteEvent): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FavoriteEvent, newItem: FavoriteEvent): Boolean {
                return oldItem == newItem
            }
        }
    }
}
