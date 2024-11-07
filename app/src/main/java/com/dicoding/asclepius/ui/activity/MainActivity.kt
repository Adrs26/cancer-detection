package com.dicoding.asclepius.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.util.CheckResultUtil
import com.dicoding.asclepius.viewmodel.CheckViewModel
import com.yalantis.ucrop.UCrop
import java.io.File

@SuppressLint("SourceLockedOrientationActivity")
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::bind)
    private val checkViewModel by lazy {
        ViewModelProvider(this)[CheckViewModel::class.java]
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.READ_MEDIA_IMAGES] == true) {
            startGallery()
        } else {
            Toast.makeText(
                this,
                "Izin mengakses media penyimpan ditolak",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = result.data?.data
            imageUri?.let {
                val fileName = resources.getString(
                    R.string.file_name,
                    System.currentTimeMillis().toString()
                )
                val destinationUri = Uri.fromFile(File(cacheDir, fileName))

                val uCropIntent = UCrop.of(it, destinationUri)
                    .withAspectRatio(1f, 1f)
                    .withMaxResultSize(300, 300)
                    .getIntent(this)

                cropImageLauncher.launch(uCropIntent)
            }
        }
    }

    private val cropImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val resultUri = UCrop.getOutput(result.data!!)
            resultUri?.let {
                checkViewModel.getUserPhoto(it)
                CheckResultUtil.imageUri = it
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomNavigationView()
        setupObservers()
    }

    private fun setupBottomNavigationView() {
        val navView = binding.navBottomView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        setupToolbar(navController)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun setupObservers() {
        checkViewModel.userPhoto.observe(this) {
            if (it != null) {
                binding.galleryButton.setImageResource(R.drawable.ic_replace_photo)
            }
        }
    }

    private fun setupToolbar(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.fragmentCheck -> {
                    binding.tvToolbarTitle.text = resources.getString(R.string.label_check)
                    binding.galleryButton.visibility = View.VISIBLE
                }
                R.id.fragmentHistory -> {
                    binding.tvToolbarTitle.text = resources.getString(R.string.label_history)
                    binding.galleryButton.visibility = View.GONE
                }
                R.id.fragmentNews -> {
                    binding.tvToolbarTitle.text = resources.getString(R.string.label_news)
                    binding.galleryButton.visibility = View.GONE
                }
            }
        }

        binding.galleryButton.setOnClickListener {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED -> {
                    startGallery()
                }
                else -> {
                    requestPermissionLauncher.launch(arrayOf(Manifest.permission.READ_MEDIA_IMAGES))
                }
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED) {
                startGallery()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    10
                )
            }
        }
    }

    private fun startGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }
}