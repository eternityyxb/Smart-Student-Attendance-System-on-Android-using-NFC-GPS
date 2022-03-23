package com.example.AttendanceSystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class TakeAttendance extends AppCompatActivity{

    String courseid;
    private Button mBtRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        courseid = getIntent().getStringExtra("courseid");
        ImageButton backbtn;

        mBtRead = findViewById(R.id.btn_read);
        Button viewbtn = findViewById(R.id.view);
        ImageButton imageButton = findViewById(R.id.imageButton);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        DocumentReference classref;
        classref = firestore.collection("course").document(courseid);
        classref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    String classid = task.getResult().getString("classid");
                    // Storing data into SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);

                    // Creating an Editor object to edit(write to the file)
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();

                    // Storing the key and its value as the data fetched from edittext
                    myEdit.putString("classno", classid);
                    System.out.println(classid);

                    // Once the changes have been made,
                    // we need to commit to apply those changes made,
                    // otherwise, it will throw an error
                    myEdit.commit();
                }else{

                }
            }
        });


        mBtRead.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //to take attendance using nfc and gps
                Intent intent = new Intent(TakeAttendance.this, readnfc.class);
                intent.putExtra("courseid", courseid);
                startActivity(intent);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //to take attendance using nfc and gps
                Intent intent = new Intent(TakeAttendance.this, readnfc.class);
                intent.putExtra("courseid", courseid);
                startActivity(intent);
            }
        });

        backbtn = findViewById(R.id.btn_back);
        backbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        viewbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //to view attendance history page
                Intent intent = new Intent(TakeAttendance.this, HistoryActivity.class);
                intent.putExtra("courseid", courseid);
                startActivity(intent);
            }
        });
    }

}