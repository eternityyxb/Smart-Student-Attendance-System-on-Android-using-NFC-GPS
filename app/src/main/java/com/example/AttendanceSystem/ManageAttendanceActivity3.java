package com.example.AttendanceSystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.AttendanceSystem.func.AttendanceHistoryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ManageAttendanceActivity3 extends AppCompatActivity {

    String courseid, userid, attendancedate, attendancetime, attendanceid, attendancelocation, attendancestatus;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentID = user.getEmail();
    DocumentReference newref, userref;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_attendance3);

        ImageButton backbtn;
        Button back, delete;
        TextView date, date1, time, time1, location, location1, status, status1, notes, notes1, name, name1;
        EditText inputnote;

        date = findViewById(R.id.date);
        date1 = findViewById(R.id.date1);
        time = findViewById(R.id.time);
        time1 = findViewById(R.id.time1);
        location = findViewById(R.id.location);
        location1 = findViewById(R.id.location1);
        notes = findViewById(R.id.notes);
        notes1 = findViewById(R.id.notes1);
        status = findViewById(R.id.status);
        status1 = findViewById(R.id.status1);
        inputnote = findViewById(R.id.inputnote);
        name = findViewById(R.id.name);
        name1 = findViewById(R.id.name1);
        delete = findViewById(R.id.delete);

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        courseid = sh.getString("courseid", "");
        userid = sh.getString("userid", "");
        attendancedate = sh.getString("attendancedate", "");
        attendancetime = sh.getString("attendancetime", "");
        attendancelocation = sh.getString("attendancelocation", "");
        attendancestatus = sh.getString("attendancestatus", "");
        attendanceid = sh.getString("attendancedate", "");

        DocumentReference username = firestore.collection("user").document(userid);
        username.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    String username = task.getResult().getString("Name");
                    name.setText("Name: ");
                    name1.setText(username);
                }else{

                }
            }
        });

        userref = firestore.collection("attendance/"+courseid+"/studentlist/"+userid+"/attendancelist").document(attendancedate);
        userref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    String date2 = task.getResult().getString("attendancedate");
                    String time2 = task.getResult().getString("attendancetime");
                    String location2 = task.getResult().getString("attendancelocation");
                    String status2 = task.getResult().getString("attendancestatus");
                    String notes2 = task.getResult().getString("notes");

                    date.setText("Date: ");
                    date1.setText(date2);
                    time.setText("Time: ");
                    time1.setText(time2);
                    location.setText("Location: ");
                    location1.setText(location2);
                    status.setText("Status: ");
                    status1.setText(status2);
                    notes.setText("Notes: ");
                    notes1.setText(notes2);

                }else{

                }
            }
        });

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
                String note = inputnote.getText().toString();
                newref = firestore.collection("attendance/"+courseid+"/studentlist/"+userid+"/attendancelist").document(attendancedate);
                Map<String, Object> ggbData = new HashMap<>();
                ggbData.put("attendanceid", attendanceid);
                ggbData.put("attendancedate", attendancedate);
                ggbData.put("attendancetime", attendancetime);
                ggbData.put("attendancelocation", attendancelocation);
                ggbData.put("attendancestatus", attendancestatus);
                ggbData.put("notes", note);
                ggbData.put("userid", userid);
                newref.set(ggbData);
                Toast.makeText(ManageAttendanceActivity3.this, "Successfully Updated Record", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                firestore.collection("attendance/"+courseid+"/studentlist/"+userid+"/attendancelist").document(attendancedate)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ManageAttendanceActivity3.this, "Successfully Deleted Record", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                finish();
            }
        });

    }

    public void onBackPressed(){
        finish();
    }

}

