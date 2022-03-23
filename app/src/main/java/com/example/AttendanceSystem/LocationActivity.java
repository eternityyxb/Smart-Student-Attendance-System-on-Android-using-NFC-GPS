package com.example.AttendanceSystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LocationActivity extends AppCompatActivity implements LocationListener {


    private static final int PERMISSIONS_CODE = 1;
    LocationManager locationManager;
    Button getLocationBtn;

    String tagcode;
    String courseid;
    String date;
    String nfc;
    String position;
    String time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        courseid = getIntent().getStringExtra("courseid");
        nfc = getIntent().getStringExtra("nfc");
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        tagcode = sh.getString("tagcode", "");

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        month = month + 1;
        date = day + "-" + month + "-" + year;

        DateFormat df = new SimpleDateFormat("h:mm a, z");
        time = df.format(Calendar.getInstance().getTime());

        if(!tagcode.equals(nfc))
        {
            Toast.makeText(this, "Wrong NFC Code", Toast.LENGTH_LONG).show();
            finish();
            return;
        }


        getLocationBtn = (Button) findViewById(R.id.read_gps);

        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });
    }

    void getLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            //Request gps permission
            String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
            String[] permissions2 = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(LocationActivity.this, permissions, PERMISSIONS_CODE);
            ActivityCompat.requestPermissions(LocationActivity.this, permissions2, PERMISSIONS_CODE);

            System.out.println("dead");
            return;
        }
        //Access location coordinates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, (LocationListener) this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(ACCESS_FINE_LOCATION)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        getLocation();
                    } else {
                        ActivityCompat.requestPermissions(LocationActivity.this, permissions, PERMISSIONS_CODE);
                        getLocation();
                    }
                }
            }
        }
    }

    public void onLocationChanged(Location location) {
        if (location != null) {
            locationManager.removeUpdates(this);
            position = location.getLatitude() + ", " + location.getLongitude();

            Intent intent = new Intent(LocationActivity.this, AttendanceResult.class);
            intent.putExtra("courseid", courseid);
            intent.putExtra("nfc", nfc);
            intent.putExtra("date", date);
            intent.putExtra("location", position);
            intent.putExtra("time", time);
            startActivity(intent);
            finish();
        }
    }


    public void onProviderDisabled(String provider) {
        Toast.makeText(LocationActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }


    public void onStatusChanged(String provider, int status, Bundle extras) {

    }


    public void onProviderEnabled(String provider) {

    }

    public void onBackPressed(){
        finish();
    }
}