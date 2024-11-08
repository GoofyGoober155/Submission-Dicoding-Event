package com.submission.aplikasidicodingevent.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.aplikasidicodingevent.databinding.FragmentHomeBinding
import com.submission.aplikasidicodingevent.adapter.EventAdapter
import com.submission.aplikasidicodingevent.adapter.VPEventAdapter
import com.submission.aplikasidicodingevent.ui.detail.DetailEventActivity
import com.submission.aplikasidicodingevent.ui.setting.SettingPreferences
import com.submission.aplikasidicodingevent.ui.setting.SettingViewModel
import com.submission.aplikasidicodingevent.ui.setting.SettingViewModelFactory
import com.submission.aplikasidicodingevent.ui.setting.dataStore

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var upcomingEventAdapter: VPEventAdapter // Adapter untuk ViewPager
    private lateinit var finishedEventAdapter: EventAdapter // Adapter untuk Finished Events

    private lateinit var settingViewModel: SettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        // Setup ViewPager for Upcoming Events
        upcomingEventAdapter = VPEventAdapter { event ->
            val intent = Intent(requireContext(), DetailEventActivity::class.java)
            intent.putExtra("EVENT_ID", event.id)
            startActivity(intent)
        }

        binding.viewPagerUpcoming.adapter = upcomingEventAdapter

        // Setup RecyclerView for Finished Events
        finishedEventAdapter = EventAdapter { event ->
            val intent = Intent(requireContext(), DetailEventActivity::class.java)
            intent.putExtra("EVENT_ID", event.id)
            startActivity(intent)
        }

        binding.rvFinished.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFinished.adapter = finishedEventAdapter

        // Observe Upcoming Events
        viewModel.upcomingEvents.observe(viewLifecycleOwner) { eventList ->
            upcomingEventAdapter.submitList(eventList) // Update data pada ViewPager
        }

        // Observe Finished Events
        viewModel.finishedEvents.observe(viewLifecycleOwner) { eventList ->
            finishedEventAdapter.submitList(eventList)
        }

        // Observe loading
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        // Observe error message
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        // Inisialisasi SettingPreferences dan SettingViewModelFactory agar dapat di Observe
        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        val factory = SettingViewModelFactory(pref)
        settingViewModel = ViewModelProvider(this, factory)[SettingViewModel::class.java]

        // Observe theme settings and update UI
        settingViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        // Fetch events when fragment is created
        viewModel.fetchUpcomingEvents()
        viewModel.fetchFinishedEvents()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


