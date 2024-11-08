package com.submission.aplikasidicodingevent.ui.upcoming

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.aplikasidicodingevent.databinding.FragmentUpcomingBinding
import com.submission.aplikasidicodingevent.ui.detail.DetailEventActivity
import com.submission.aplikasidicodingevent.adapter.EventAdapter

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private val upcomingViewModel : UpcomingViewModel by viewModels()
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        eventAdapter = EventAdapter { event ->
            val intent = Intent(requireContext(), DetailEventActivity::class.java)
            intent.putExtra("EVENT_ID", event.id)
            startActivity(intent)
        }
        binding.rvUpcomingEvent.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUpcomingEvent.adapter = eventAdapter

        // observe LiveData dari ViewModel
        upcomingViewModel.events.observe(viewLifecycleOwner) { eventList ->
            eventAdapter.submitList(eventList)
        }

        // observe loading
        upcomingViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        // observe error message
        upcomingViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        // memanggil fetch data dari ViewModel saat fragment dimulai
        upcomingViewModel.fetchEvents()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        }
        else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
