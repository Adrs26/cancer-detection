package com.dicoding.asclepius.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.FragmentCheckBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.ml.CancerClassification
import com.dicoding.asclepius.ui.activity.ResultActivity
import com.dicoding.asclepius.util.CheckResultUtil
import com.dicoding.asclepius.util.CheckResultUtil.imageUri
import com.dicoding.asclepius.util.CheckResultUtil.outputCancerClassification
import com.dicoding.asclepius.viewmodel.CheckViewModel
import org.tensorflow.lite.support.image.TensorImage

class CheckFragment : Fragment(R.layout.fragment_check) {
    private val binding by viewBinding(FragmentCheckBinding::bind)
    private val checkViewModel by lazy {
        ViewModelProvider(requireActivity())[CheckViewModel::class.java]
    }
    private val imageClassifierHelper by lazy {
        ImageClassifierHelper(requireContext(), object : ImageClassifierHelper.ClassifierListener{
            override fun onError(error: String) {
                showToast(error)
            }

            override fun onResults(tensorImage: TensorImage) {
                outputCancerClassification = cancerClassification.process(tensorImage)
                CheckResultUtil.isNewData = true
                moveToResult()
            }
        })
    }
    private lateinit var cancerClassification: CancerClassification

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButton()
        setupObservers()
    }

    override fun onResume() {
        super.onResume()
        cancerClassification = CancerClassification.newInstance(requireContext())
        binding.progressIndicator.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cancerClassification.close()
    }

    private fun setupButton() {
        binding.analyzeButton.setOnClickListener {
            analyzeImage()
        }
    }

    private fun setupObservers() {
        checkViewModel.userPhoto.observe(viewLifecycleOwner) {
            showImage(it)
             if (it != null) {
                 binding.analyzeButton.visibility = View.VISIBLE
             }
        }
    }

    private fun showImage(userPhoto: Uri) {
        Glide.with(requireContext())
            .load(userPhoto)
            .centerCrop()
            .into(binding.previewImageView)
    }

    private fun analyzeImage() {
        binding.progressIndicator.visibility = View.VISIBLE
        imageUri?.let { imageClassifierHelper.classifyStaticImage(it) }
    }

    private fun moveToResult() {
        val intent = Intent(requireContext(), ResultActivity::class.java)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}