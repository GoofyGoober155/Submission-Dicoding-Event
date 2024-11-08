package com.submission.aplikasidicodingevent.ui.favorite

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.aplikasidicodingevent.adapter.FavEventAdapter
import com.submission.aplikasidicodingevent.databinding.FragmentFavoriteBinding
import com.submission.aplikasidicodingevent.ui.detail.DetailEventActivity

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private lateinit var favoriteAdapter: FavEventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        // observe loading
        favoriteViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        // observe error message
        favoriteViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        favoriteAdapter = FavEventAdapter { event ->
            val intent = Intent(requireContext(), DetailEventActivity::class.java)
            intent.putExtra("EVENT_ID", event.id)
            startActivity(intent)
        }
        binding.rvFavoriteEvent.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavoriteEvent.adapter = favoriteAdapter

        // Observe favorite events from ViewModel
        favoriteViewModel.favoriteEvents.observe(viewLifecycleOwner) { favoriteList ->
            if (favoriteList.isNullOrEmpty()) {
                binding.rvFavoriteEvent.visibility = View.GONE
            }
            else {
                binding.rvFavoriteEvent.visibility = View.VISIBLE
                favoriteAdapter.submitList(favoriteList)
            }
        }
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
