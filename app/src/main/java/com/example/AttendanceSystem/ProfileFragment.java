package com.example.AttendanceSystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    DocumentReference reference, reference2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rooView = inflater.inflate(R.layout.fragment_profile, container, false);

        Button logoutbtn = rooView.findViewById(R.id.logout);
        TextView ID, Name, Email, Gender, IC, Programme, Intake, Nationality, Phone;
        TextView programmefield, intakefield, idf, namef, emailf, genderf, icf, naf, phonef;

        ID = rooView.findViewById(R.id.studentID1);
        Name = rooView.findViewById(R.id.studentName1);
        Email= rooView.findViewById(R.id.studentEmail1);
        Gender = rooView.findViewById(R.id.studentGender1);
        IC = rooView.findViewById(R.id.studentIC1);
        Programme = rooView.findViewById(R.id.studentProgramme1);
        programmefield = rooView.findViewById(R.id.studentProgramme);
        intakefield = rooView.findViewById(R.id.studentIntake);
        Intake= rooView.findViewById(R.id.studentIntake1);
        Nationality = rooView.findViewById(R.id.studentNationality1);
        Phone = rooView.findViewById(R.id.studentPhone1);
        idf = rooView.findViewById(R.id.studentID);
        namef = rooView.findViewById(R.id.studentName);
        emailf = rooView.findViewById(R.id.studentEmail);
        genderf = rooView.findViewById(R.id.studentGender);
        icf = rooView.findViewById(R.id.studentIC);
        naf = rooView.findViewById(R.id.studentNationality);
        phonef = rooView.findViewById(R.id.studentPhone);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentID = user.getEmail();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        reference = firestore.collection("user").document(currentID);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    String idr = task.getResult().getString("ID");
                    String namer = task.getResult().getString("Name");
                    String emailr = task.getResult().getString("Email");
                    String genderr = task.getResult().getString("Gender");
                    String icr = task.getResult().getString("IC");
                    String programmer = task.getResult().getString("Programme");
                    String intaker = task.getResult().getString("Intake");
                    String nationalityr = task.getResult().getString("Nationality");
                    String phoner = task.getResult().getString("Phone");

                    idf.setText("Student ID: ");
                    ID.setText(idr);
                    namef.setText("Name :");
                    Name.setText(namer);
                    emailf.setText("Email :");
                    Email.setText(emailr);
                    genderf.setText("Gender :");
                    Gender.setText(genderr);
                    icf.setText("NRIC :");
                    IC.setText(icr);
                    programmefield.setText("Programme :");
                    Programme.setText(programmer);
                    intakefield.setText("Intake :");
                    Intake.setText(intaker);
                    naf.setText("Nationality :");
                    Nationality.setText(nationalityr);
                    phonef.setText("Phone :");
                    Phone.setText(phoner);

                }else{

                }
            }
        });

        reference2 = firestore.collection("user2").document(currentID);
        reference2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    String idr = task.getResult().getString("ID");
                    String namer = task.getResult().getString("Name");
                    String emailr = task.getResult().getString("Email");
                    String genderr = task.getResult().getString("Gender");
                    String icr = task.getResult().getString("IC");
                    String programmer = task.getResult().getString("Position");
                    String intaker = task.getResult().getString("Faculty");
                    String nationalityr = task.getResult().getString("Nationality");
                    String phoner = task.getResult().getString("Phone");

                    idf.setText("Lecturer ID :");
                    ID.setText(idr);
                    namef.setText("Name :");
                    Name.setText(namer);
                    emailf.setText("Email :");
                    Email.setText(emailr);
                    genderf.setText("Gender :");
                    Gender.setText(genderr);
                    icf.setText("NRIC :");
                    IC.setText(icr);
                    programmefield.setText("Faculty :");
                    Programme.setText(intaker);
                    intakefield.setText("Position :");
                    Intake.setText(programmer);
                    naf.setText("Nationality :");
                    Nationality.setText(nationalityr);
                    phonef.setText("Phone :");
                    Phone.setText(phoner);

                }else{

                }
            }
        });

        logoutbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                ab.setTitle("");
                ab.setMessage("Do you want to logout?");
                ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getActivity().moveTaskToBack(true);
                        FirebaseAuth.getInstance().signOut();

                        startActivity(new Intent(getActivity(),LoginPage.class));
                        getActivity().finish();
                    }
                });
                ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        onResume();
                    }
                });
                ab.show();
            }
        });

        return rooView;
    }
}