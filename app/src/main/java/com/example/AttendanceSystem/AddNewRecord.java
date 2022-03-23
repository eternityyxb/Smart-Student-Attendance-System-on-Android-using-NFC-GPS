package com.example.AttendanceSystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddNewRecord extends AppCompatActivity {

    String courseid, userid, attendancedate, attendancetime, attendanceid, attendancelocation, attendancestatus;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DocumentReference attendancereference, userref;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_record);

        ImageButton backbtn;
        Button back;
        TextView date, date1, time, time1, location, location1, status, status1, notes, notes1, name, name1;
        String datee, timee;
        EditText inputnote, inputdate;

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        courseid = sh.getString("courseid", "");
        userid = sh.getString("userid", "");

        inputnote = findViewById(R.id.inputnote);
        name = findViewById(R.id.name);
        name1 = findViewById(R.id.name1);
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
        inputdate = findViewById(R.id.inputdate);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        month = month + 1;
        datee = day + "-" + month + "-" + year;

        DateFormat df = new SimpleDateFormat("h:mm a, z");
        timee = df.format(Calendar.getInstance().getTime());

        System.out.println(datee);
        System.out.println(timee);

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


                    date.setText("Date: ");
                    date1.setText(datee);
                    time.setText("Time: ");
                    time1.setText(timee);
                    location.setText("Location: ");
                    location1.setText("-");
                    status.setText("Status: ");
                    status1.setText("Y");
                    notes.setText("Notes: ");
                    notes1.setText("");


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
                String newdate = inputdate.getText().toString();

                if(TextUtils.isEmpty(newdate)){
                    attendancereference = firestore.collection("attendance/"+courseid+"/studentlist/"+userid+"/attendancelist").document(datee);
                    Map<String, Object> ggbData = new HashMap<>();
                    ggbData.put("attendanceid", datee);
                    ggbData.put("attendancedate", datee);
                    ggbData.put("attendancetime", timee);
                    ggbData.put("attendancelocation", "");
                    ggbData.put("attendancestatus", "Y");
                    ggbData.put("notes", note);
                    ggbData.put("userid", userid);
                    attendancereference.set(ggbData);
                    Toast.makeText(AddNewRecord.this, "Successfully Added New Record", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    {
                        String datee = inputdate.getText().toString();
                        attendancereference = firestore.collection("attendance/"+courseid+"/studentlist/"+userid+"/attendancelist").document(datee);
                        Map<String, Object> ggbData = new HashMap<>();
                        ggbData.put("attendanceid", datee);
                        ggbData.put("attendancedate", datee);
                        ggbData.put("attendancetime", timee);
                        ggbData.put("attendancelocation", "");
                        ggbData.put("attendancestatus", "Y");
                        ggbData.put("notes", note);
                        ggbData.put("userid", userid);
                        attendancereference.set(ggbData);
                        Toast.makeText(AddNewRecord.this, "Successfully Added New Record", Toast.LENGTH_SHORT).show();
                        finish();
                    }
            }
        });

    }
}