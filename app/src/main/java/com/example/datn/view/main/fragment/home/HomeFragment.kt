package com.example.datn.view.main.fragment.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.datn.R
import com.example.datn.databinding.FragmentHomeBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.gms.maps.model.PolylineOptions
import kotlin.math.*

class HomeFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentHomeBinding
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    // Địa điểm cố định (sau này có thể lấy từ API)
    private val fixedLocation = LatLng(21.02295, 105.80137) // Ví dụ: Hồ Con Rùa, Sài Gòn

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        requestLocationPermission()
        return binding.root
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            setupMap()
        }
    }

    private fun setupMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as? com.google.android.gms.maps.SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        Log.e("MapFragment", "onMapReady called")

        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        enableMyLocation()
    }

    private fun enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            getLocation()
        } else {
            requestLocationPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)

                // Đánh dấu vị trí của người dùng
                mMap.addMarker(
                    MarkerOptions()
                        .position(currentLatLng)
                        .title("Vị trí của bạn")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                )

                // Đánh dấu địa điểm cố định
                mMap.addMarker(
                    MarkerOptions()
                        .position(fixedLocation)
                        .title("Địa điểm cố định")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                )

                // Vẽ đường đi
                drawRoute(currentLatLng, fixedLocation)

                // Tính khoảng cách
                val distance = calculateDistance(
                    currentLatLng.latitude, currentLatLng.longitude,
                    fixedLocation.latitude, fixedLocation.longitude
                )
                Snackbar.make(binding.root, "Khoảng cách: ${"%.2f".format(distance)} km", Snackbar.LENGTH_LONG).show()

                // Di chuyển camera để hiển thị cả 2 điểm
                val bounds = LatLngBounds.builder()
                    .include(currentLatLng)
                    .include(fixedLocation)
                    .build()
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150))
            } else {
                Snackbar.make(binding.root, "Không thể lấy vị trí hiện tại", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }

    // Hàm vẽ đường đi giữa hai điểm
    private fun drawRoute(start: LatLng, end: LatLng) {
        val polylineOptions = PolylineOptions()
            .add(start)
            .add(end)
            .width(8f)
            .color(ContextCompat.getColor(requireContext(), R.color.black))
        mMap.addPolyline(polylineOptions)
    }

    // Hàm tính khoảng cách giữa hai tọa độ (km)
    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371 // Bán kính trái đất (km)
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c // Khoảng cách (km)
    }
}
