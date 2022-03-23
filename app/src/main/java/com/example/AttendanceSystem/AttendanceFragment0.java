package com.example.AttendanceSystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.AttendanceSystem.func.AttendanceHistoryModel;
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

import org.jetbrains.annotations.NotNull;

import java.sql.Time;

public class AttendanceFragment0 extends Fragment {

    FirebaseFirestore firebaseFirestore;
    RecyclerView classes;
    FirestoreRecyclerAdapter adapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentID = user.getEmail();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rooView=inflater.inflate(R.layout.fragment_attendance0, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        classes = rooView.findViewById(R.id.classes);

        //query
        Query query = firebaseFirestore.collection("studentcourse/"+currentID+"/courselist");

        //recycler options
        FirestoreRecyclerOptions<TimetableModel> options = new FirestoreRecyclerOptions.Builder<TimetableModel>()
                .setQuery(query,TimetableModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<TimetableModel, AttendanceFragment0.TimetableViewHolder>(options) {
            @NonNull
            @NotNull
            @Override
            public AttendanceFragment0.TimetableViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_row_course, parent,false);
                return new AttendanceFragment0.TimetableViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull @NotNull AttendanceFragment0.TimetableViewHolder holder, int position, @NonNull TimetableModel model) {
                holder.txt1.setText(model.getCoursename());
                holder.itemView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(getContext(), TakeAttendance.class);
                        intent.putExtra("courseid", model.getCourseid());
                        getContext().startActivity(intent);
                    }
                });
            }
        };

        classes.setHasFixedSize(true);
        classes.setLayoutManager(new LinearLayoutManager(getContext()));
        classes.setAdapter(adapter);


        return rooView;
    }

    private class TimetableViewHolder extends RecyclerView.ViewHolder{
        private TextView txt1;
        public TimetableViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            txt1 = itemView.findViewById(R.id.name);
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
