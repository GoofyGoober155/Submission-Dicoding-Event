package com.submission.aplikasidicodingevent.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.submission.aplikasidicodingevent.R
import com.submission.aplikasidicodingevent.database.FavoriteEvent
import com.submission.aplikasidicodingevent.repository.FavoriteRepository
import com.submission.aplikasidicodingevent.response.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailEventActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var imageView: ImageView
    private lateinit var tvEventTitle: TextView
    private lateinit var tvOwnerName: TextView
    private lateinit var tvTimeEvent: TextView
    private lateinit var tvQuota: TextView
    private lateinit var tvEventDescription: TextView
    private lateinit var tvRegistrants: TextView
    private lateinit var tvCategory: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnRegister: Button
    private lateinit var eventLink: String

    // Floating Action Button for Favorites
    private lateinit var fabFavorite: FloatingActionButton

    // Database
    private lateinit var favoriteRepository: FavoriteRepository
    private var isFavorite = false
    private lateinit var event: Event

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_event)
        supportActionBar?.title = "Detail Event"

        // Initialize Views
        imageView = findViewById(R.id.iv_event)
        tvEventTitle = findViewById(R.id.tv_event_title)
        tvOwnerName = findViewById(R.id.tv_owner_name)
        tvTimeEvent = findViewById(R.id.tv_time_event)
        tvQuota = findViewById(R.id.tv_quota)
        tvEventDescription = findViewById(R.id.tv_event_desc)
        tvRegistrants = findViewById(R.id.tv_registrants)
        tvCategory = findViewById(R.id.tv_category)
        progressBar = findViewById(R.id.progressBar)
        btnRegister = findViewById(R.id.btn_register)
        fabFavorite = findViewById(R.id.fab_favorite)

        // Initialize Repository
        favoriteRepository = FavoriteRepository(application)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]

        // Get Event ID from Intent
        val eventID = intent.getIntExtra("EVENT_ID", -1)
        Log.d("DetailEventActivity", "Received Event ID: $eventID")

        if (eventID != -1) {
            // Fetch event details from ViewModel
            viewModel.fetchEvent(eventID)
        }
        else {
            Log.e("DetailEventActivity", "Invalid or null Event ID!")
        }

        // Observe ViewModel data
        viewModel.events.observe(this) { detailResponse ->
            if (detailResponse?.event != null) {
                event = detailResponse.event
                displayEventDetails(event)
                observeFavoriteStatus(event.id) // Observe favorite status for current event
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        btnRegister.setOnClickListener {
            if (this::eventLink.isInitialized) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(eventLink))
                startActivity(intent)
            }
            else {
                Toast.makeText(this, "Event link not available", Toast.LENGTH_SHORT).show()
            }
        }

        fabFavorite.setOnClickListener {
            toggleFavorite()
        }
    }

    private fun displayEventDetails(event: Event) {
        tvEventTitle.text = event.name
        tvOwnerName.text = event.ownerName
        tvTimeEvent.text = "${event.beginTime} - ${event.endTime}"
        tvQuota.text = "Quota: ${event.quota}"
        tvEventDescription.text = HtmlCompat.fromHtml(event.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
        tvRegistrants.text = "Registrants: ${event.registrants}"
        tvCategory.text = "Category: ${event.category}"
        eventLink = event.link

        Glide.with(this)
            .load(event.mediaCover)
            .into(imageView)
    }

    private fun observeFavoriteStatus(eventId: Int) {
        // Observing the favorite status
        favoriteRepository.isFavorite(eventId).observe(this) { favorite ->
            isFavorite = favorite != null
            updateFabIcon()
        }
    }

    private fun toggleFavorite() {
        CoroutineScope(Dispatchers.IO).launch {
            val favoriteEvent = FavoriteEvent(
                id = event.id,
                name = event.name,
                mediaCover = event.mediaCover
            )
            if (isFavorite) {
                favoriteRepository.delete(favoriteEvent)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DetailEventActivity, "Removed from Favorites", Toast.LENGTH_SHORT).show()
                }
            } else {
                favoriteRepository.insert(favoriteEvent)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DetailEventActivity, "Added to Favorites", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateFabIcon() {
        fabFavorite.setImageResource(if (isFavorite) R.drawable.ic_fav else R.drawable.ic_unfav)
    }
}
