package ca.ulaval.ima.mp.ui.home

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ca.ulaval.ima.mp.databinding.FragmentHomeBinding
import ca.ulaval.ima.mp.ui.home.restaurant.RestaurantLight
import ca.ulaval.ima.mp.ui.home.restaurant.RestaurantService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.sql.Types

class HomeFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var  context: Context


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
      //  val homeViewModel =
        //    ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.onResume()
        mapView.getMapAsync(this)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            // Récupérer la nouvelle position ici et mettre à jour la carte
            val currentLatLng = LatLng(location.latitude, location.longitude)
            googleMap.addMarker(MarkerOptions().position(currentLatLng).title("Marker at current location"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng))
        }

        override fun onProviderEnabled(provider: String) {}

        override fun onProviderDisabled(provider: String) {}

        @Deprecated("Deprecated in Java")
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    }

    private fun getLastKnownLocation(context: Context): Location? {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 0f, locationListener)

        val criteria = Criteria()
        val provider = locationManager.getBestProvider(criteria, false)
        return if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            null
        } else {
            if (provider != null) {
                locationManager.getLastKnownLocation(provider)
            } else {
                null
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMapReady(map: GoogleMap) {

        googleMap = map

        // Customize the map
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = true

        val location = getLastKnownLocation(requireContext())

        // Add a marker in current location and move the camera
        val currentLatLng = location?.let { LatLng(it.latitude, it.longitude) }
        if (currentLatLng != null) {
            googleMap.addMarker(MarkerOptions().position(currentLatLng).title("Home"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,15f))

                    // Handle error
            // Call getNearbyRestaurants function to get the nearby restaurants
            val position = Location("").apply {
                latitude = currentLatLng.latitude
                longitude = currentLatLng.longitude
            }
            RestaurantService().getNearbyRestaurants(position, 1.0,
                onSuccess = {
                    // handle the nearby restaurants here
                },
                onError = {
                    // handle the error here
                }
            )
        }


    }



}