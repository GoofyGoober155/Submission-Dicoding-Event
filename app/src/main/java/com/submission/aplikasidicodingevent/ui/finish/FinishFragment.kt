package com.submission.aplikasidicodingevent.ui.finish

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.aplikasidicodingevent.databinding.FragmentFinishBinding
import com.submission.aplikasidicodingevent.ui.detail.DetailEventActivity
import com.submission.aplikasidicodingevent.adapter.EventAdapter

class FinishFragment : Fragment() {

    private var _binding: FragmentFinishBinding? = null
    private val binding get() = _binding!!

    private val finishViewModel : FinishViewModel by viewModels()
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventAdapter = EventAdapter { event ->
            val intent = Intent(requireContext(), DetailEventActivity::class.java)
            intent.putExtra("EVENT_ID", event.id)
            startActivity(intent)
        }

        binding.rvFinishEvent.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFinishEvent.adapter = eventAdapter

        // observe LiveData dari ViewModel
        finishViewModel.events.observe(viewLifecycleOwner) { eventList ->
            eventAdapter.submitList(eventList)
        }

        // observe loading
        finishViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        // observe error message
        finishViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        // memanggil fetch data dari ViewModel saat fragment dimulai
        finishViewModel.fetchEvents()
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