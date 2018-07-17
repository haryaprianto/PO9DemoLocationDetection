package com.example.a16022596.po9demolocationdetection;

import android.Manifest;
import android.app.Activity;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


public class MainActivity extends AppCompatActivity {
    Button btnGetLocation;
    Button btnRecieveLocation;
    Button btnDeletelocation;
    FusedLocationProviderClient client;
    LocationCallback mLocationCallBack;
    LocationRequest mLocationRequest = LocationRequest.create();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        client = LocationServices.getFusedLocationProviderClient(this);

        mLocationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location data = locationResult.getLastLocation();
                    String msg = "Updated Location" + "\n" + "Lat : " + data.getLatitude() + " Lng : " + data.getLongitude();
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "No Last Location Found", Toast.LENGTH_SHORT).show();
                }
            };
        };

        btnGetLocation = findViewById(R.id.btnGetLastLocation);
        btnRecieveLocation = findViewById(R.id.btnRecieveLoc);
        btnDeletelocation = findViewById(R.id.btnDeleteLoc);



        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission() == true) {
                    Task<Location> task = client.getLastLocation();
                    task.addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                String msg = "Lat : " + location.getLatitude() + " Lng : " + location.getLongitude();
                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                            } else {
                                String msg = "No last Known Location found";
                                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT);
                            }
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Permission Not granted", Toast.LENGTH_SHORT);
                }
            }
        });

        btnDeletelocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                client.removeLocationUpdates(mLocationCallBack);

            }
        });

        btnRecieveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()==true){
                    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    mLocationRequest.setInterval(10000);
                    mLocationRequest.setFastestInterval(5000);
                    mLocationRequest.setSmallestDisplacement(100);
                    client = LocationServices.getFusedLocationProviderClient(MainActivity.this);

                    client.requestLocationUpdates(mLocationRequest,mLocationCallBack,null);
                }
                else{
                    String msg = "Permission not granted to retrieve location info";
                    Toast.makeText(MainActivity.this,msg,Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                }
            }
        });

            }

            private boolean checkPermission() {
                int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                        MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
                int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                        MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);

                if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED
                        || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED) {
                    return true;
                } else {
                    return false;
                }
            }
        }




