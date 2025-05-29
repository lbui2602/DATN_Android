package com.example.datn.view.main.fragment.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.datn.BuildConfig
import com.example.datn.R
import com.example.datn.base.BaseFragment
import com.example.datn.databinding.FragmentHomeBinding
import com.example.datn.util.SharedPreferencesManager
import com.example.datn.util.Util
import com.example.datn.view.main.MainActivity
import com.example.datn.view.main.fragment.change_password.ChangePasswordViewModel
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.*

@AndroidEntryPoint
class HomeFragment : BaseFragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentHomeBinding
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @Inject
    lateinit var sharedPreferencesManager: SharedPreferencesManager

    private val fixedLocation = LatLng(21.053811, 105.735400)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        requestLocationPermission()
        return binding.root
    }

    override fun setView() {}

    override fun setAction() {
        binding.btnAttendance.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_attendanceFragment)
        }
        binding.imgMess.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_messFragment)
        }
    }

    override fun setObserves() {}

    override fun setTabBar() {
        (requireActivity() as MainActivity).binding.bnvMain.visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("Home", "onViewCreated")
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (::mMap.isInitialized) {
                enableMyLocation()
            } else {
                // Nếu map chưa sẵn sàng thì setup map (onMapReady sẽ xử lý tiếp)
                setupMap()
            }
        }
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
        Log.e("setupMap", "true")
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
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
            Log.e("enableMyLocation", "Bắt đầu lấy vị trí")
            mMap.isMyLocationEnabled = true

            // Lấy vị trí hiện tại một lần duy nhất
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location ->
                    if (location != null) {
                        updateLocationOnMap(LatLng(location.latitude, location.longitude))
                    } else {
                        Toast.makeText(requireContext(), "Không lấy được vị trí hiện tại", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            requestLocationPermission()
        }
    }

    private fun updateLocationOnMap(currentLatLng: LatLng) {
        Log.e("updateLocationOnMap", "start")
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
        if (distance < 100) {
            binding.tvDistance.text = "Khoảng cách của bạn tới công ty là: ${"%.2f".format(distance)} mét.\nBạn có thể chấm công ngay."
            binding.btnAttendance.isEnabled = true
            binding.btnAttendance.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.green)
            )
        }else{
            binding.tvDistance.text = "Khoảng cách của bạn tới công ty là: ${"%.2f".format(distance)} mét.\nVui lòng đi chuyển đến bán kính 100 mét để chấm công."
            binding.btnAttendance.isEnabled = false
            binding.btnAttendance.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.grey)
            )
        }

        // Điều chỉnh camera để hiển thị cả hai điểm
        val bounds = LatLngBounds.builder()
            .include(currentLatLng)
            .include(fixedLocation)
            .build()

        val padding = 100 // Độ đệm cho camera (pixels)
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
        Log.e("updateLocationOnMap", "end")
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

    // Hàm tính khoảng cách giữa hai tọa độ (mét)
    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371000 // Bán kính trái đất (mét)
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c // Khoảng cách (mét)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Không cần remove location updates vì không đăng ký cập nhật liên tục nữa
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setupMap()
                } else {
                    Util.showDialog(
                        requireContext(),
                        "Ứng dụng cần quyền truy cập vị trí để tiếp tục. Vui lòng bật quyền trong Cài đặt.",
                        "Đi đến cài đặt",
                        {
                            val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", requireContext().packageName, null)
                            intent.data = uri
                            startActivity(intent)
                        },
                        "Huỷ",
                        {
                            requireActivity().finish()
                        }
                    )
                }
            }
        }
    }
}
