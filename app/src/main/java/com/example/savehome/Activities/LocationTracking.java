package com.example.savehome.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.example.savehome.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationTracking extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.IvBackLocationTracking)
    ImageView IvBackLocationTracking;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etCurrentLocation)
    TextView etCurrentLocation;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.etSelectDestination)
    TextView etSelectDestination;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btnStartJourney)
    Button btnStartJourney;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.C_Layout_Current_Location)
    ConstraintLayout C_Layout_Current_Location;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.C_Layout_select_destination)
    ConstraintLayout C_Layout_select_destination;

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_tracking);
        ButterKnife.bind(this);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);

        // initialize fused location
        client = LocationServices.getFusedLocationProviderClient(this);

        // check permission
        if (ActivityCompat.checkSelfPermission(LocationTracking.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //when permission granted
            //call method
            getCurrentLocation();

        } else {
            // when permission denied
            //request permission
            ActivityCompat.requestPermissions(LocationTracking.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);

        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.C_Layout_Current_Location, R.id.C_Layout_select_destination, R.id.btnStartJourney})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.C_Layout_Current_Location:
            case R.id.C_Layout_select_destination:
                startActivity(new Intent(LocationTracking.this, SearchActivity.class));
                break;
            case R.id.btnStartJourney:
                startActivity(new Intent(LocationTracking.this, LocationTrackingShare.class));
                break;
            case R.id.IvBackLocationTracking:
                startActivity(new Intent(LocationTracking.this, HomeScreenActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;


        }
    }

    private void getCurrentLocation() {
        //initialize task location

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(location -> {
            //when success
            if (location != null) {
                supportMapFragment.getMapAsync(googleMap -> {
                    //initialize last location
                    LatLng latLng = new LatLng(location.getLatitude()
                            , location.getLongitude());
                    //create marker option
                    MarkerOptions options = new MarkerOptions().position(latLng)
                            .title("I am there");
                    //zoom Map
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    //Add marker or map
                    googleMap.addMarker(options);

                });
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // When permission granted
                // call method
                getCurrentLocation();
            }
        }
    }

}