package com.example.tmdb_challenge.modules.locations.view

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.tmdb_challenge.base.BaseFragment
import com.example.tmdb_challenge.databinding.FragmentLocationBinding
import com.example.tmdb_challenge.domain.models.firestore.LocationFirestore
import com.example.tmdb_challenge.modules.locations.viewModel.LocationsViewModel
import com.example.tmdb_challenge.services.LocationService
import com.example.tmdb_challenge.util.observe
import com.example.tmdb_challenge.util.safeLet
import com.example.tmdb_challenge.util.toDate
import com.example.tmdb_challenge.util.withViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.config.Configuration.getInstance
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker


@AndroidEntryPoint
class LocationsFragment : BaseFragment<FragmentLocationBinding>() {

    private lateinit var map: MapView

    override fun setUp(arguments: Bundle?) {
        super.setUp(arguments)
        handleNotificationPermission()
    }


    override fun initView() {
        super.initView()
        map = binding.mvLocations
    }

    private fun initMap() {
        getInstance().load(
            requireContext(),
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        )
        map.setTileSource(TileSourceFactory.MAPNIK)
        val mapController = map.controller
        mapController.setZoom(17.0)
        val startPoint = GeoPoint(-38.1181826, -57.5952724);
        mapController.setCenter(startPoint)
        startService()
    }

    private fun handleNotificationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED -> {
                handleLocationPermission()
            }
            shouldShowRequestPermissionRationale(POST_NOTIFICATIONS) -> {
                showPermissionRationale()
                handleLocationPermission()
            }
            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestNotificationPermissionLauncher.launch(
                        POST_NOTIFICATIONS
                    )
                }
            }
        }
    }

    private fun handleLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED -> {
                initMap()
            }
            shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION) -> {
                showPermissionRationale()
            }
            else -> {
                requestLocationPermissionLauncher.launch(
                    ACCESS_FINE_LOCATION
                )
            }
        }
    }

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                handleLocationPermission()
            } else {
                showPermissionRationale()
            }
        }


    private val requestLocationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                initMap()
            } else {
                showPermissionRationale()
            }
        }

    override fun getBindingView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentLocationBinding = FragmentLocationBinding.inflate(inflater, container, false)

    override fun getViewModel(): LocationsViewModel = withViewModel {
        observe(locations()) { onLocations(it) }
    }

    private fun onLocations(locations: List<LocationFirestore>) {
        locations.forEach { location ->
            safeLet(location.latitude, location.longitude) { lat, lon ->
                val marker = Marker(map)
                marker.position = GeoPoint(lat, lon)
                marker.title = location.time?.toDate()
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                marker.showInfoWindow()
                map.overlays.add(marker)
            }
        }
        map.invalidate()
        //move to last location
        with(locations.lastOrNull()) {
            safeLet(this?.latitude, this?.longitude) { lat, lon ->
                map.controller.setCenter(GeoPoint(lat, lon))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onResume()
    }

    override fun onDetach() {
        super.onDetach()
        map.onDetach()
    }


    private fun startService() {
        val alarmManager =
            requireContext().getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), LocationService::class.java)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getService(requireContext(), 0, intent, FLAG_IMMUTABLE)
        } else {
            PendingIntent.getService(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            SystemClock.elapsedRealtime(),
            1000 * 60 * 1, //5 minutes
            pendingIntent
        )
        getViewModel().requestLocations()
    }

}