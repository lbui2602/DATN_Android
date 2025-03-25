package com.example.datn.view.main.fragment.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.ColorStateList
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
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import kotlin.math.*

class HomeFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentHomeBinding
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private val fixedLocation = LatLng(21.02295, 105.80137)

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
            childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        // Khởi tạo FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        // Thiết lập yêu cầu cập nhật vị trí
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000) // Cập nhật mỗi 5 giây
            .setMinUpdateDistanceMeters(10f) // Chỉ cập nhật khi di chuyển tối thiểu 10m
            .build()

        // Khởi tạo LocationCallback để lắng nghe vị trí mới
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    updateLocationOnMap(LatLng(location.latitude, location.longitude))
                }
            }
        }
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

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        } else {
            requestLocationPermission()
        }
    }

    private fun updateLocationOnMap(currentLatLng: LatLng) {
        mMap.clear()


        mMap.addMarker(
            MarkerOptions()
                .position(fixedLocation)
                .title("Địa điểm cố định")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        )

        drawRoute(currentLatLng, fixedLocation)

        val distance = calculateDistance(
            currentLatLng.latitude, currentLatLng.longitude,
            fixedLocation.latitude, fixedLocation.longitude
        )
        binding.tvDistance.text = "Khoảng cách của bạn tới công ty là: ${"%.2f".format(distance)} km"
        if(distance < 1){
            binding.btnAttendance.isEnabled = true
            binding.btnAttendance.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.green)
            )
        }

        // Điều chỉnh camera để hiển thị cả hai điểm
        val bounds = LatLngBounds.builder()
            .include(currentLatLng)
            .include(fixedLocation)
            .build()

        val padding = 100 // Độ đệm cho camera (pixels)
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
    }

    // Hàm vẽ đường đi giữa hai điểm
    private fun drawRoute(start: LatLng, end: LatLng) {
        val polylineOptions = PolylineOptions()
            .add(start)
            .add(end)
            .width(3f)
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

    override fun onDestroyView() {
        super.onDestroyView()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}
