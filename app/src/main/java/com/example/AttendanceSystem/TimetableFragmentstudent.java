package com.example.AttendanceSystem;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.AttendanceSystem.func.TimetableModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

public class TimetableFragmentstudent extends Fragment {

    FirebaseFirestore firebaseFirestore;
    RecyclerView timetable;
    FirestoreRecyclerAdapter adapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentID = user.getEmail();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rooView=inflater.inflate(R.layout.fragment_timetable, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        timetable = rooView.findViewById(R.id.timetable);

        //query
        Query query = firebaseFirestore.collection("studentcourse/"+currentID+"/courselist");

        //recycler options
        FirestoreRecyclerOptions<TimetableModel> options = new FirestoreRecyclerOptions.Builder<TimetableModel>()
                .setQuery(query,TimetableModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<TimetableModel, TimetableFragmentstudent.TimetableViewHolder>(options) {
            @NonNull
            @NotNull
            @Override
            public TimetableFragmentstudent.TimetableViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_row_timetable, parent,false);
                return new TimetableFragmentstudent.TimetableViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull @NotNull TimetableFragmentstudent.TimetableViewHolder holder, int position, @NonNull TimetableModel model) {
                holder.txt1.setText(model.getCoursename());
                holder.txt2.setText("Date: "+model.getCoursedate());
                holder.txt3.setText("Time: "+model.getCoursetime());
                holder.txt4.setText("Venue: "+model.getClassname());
            }
        };

        timetable.setHasFixedSize(true);
        timetable.setLayoutManager(new LinearLayoutManager(getContext()));
        timetable.setAdapter(adapter);


        return rooView;
    }

    private class TimetableViewHolder extends RecyclerView.ViewHolder{
        private TextView txt1, txt2, txt3, txt4;
        public TimetableViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            txt1 = itemView.findViewById(R.id.name);
            txt2 = itemView.findViewById(R.id.Date);
            txt3 = itemView.findViewById(R.id.Time);
            txt4 = itemView.findViewById(R.id.Location);
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
