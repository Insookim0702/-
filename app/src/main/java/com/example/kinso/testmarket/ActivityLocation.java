package com.example.kinso.testmarket;
import android.app.FragmentManager;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by kinso on 2019-02-04.
 */

public class ActivityLocation extends  AppCompatActivity implements OnMapReadyCallback{
    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_location);
            FragmentManager fragmentManager = getFragmentManager();
            MapFragment mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        @Override
        public void onMapReady(final GoogleMap map) {
            Intent get =getIntent();
            //double getLatitude = Double.parseDouble(get.getExtras().getString("getLatitude"));
            //double getLongitude = Double.parseDouble(get.getExtras().getString("getLongitude"));
            LatLng SEOUL = new LatLng(35.81892284564674, 127.13985336944461);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(SEOUL);
            markerOptions.title("내 위치");
            markerOptions.snippet("설명");
            map.addMarker(markerOptions);

            map.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
            map.animateCamera(CameraUpdateFactory.zoomTo(10));
        }

}
