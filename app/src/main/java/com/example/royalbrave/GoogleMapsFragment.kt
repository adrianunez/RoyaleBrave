package com.example.royalbrave

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.annotations.NonNls

// TODO: Rename parameter arguments, choose names that match

class GoogleMapsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_google_maps, container, false)
        val supportMapFragment = childFragmentManager.findFragmentById(R.id.My_MAP) as SupportMapFragment
        supportMapFragment.getMapAsync(object : OnMapReadyCallback {
            override fun onMapReady(googleMap: GoogleMap) {
                googleMap.setOnMapClickListener(object : GoogleMap.OnMapClickListener {
                    override fun onMapClick(latLng: LatLng) {
                        val markerOptions = MarkerOptions()
                        markerOptions.position(latLng)
                        markerOptions.title("La Salle")
                        googleMap.clear()
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20F))
                        googleMap.addMarker(markerOptions)
                    }
                })
            }
        })
        return view
    }

}