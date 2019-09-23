package com.example.locationApp

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Paint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow


class MainActivity : AppCompatActivity(), LocationListener {

    val TAG = "DBG"
    private lateinit var locationManager: LocationManager
    private lateinit var mLocationOverlay: MyLocationNewOverlay
    private var tracking = false
    private var locationData: MutableList<Location> = mutableListOf()
    private lateinit var line: Polyline
    private lateinit var marker: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // load/initialize the osmdroid configuration
        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))

        setContentView(R.layout.activity_main)
        track_fab.setImageResource(
            if(tracking) R.drawable.osm_ic_follow_me_on else R.drawable.osm_ic_follow_me
        )

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // setupo map view
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setBuiltInZoomControls(false)
        map.setMultiTouchControls(true)
        map.controller.setZoom(9.5)

        addMapEventsReceiver()

        line = Polyline(map)
        line.paint.color = Color.BLUE
        line.paint.strokeJoin = Paint.Join.ROUND
        line.title = "Current track info"
        line.subDescription = "Distance = ${line.distance} meters"
        line.infoWindow = BasicInfoWindow(org.osmdroid.library.R.layout.bonuspack_bubble, map)
        line.showInfoWindow()

        marker = Marker(map)
        marker.title = "Placed Marker"

        mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(this), map)
        mLocationOverlay.enableMyLocation()

        val lastLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        lastLoc?.let { map.controller.setCenter(GeoPoint(it.latitude, it.longitude)) }

        checkPermission()

        requestLocationUpdates()

        mylocation_fab.setOnClickListener {
            mLocationOverlay.isEnabled = true
            val geoPoint: GeoPoint? = mLocationOverlay.myLocation ?:
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)?.let { GeoPoint(it.latitude, it.longitude) }
            geoPoint?.let {
                map.overlays.add(mLocationOverlay)
                map.controller.animateTo(it, 18.5, 1000)
            }
        }

        track_fab.setOnClickListener {
            if (tracking) {
                tracking = false
                track_fab.setImageResource(R.drawable.osm_ic_follow_me)
                map.overlayManager.remove(line)
                mLocationOverlay.isEnabled = true
//                locationData = mutableListOf()
            } else {
                tracking = true
                track_fab.setImageResource(R.drawable.osm_ic_follow_me_on)
                map.overlays.remove(mLocationOverlay)
                mLocationOverlay.isEnabled = false
                map.overlayManager.add(line)
            }
        }
    }

    fun checkPermission() {
        if ((Build.VERSION.SDK_INT >= 23 &&
                    checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.WRITE_EXTERNAL_STORAGE),0)
        }
    }

    private fun requestLocationUpdates() {
        locationManager.removeUpdates(this)
        try {
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5.0f, this)
            } else {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 5.0f, this)
            }

        } catch (e: SecurityException) {
            Log.d(TAG, "security exception ${e.message}")
        }
    }

    override fun onLocationChanged(location: Location?) {
        Toast.makeText(this, "latitued ${location?.latitude} altitude ${location?.altitude} provided by ${location?.provider} ", Toast.LENGTH_SHORT).show()
        if (tracking) line.addPoint(GeoPoint(location))
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
//        locationManager.removeUpdates(this)
        requestLocationUpdates()
    }

    override fun onProviderEnabled(provider: String?) {
//        locationManager.removeUpdates(this)
        requestLocationUpdates()
    }

    override fun onProviderDisabled(provider: String?) {
        Toast.makeText(this, "Location is disabled, Please turn on location.", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onResume()
    }

    private fun addMapEventsReceiver() {
        var mapEventsReceiver: MapEventsReceiver = object : MapEventsReceiver {
            override fun longPressHelper(p: GeoPoint?): Boolean {
                map.overlays.remove(marker)
                marker.subDescription = "lat= ${p?.latitude} \n long= ${p?.longitude}"
                marker.position = p
                map.controller.animateTo(p)
                map.overlays.add(marker)
                return false
            }

            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                return false
            }
        }
        map.overlays.add(MapEventsOverlay(mapEventsReceiver))
    }
}
