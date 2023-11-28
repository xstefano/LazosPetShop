package com.example.lazospetshop.actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Geocoder;
import android.os.Bundle;

import com.example.lazospetshop.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class UbicacionActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private Geocoder geocoder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapUbicacion);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng ubicacion = new LatLng(-11.966221, -76.993650);
        googleMap.addMarker(new MarkerOptions().position(ubicacion).title("Lazos Pet Shop"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 16));
    }
}