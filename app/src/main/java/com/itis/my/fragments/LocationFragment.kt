package com.itis.my.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationRequest
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.itis.my.adapters.LocationAdapter
import com.itis.my.databinding.LocationFragmentBinding
import com.itis.my.model.Location
import com.itis.my.viewmodels.LocationViewModel
import java.time.Instant
import java.util.*


class LocationFragment :
    ViewBindingFragment<LocationFragmentBinding>(LocationFragmentBinding::inflate) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private val viewModel: LocationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvLocations.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = LocationAdapter()
        }
        viewModel.listenLocationUpdates()
        val geo = Geocoder(requireContext(), Locale.getDefault())
        viewModel.locations.observe(viewLifecycleOwner) { locations ->
            (binding.rvLocations.adapter as LocationAdapter).updateList(locations)
        }



        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val locationPermissionRequest = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                when {
                    permissions.getOrDefault(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        false
                    ) -> {
                    }
                    permissions.getOrDefault(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        false
                    ) -> {
                    }
                    else -> {
                    }
                }
            }
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
        binding.fabAddLocation.setOnClickListener {
            try {
                fusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(requireActivity())
                fusedLocationClient.getCurrentLocation(
                    LocationRequest.QUALITY_HIGH_ACCURACY, CancellationTokenSource().token
                )
                    .addOnSuccessListener { location ->
                        val addresses =
                            geo.getFromLocation(location.latitude, location.longitude, 1)
                        val currentLocation = Location(
                            id = 0, text = addresses[0].getAddressLine(0),
                            Instant.now().toEpochMilli()
                        )
                        viewModel.saveLocation(currentLocation)
                    }
            } catch (e: Throwable) {

            }
        }
    }
}