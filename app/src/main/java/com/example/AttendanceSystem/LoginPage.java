package com.example.AttendanceSystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginPage extends AppCompatActivity {

    EditText memail, mpassword;
    ImageButton showPass;
    Button btnlogin, btnsignup;
    Toast toastMessage;
    int b=1;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        memail = (EditText)findViewById(R.id.login_email);
        mpassword = (EditText)findViewById(R.id.login_password);
        btnlogin = (Button)findViewById(R.id.btnsignin1);
        btnsignup = findViewById(R.id.btnsignup1);
        showPass = findViewById(R.id.togglePassword);

        if(mAuth.getCurrentUser()!=null){
            Intent intent = new Intent(getApplicationContext(), HomePage.class);
            startActivity(intent);
            finish();
        }

        showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (b==1) {
                    mpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    b=0;
                    showPass.setBackgroundResource(R.drawable.show_password);
                } else {
                    mpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    b=1;
                    showPass.setBackgroundResource(R.drawable.hide_password);
                }
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = memail.getText().toString().trim();
                String password = mpassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    memail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mpassword.setError("Password is Required.");
                    return;
                }

                //Authenticate User
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                                // user is verified, so you can finish this activity or send user to activity which you want.
                                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(LoginPage.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LoginPage.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpPg.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }

}