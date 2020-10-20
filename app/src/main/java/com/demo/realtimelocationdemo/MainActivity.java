package com.demo.realtimelocationdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private FusedLocationProviderClient client;
    private String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        SupportMapFragment map = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert map != null;
        map.getMapAsync(this);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, 1000);
            return;
        }
        client = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(mActivity, permissions[0]) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnCompleteListener(MainActivity.this, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(latLng).title("real Time Location"));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                }
            }
        });
    }
}