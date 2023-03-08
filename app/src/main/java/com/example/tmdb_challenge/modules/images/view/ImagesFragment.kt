package com.example.tmdb_challenge.modules.images.view

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.tmdb_challenge.R
import com.example.tmdb_challenge.base.BaseFragment
import com.example.tmdb_challenge.databinding.FragmentImagesBinding
import com.example.tmdb_challenge.modules.images.viewModel.ImagesViewModel
import com.example.tmdb_challenge.util.observe
import com.example.tmdb_challenge.util.withViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ImagesFragment : BaseFragment<FragmentImagesBinding>() {

    private val readImagePermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else
            Manifest.permission.READ_EXTERNAL_STORAGE


    override fun initView() {
        super.initView()
        with(binding) {
            btGallery.setOnClickListener {
                handleGalleryPermissions()
            }
            btCamera.setOnClickListener {
                handleCameraPermissions()
            }
        }
    }

    override fun getBindingView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentImagesBinding = FragmentImagesBinding.inflate(inflater, container, false)

    override fun getViewModel(): ImagesViewModel = withViewModel {
        observe(isUploading()) { onImageUploading(it) }
        observe(uploadStatus()) { onUploadStatus(it) }
    }

    private fun onUploadStatus(isSuccess: Boolean) {
        binding.tvImageStatus.apply {
            if (isSuccess) {
                text = getString(R.string.success_uploading_image)
                setTextColor(ContextCompat.getColor(context, R.color.green_success))
            }else{
                text = getString(R.string.error_uploading_image)
                setTextColor(ContextCompat.getColor(context, R.color.red_error))
            }
            isVisible = true
        }
    }

    private fun onImageUploading(isUploading: Boolean) {
        with(binding) {
            pbUploading.isVisible = isUploading
            btCamera.isEnabled = !isUploading
            btGallery.isEnabled = !isUploading
        }
    }

    private fun launchGallery() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        launcherIntentImage.launch(intent)
    }

    private fun launchCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        launcherIntentCamera.launch(intent)
    }

    private fun handleGalleryPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                readImagePermission,
            ) == PackageManager.PERMISSION_GRANTED -> {
                launchGallery()
            }
            shouldShowRequestPermissionRationale(readImagePermission) -> {
                showPermissionRationale()
            }
            else -> {
                requestPermissionGalleryLauncher.launch(
                    readImagePermission
                )
            }
        }
    }

    private fun handleCameraPermissions() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA,
            ) == PackageManager.PERMISSION_GRANTED -> {
                launchCamera()
            }
            shouldShowRequestPermissionRationale(readImagePermission) -> {
                showPermissionRationale()
            }
            else -> {
                requestPermissionCameraLauncher.launch(
                    Manifest.permission.CAMERA
                )
            }
        }
    }

    private val requestPermissionGalleryLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                launchGallery()
            } else {
                showPermissionRationale()
            }
        }

    private val requestPermissionCameraLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                launchCamera()
            } else {
                showPermissionRationale()
            }
        }

    private val launcherIntentImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                data?.data?.let {
                    getViewModel().uploadImage(it)
                }
            }
        }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                data?.getParcelableExtra("data", Bitmap::class.java)
            } else {
                data?.getParcelableExtra<Bitmap>("data")
            }
            bitmap?.let { getViewModel().uploadImage(bitmap) }
        }
    }
}