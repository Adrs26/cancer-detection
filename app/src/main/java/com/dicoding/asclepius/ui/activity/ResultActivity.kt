package com.dicoding.asclepius.ui.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.LocalDatabase
import com.dicoding.asclepius.data.local.entity.Check
import com.dicoding.asclepius.data.repository.CheckRepository
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.ml.CancerClassification
import com.dicoding.asclepius.util.CheckResultUtil
import com.dicoding.asclepius.util.DateUtil.convertDateToEnglishFormat
import com.dicoding.asclepius.viewmodel.HistoryViewModel
import com.dicoding.asclepius.viewmodel.ViewModelFactory
import org.tensorflow.lite.support.label.Category

@SuppressLint("SourceLockedOrientationActivity")
class ResultActivity : AppCompatActivity(R.layout.activity_result) {
    private val binding by viewBinding(ActivityResultBinding::bind)
    private val historyViewModel by lazy {
        val factory = ViewModelFactory(
            CheckRepository(
            LocalDatabase.getDatabase(this).getCheckDao())
        )
        ViewModelProvider(this, factory)[HistoryViewModel::class.java]
    }
    private val category by lazy {
        CheckResultUtil.outputCancerClassification?.let { getResults(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupButton()
        setupResult()
    }

    private fun setupButton() {
        binding.ibBack.setOnClickListener {
            finish()
        }
        binding.saveButton.setOnClickListener {
            historyViewModel.saveCheck(Check(
                imageUri = CheckResultUtil.imageUri.toString(),
                classification = binding.classificationResult.text.toString(),
                confidenceScore = binding.confidenceScoreResult.text.toString(),
                checkDate = binding.dateResult.text.toString()
            ))
            Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show()
            finish()
        }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun setupResult() {
        Glide.with(this)
            .load(CheckResultUtil.imageUri)
            .centerCrop()
            .into(binding.resultImage)

        binding.classificationResult.text = category?.label
        binding.confidenceScoreResult.text = resources.getString(
            R.string.result_percentage,
            (category?.score?.times(100))?.toInt().toString()
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.dateResult.text = convertDateToEnglishFormat()
        }
    }

    private fun getResults(outputs: CancerClassification.Outputs): Category? {
        val probabilities = outputs.probabilityAsCategoryList
        return probabilities.maxByOrNull { it.score }
    }
}