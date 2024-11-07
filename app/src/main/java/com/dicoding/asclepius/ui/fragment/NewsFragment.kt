package com.dicoding.asclepius.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.NewsAdapter
import com.dicoding.asclepius.databinding.FragmentNewsBinding
import com.dicoding.asclepius.viewmodel.NewsViewModel

class NewsFragment : Fragment(R.layout.fragment_news) {
    private val binding by viewBinding(FragmentNewsBinding::bind)
    private val newsViewModel by lazy {
        ViewModelProvider(requireActivity())[NewsViewModel::class.java]
    }
    private lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        getLatestCancerNews()
        setupObserver()
    }

    private fun setupAdapter() {
        newsAdapter = NewsAdapter()
        binding.rvNews.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNews.adapter = newsAdapter
    }

    private fun getLatestCancerNews() {
        newsViewModel.getLatestCancerNews()
    }

    private fun setupObserver() {
        newsViewModel.newsArticles.observe(viewLifecycleOwner) { listArticles ->
            binding.rvNews.visibility = View.VISIBLE
            binding.tvFailedLoadData.visibility = View.GONE
            newsAdapter.submitList(listArticles)
        }

        newsViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.rvNews.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        newsViewModel.exception.observe(viewLifecycleOwner) { exception ->
            if (exception) {
                showToast(resources.getString(R.string.failed_to_load_data))
                binding.tvFailedLoadData.visibility = View.VISIBLE
                newsViewModel.resetExceptionValue()
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}