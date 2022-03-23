package com.example.AttendanceSystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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

import com.example.AttendanceSystem.func.StudentlistModel;
import com.example.AttendanceSystem.func.TimetableModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ManageAttendanceActivity extends AppCompatActivity {

    String courseid;
    FirebaseFirestore firebaseFirestore, firestore, firestore2;
    RecyclerView studentlist;
    FirestoreRecyclerAdapter adapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentID = user.getEmail();
    Button export;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_attendance);

        courseid = getIntent().getStringExtra("courseid");

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("courseid", courseid);
        myEdit.commit();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ManageAttendanceActivity.this);
        SharedPreferences.Editor editor = prefs.edit();

        ImageButton backbtn;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firestore2 = FirebaseFirestore.getInstance();
        studentlist = findViewById(R.id.studentlist);

        firebaseFirestore.collection("attendance/"+courseid+"/studentlist")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> namelist = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        String userid = document.getId();

                        DocumentReference reference = firestore2.collection("attendance/"+courseid+"/studentlist").document(userid);
                        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.getResult().exists()){
                                    String name = task.getResult().getString("name");
                                    //namelist.add("\n"+userid);
                                    //namelist.add(name);
                                    namelist.add(userid);
                                    namelist.add("\n."+name);
                                    System.out.println(namelist);
                                    Gson gson = new Gson();
                                    String json = gson.toJson(namelist);
                                    editor.putString("namelist", json);
                                    editor.apply();
                                }
                            }

                        });


                    }
                }
            }
        });

        firebaseFirestore.collection("attendance/"+courseid+"/studentlist")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> datelist = new ArrayList<>();
                    List<String> attendanceratelist = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        final int[] count = {0,1};
                        final int[] attendancerate = {0};
                        String userid = document.getId();
                        firestore.collection("attendance/"+courseid+"/studentlist/"+userid+"/attendancelist/")
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        int i = attendancerate[0]++;
                                        i++;

                                        if(count[0] ==0){
                                            String date = doc.getId();
                                            datelist.add("\n."+date);
                                            count[0]++;
                                        }else
                                        {
                                            String date = doc.getId();
                                            datelist.add(date);
                                        }
                                        Gson gson = new Gson();
                                        String json = gson.toJson(datelist);
                                        editor.putString("datelist", json);
                                        editor.apply();

                                        String a = Integer.toString(i);

                                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                        myEdit.putString("attendancerate", a);
                                        myEdit.commit();

                                    }

                                    SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                                    String attendancerate = sh.getString("attendancerate", "");

                                    attendanceratelist.add("\n."+attendancerate);

                                    Gson gson = new Gson();
                                    String json = gson.toJson(attendanceratelist);
                                    editor.putString("attendancerate", json);
                                    editor.apply();

                                }
                            }
                        });

                    }
                }
            }
        });

        //query
        Query query = firebaseFirestore.collection("attendance/"+courseid+"/studentlist");
        //Query query = firebaseFirestore.collection("attendance/are4C0MnvTuQJxrtQdUi/studentlist");

        //recycler options
        FirestoreRecyclerOptions<StudentlistModel> options = new FirestoreRecyclerOptions.Builder<StudentlistModel>()
                .setQuery(query, StudentlistModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<StudentlistModel, ManageAttendanceActivity.StudentlistViewHolder>(options) {
            @NonNull
            @NotNull
            @Override
            public ManageAttendanceActivity.StudentlistViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_row_studentlist, parent,false);
                return new ManageAttendanceActivity.StudentlistViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull @NotNull ManageAttendanceActivity.StudentlistViewHolder holder, int position, @NonNull StudentlistModel model) {
                holder.txt1.setText(model.getName());
                holder.txt2.setText(model.getID());
                holder.itemView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(ManageAttendanceActivity.this, ManageAttendanceActivity2.class);
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        myEdit.putString("userid", model.getUserid());
                        myEdit.commit();
                        startActivity(intent);
                    }
                });
            }
        };

        studentlist.setHasFixedSize(true);
        studentlist.setLayoutManager(new LinearLayoutManager(this));
        studentlist.setAdapter(adapter);

        backbtn = findViewById(R.id.btn_back);
        backbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        export = findViewById(R.id.export);
        export.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ManageAttendanceActivity.this, ExportActivity.class);
                startActivity(intent);
            }
        });

    }

    private class StudentlistViewHolder extends RecyclerView.ViewHolder{
        private TextView txt1, txt2;
        public StudentlistViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            txt1 = itemView.findViewById(R.id.name);
            txt2 = itemView.findViewById(R.id.id);
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