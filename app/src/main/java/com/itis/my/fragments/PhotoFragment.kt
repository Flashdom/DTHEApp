package com.itis.my.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.itis.my.adapters.PhotoAdapter
import com.itis.my.databinding.PhotoFragmentBinding
import com.itis.my.model.Photo
import com.itis.my.viewmodels.PhotoViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class PhotoFragment : ViewBindingFragment<PhotoFragmentBinding>(PhotoFragmentBinding::inflate) {


    private val viewModel: PhotoViewModel by viewModels()


    private fun checkPhotoPermissions() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val locationPermissionRequest = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) {
            }
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkPhotoPermissions()
        viewModel.listenPhotosFromDb()
        viewModel.photos.observe(viewLifecycleOwner) { photos ->
            (binding.rvPhotos.adapter as PhotoAdapter).updateList(photos)

        }
        binding.rvPhotos.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = PhotoAdapter { uriString ->
                PhotoDetailedDialogFragment().newInstance(uriString)
                    .show(parentFragmentManager, "ac")
            }
        }
        binding.fabAddPhoto.setOnClickListener {
            takeImage()
        }
    }


    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    viewModel.savePhoto(Photo(0, uri, Instant.now().toEpochMilli()))
                }
            }
        }
    private var latestTmpUri: Uri? = null


    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }


    private fun getTmpFileUri(): Uri {
        val tmpFile =
            File.createTempFile(
                "JPEG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ROOT).format(Date())}_",
                ".png",
                requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            ).apply {
                createNewFile()
                deleteOnExit()
            }
        return FileProvider.getUriForFile(requireContext(), "com.itis.fileprovider", tmpFile)
    }
}
