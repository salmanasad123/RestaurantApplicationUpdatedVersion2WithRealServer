package com.example.salman.restaurantapplication;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import org.greenrobot.eventbus.EventBus;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MTAG";

    SharedPreferences sharedPreferences;
    String username;

    Button b1;
    Button location;
    private LocationManager locationManager;
    private LocationListener locationListener;

    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("loginInfo", MODE_PRIVATE);

        location = findViewById(R.id.btnLocation);

        b1 = findViewById(R.id.btn_continue);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LocationEvent locationEvent = new LocationEvent(longitude, latitude);
                EventBus.getDefault().postSticky(locationEvent);


                username = sharedPreferences.getString("username", "x");

                if (username != "x") {
                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //tv.append("\n " + location.getLatitude() + " " + location.getLongitude());


                longitude = location.getLongitude();
                latitude = location.getLatitude();

                if (longitude != 0.0 && latitude != 0.0) {
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                }
                Toast.makeText(MainActivity.this, "Location Acquired", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);

                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.


                return;
            }
        } else {
            configureButton();
        }
        locationManager.requestLocationUpdates("gps", 1200000, 0, locationListener);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    ;
                configureButton();
                return;
        }
    }

    private void configureButton() {
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                locationManager.requestLocationUpdates("gps", 1200000, 0, locationListener);
            }
        });

    }
}




