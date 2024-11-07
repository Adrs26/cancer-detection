package com.dicoding.asclepius.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.HistoryAdapter
import com.dicoding.asclepius.data.local.LocalDatabase
import com.dicoding.asclepius.data.repository.CheckRepository
import com.dicoding.asclepius.databinding.FragmentHistoryBinding
import com.dicoding.asclepius.viewmodel.HistoryViewModel
import com.dicoding.asclepius.viewmodel.ViewModelFactory

class HistoryFragment : Fragment(R.layout.fragment_history) {
    private val binding by viewBinding(FragmentHistoryBinding::bind)
    private val historyViewModel by lazy {
        val factory = ViewModelFactory(CheckRepository(
            LocalDatabase.getDatabase(requireContext()).getCheckDao())
        )
        ViewModelProvider(requireActivity(), factory)[HistoryViewModel::class.java]
    }
    private lateinit var historyAdapter: HistoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        getCheckHistory()
        setupObservers()
    }

    private fun setupAdapter() {
        historyAdapter = HistoryAdapter()
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.adapter = historyAdapter
    }

    private fun getCheckHistory() {
        historyViewModel.getCheckHistory()
    }

    private fun setupObservers() {
        historyViewModel.checkHistory.observe(viewLifecycleOwner) { listCheckHistory ->
            historyAdapter.submitList(listCheckHistory)

            if (listCheckHistory.isEmpty()) {
                binding.tvEmptyData.visibility = View.VISIBLE
            } else {
                binding.tvEmptyData.visibility = View.GONE
            }
        }
    }
}