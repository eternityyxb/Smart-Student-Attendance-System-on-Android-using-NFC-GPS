package com.example.AttendanceSystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.AttendanceSystem.func.AttendanceHistoryModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ManageAttendanceActivity2 extends AppCompatActivity {

    String courseid, userid;
    TextView instruction;
    FirebaseFirestore firebaseFirestore;
    RecyclerView studentattendance;
    FirestoreRecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_attendance2);

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        courseid = sh.getString("courseid", "");
        userid = sh.getString("userid", "");

        instruction = findViewById(R.id.instruction);

        ImageButton backbtn;
        Button add, export;
        firebaseFirestore = FirebaseFirestore.getInstance();
        studentattendance = findViewById(R.id.attendancelist);

        DocumentReference username = firebaseFirestore.collection("user").document(userid);
        username.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    String username = task.getResult().getString("Name");
                    instruction.setText("Student Name: \n"+username);
                }else{

                }
            }
        });

        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ManageAttendanceActivity2.this, AddNewRecord.class);
                startActivity(intent);
            }
        });

        //query
        Query query = firebaseFirestore.collection("attendance/"+courseid+"/studentlist/"+userid+"/attendancelist");

        //recycler options
        FirestoreRecyclerOptions<AttendanceHistoryModel> options = new FirestoreRecyclerOptions.Builder<AttendanceHistoryModel>()
                .setQuery(query, AttendanceHistoryModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<AttendanceHistoryModel, ManageAttendanceActivity2.AttendanceListViewHolder>(options) {
            @NonNull
            @NotNull
            @Override
            public ManageAttendanceActivity2.AttendanceListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_row_attendance_manage, parent,false);
                return new ManageAttendanceActivity2.AttendanceListViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull @NotNull ManageAttendanceActivity2.AttendanceListViewHolder holder, int position, @NonNull AttendanceHistoryModel model) {
                holder.txt1.setText(model.getAttendanceid());
                holder.txt2.setText("Date: "+model.getAttendancedate());
                holder.txt3.setText("Time: "+model.getAttendancetime());
                holder.txt4.setText("Location: "+model.getAttendancelocation());
                holder.txt5.setText("Status: "+model.getAttendancestatus());
                holder.txt6.setText("Notes: "+model.getNotes());
                holder.itemView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(ManageAttendanceActivity2.this, ManageAttendanceActivity3.class);
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        myEdit.putString("attendancedate", model.getAttendancedate());
                        myEdit.putString("attendancetime", model.getAttendancetime());
                        myEdit.putString("attendancelocation", model.getAttendancelocation());
                        myEdit.putString("attendancestatus", model.getAttendancestatus());
                        myEdit.commit();
                        startActivity(intent);
                    }
                });
            }
        };

        studentattendance.setHasFixedSize(true);
        studentattendance.setLayoutManager(new LinearLayoutManager(this));
        studentattendance.setAdapter(adapter);

        backbtn = findViewById(R.id.btn_back);
        backbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

    }

    private class AttendanceListViewHolder extends RecyclerView.ViewHolder{
        private TextView txt1, txt2, txt3, txt4, txt5, txt6;
        public AttendanceListViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            txt1 = itemView.findViewById(R.id.name);
            txt2 = itemView.findViewById(R.id.Date);
            txt3 = itemView.findViewById(R.id.Time);
            txt4 = itemView.findViewById(R.id.Location);
            txt5 = itemView.findViewById(R.id.status);
            txt6 = itemView.findViewById(R.id.notes);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}