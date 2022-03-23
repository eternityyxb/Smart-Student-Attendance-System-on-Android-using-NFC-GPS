package com.example.AttendanceSystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomePage extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentID = user.getEmail();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    String roles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        DocumentReference roleref;
        roleref = firestore.collection("user").document(currentID);
        roleref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    String roles = task.getResult().getString("Roles");
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("roles", roles);
                    myEdit.commit();
                }else{
                }
            }
        });

        roleref = firestore.collection("user2").document(currentID);
        roleref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists()){
                    String roles = task.getResult().getString("Roles");
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("roles", roles);
                    myEdit.commit();
                }else{
                }
            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, new ProfileFragment(),"Profile").commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

            String roles = sh.getString("roles", "");
            System.out.println(roles);

            switch (item.getItemId()){
                case R.id.Profile:
                    selectedFragment = new ProfileFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, selectedFragment,"Profile").commit();
                    break;
                case R.id.Timetable:
                    if(roles.equals("Student"))
                    {
                        selectedFragment = new TimetableFragmentstudent();
                        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, selectedFragment,"Timetable").commit();
                        break;
                    }else
                    {
                        selectedFragment = new TimetableFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, selectedFragment,"Timetable").commit();
                        break;
                    }
                case R.id.Attendance:
                    if(roles.equals("Student"))
                    {
                        selectedFragment = new AttendanceFragment0();
                        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, selectedFragment,"Attendance").commit();
                        break;
                    }else
                    {
                        selectedFragment = new AttendanceFragment1();
                        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, selectedFragment,"Attendance").commit();
                        break;
                    }
            }

            return true;
        }
    };

    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.FragmentContainer);
        if (currentFragment.getTag().equals("Profile")) {
            AlertDialog.Builder ab = new AlertDialog.Builder(HomePage.this);
            ab.setTitle("");
            ab.setMessage("Do you want to exit?");
            ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finishAffinity();
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
        }else {
            BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
            bottomNav.setSelectedItemId(R.id.Profile);
            getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, new ProfileFragment(),"Profile").commit();
        }
    }

}