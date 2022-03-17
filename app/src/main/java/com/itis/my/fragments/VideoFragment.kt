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
import androidx.recyclerview.widget.LinearLayoutManager
import com.itis.my.adapters.VideoAdapter
import com.itis.my.databinding.FragmentVideoBinding
import com.itis.my.model.Video
import com.itis.my.viewmodels.VideoViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class VideoFragment : ViewBindingFragment<FragmentVideoBinding>(FragmentVideoBinding::inflate) {

    private val viewModel: VideoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvVideos.apply {
            adapter = VideoAdapter()
            layoutManager = LinearLayoutManager(requireContext())
        }
        checkPhotoPermissions()
        viewModel.listenVideosFromDb()
        viewModel.videos.observe(viewLifecycleOwner) { videos ->
            (binding.rvVideos.adapter as VideoAdapter).updateList(videos)
        }
        binding.fabAddVideo.setOnClickListener {
            takeVideo()
        }
    }

    private val takeVideoResult =
        registerForActivityResult(ActivityResultContracts.TakeVideo()) { bitmap ->
            latestTmpUri?.let { uri ->
                viewModel.saveVideo(Video(0, uri.toString(), Instant.now().toEpochMilli()))
            }
        }

    private var latestTmpUri: Uri? = null

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

    private fun takeVideo() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeVideoResult.launch(latestTmpUri)
            }
        }
    }


    private fun getTmpFileUri(): Uri {
        val tmpFile =
            File.createTempFile(
                "MKV_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ROOT).format(Date())}_",
                ".mkv",
                requireContext().getExternalFilesDir(Environment.DIRECTORY_MOVIES)
            ).apply {
                createNewFile()
                deleteOnExit()
            }
        return FileProvider.getUriForFile(requireContext(), "com.itis.fileprovider", tmpFile)
    }


}