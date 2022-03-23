package com.example.AttendanceSystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.AttendanceSystem.func.AttendanceHistoryModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

public class HistoryActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    RecyclerView history;
    FirestoreRecyclerAdapter adapter;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentID = user.getEmail();
    String courseid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ImageButton backbtn;

        courseid = getIntent().getStringExtra("courseid");

        firebaseFirestore = FirebaseFirestore.getInstance();
        history = findViewById(R.id.history);

        //query
        Query query = firebaseFirestore.collection("attendance/"+courseid+"/studentlist/"+currentID+"/attendancelist");

        //recycler options
        FirestoreRecyclerOptions<AttendanceHistoryModel> options = new FirestoreRecyclerOptions.Builder<AttendanceHistoryModel>()
                .setQuery(query,AttendanceHistoryModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<AttendanceHistoryModel, TimetableViewHolder>(options) {
            @NonNull
            @NotNull
            @Override
            public HistoryActivity.TimetableViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_row_history, parent,false);
                return new TimetableViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull @NotNull HistoryActivity.TimetableViewHolder holder, int position, @NonNull AttendanceHistoryModel model) {
                holder.txt1.setText(model.getAttendanceid());
                holder.txt2.setText("Date: "+model.getAttendancedate());
                holder.txt3.setText("Time: "+model.getAttendancetime());
                holder.txt4.setText("Coordinate: "+model.getAttendancelocation());
                holder.txt5.setText("Status: "+model.getAttendancestatus());
            }
        };

        history.setHasFixedSize(true);
        history.setLayoutManager(new LinearLayoutManager(this));
        history.setAdapter(adapter);


        backbtn = findViewById(R.id.btn_back);
        backbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

    }

    private class TimetableViewHolder extends RecyclerView.ViewHolder{

        private TextView txt1, txt2, txt3, txt4,txt5;
        public TimetableViewHolder(@NonNull View itemView) {
            super(itemView);
            txt1 = itemView.findViewById(R.id.name);
            txt2 = itemView.findViewById(R.id.Date);
            txt3 = itemView.findViewById(R.id.Time);
            txt4 = itemView.findViewById(R.id.Location);
            txt5 = itemView.findViewById(R.id.status);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}


