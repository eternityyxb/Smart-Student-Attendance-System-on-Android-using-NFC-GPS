package com.example.AttendanceSystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AttendanceResult extends AppCompatActivity {

    String courseid;
    String date;
    String nfc;
    String position;
    String time;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentID = user.getEmail();
    DocumentReference attendancereference;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_result);

        ImageButton backbtn;
        Button back;
        TextView dateq, timeq, locationq, statusq;
        TextView datef, timef, locationf, statusf;

        courseid = getIntent().getStringExtra("courseid");
        nfc = getIntent().getStringExtra("nfc");
        date = getIntent().getStringExtra("date");
        position = getIntent().getStringExtra("location");
        time = getIntent().getStringExtra("time");

        dateq = findViewById(R.id.date);
        timeq = findViewById(R.id.time);
        locationq = findViewById(R.id.location);
        statusq = findViewById(R.id.status);

        datef = findViewById(R.id.date1);
        timef = findViewById(R.id.time1);
        locationf = findViewById(R.id.location1);
        statusf = findViewById(R.id.status1);

        dateq.setText("Date: ");
        datef.setText(date);
        timeq.setText("Time: ");
        timef.setText(time);
        locationq.setText("Coordinate: ");
        locationf.setText(position);
        statusq.setText("Status: ");
        statusf.setText("Present (Y)");

        attendancereference = firestore.collection("attendance/"+courseid+"/studentlist/"+currentID+"/attendancelist").document(date);
        Map<String, Object> ggbData = new HashMap<>();
        ggbData.put("attendanceid", date);
        ggbData.put("attendancedate", date);
        ggbData.put("attendancetime", time);
        ggbData.put("attendancelocation", position);
        ggbData.put("attendancestatus", "Y");
        ggbData.put("notes", "");
        ggbData.put("userid", currentID);
        attendancereference.set(ggbData);
        Toast.makeText(AttendanceResult.this, "Attendance Recorded Successfully", Toast.LENGTH_SHORT).show();

        backbtn = findViewById(R.id.btn_back);
        backbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

    }

    public void onBackPressed(){
        finish();
    }
}