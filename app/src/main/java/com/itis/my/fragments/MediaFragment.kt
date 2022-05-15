package com.itis.my.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.itis.my.adapters.MediaAdapter
import com.itis.my.databinding.FragmentMediaBinding
import com.itis.my.model.Media
import com.itis.my.viewmodels.MediaViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import kotlin.random.Random

class MediaFragment : ViewBindingFragment<FragmentMediaBinding>(FragmentMediaBinding::inflate) {


    private val viewModel: MediaViewModel by viewModels()

    private lateinit var recorder: MediaRecorder

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
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
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
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO
                )
            )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.listenMediaFromDb()
        checkPhotoPermissions()
        viewModel.media.observe(viewLifecycleOwner) { media ->
            binding.pbLoading.visibility = View.GONE
            (binding.rvMedia.adapter as MediaAdapter).updateList(media)
        }

        binding.fabAddAudio.setOnTouchListener { v, event ->
            binding.pbLoading.visibility = View.VISIBLE
            val fileName =
                "${requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC)?.absolutePath}/audiorecordtest.3gp"
            val file = File(fileName).apply {
                createNewFile()
                deleteOnExit()
            }
            if (event.action == MotionEvent.ACTION_DOWN) {
                recorder = MediaRecorder().apply {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                    setOutputFile(fileName)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

                    try {
                        prepare()
                    } catch (e: IOException) {
                        Log.e("as", "prepare() failed")
                    }
                    start()
                }

            }

            if (event.action == MotionEvent.ACTION_UP) {
                recorder.stop()
                viewModel.saveAudio(
                    Media.Audio(
                        Random.nextInt().toString(),
                        FileProvider.getUriForFile(
                            requireContext(),
                            "com.itis.fileprovider",
                            file
                        ),
                        Instant.now().toEpochMilli()
                    )
                )
            }

            return@setOnTouchListener true
        }



        binding.fabAddVideo.setOnClickListener {
            binding.pbLoading.visibility = View.VISIBLE
            takeVideo()
        }

        binding.rvMedia.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = MediaAdapter { uriString ->
                PhotoDetailedDialogFragment().newInstance(uriString)
                    .show(parentFragmentManager, "ac")
            }
        }
        binding.fabAddPhoto.setOnClickListener {
            binding.pbLoading.visibility = View.VISIBLE
            takeImage()
        }
    }


    private val takeVideoResult =
        registerForActivityResult(ActivityResultContracts.TakeVideo()) { bitmap ->
            latestTmpUri?.let { uri ->
                viewModel.saveVideo(Media.Video(0.toString(), uri, Instant.now().toEpochMilli()))
            }
        }


    private fun takeVideo() {
        lifecycleScope.launchWhenStarted {
            getTmpVideoFileUri().let { uri ->
                latestTmpUri = uri
                takeVideoResult.launch(latestTmpUri)
            }
        }
    }


    private fun getTmpVideoFileUri(): Uri {
        val tmpFile =
            File.createTempFile(
                "MP4_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ROOT).format(Date())}_",
                ".mp4",
                requireContext().getExternalFilesDir(Environment.DIRECTORY_MOVIES)
            ).apply {
                createNewFile()
                deleteOnExit()
            }
        return FileProvider.getUriForFile(requireContext(), "com.itis.fileprovider", tmpFile)
    }


    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    viewModel.savePhoto(
                        Media.Photo(
                            0.toString(),
                            uri,
                            Instant.now().toEpochMilli()
                        )
                    )
                }
            }
        }
    private var latestTmpUri: Uri? = null


    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpPhotoFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }


    private fun getTmpPhotoFileUri(): Uri {
        val tmpFile =
            File.createTempFile(
                "PNG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ROOT).format(Date())}_",
                ".png",
                requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            ).apply {
                createNewFile()
                deleteOnExit()
            }
        return FileProvider.getUriForFile(requireContext(), "com.itis.fileprovider", tmpFile)
    }
}
