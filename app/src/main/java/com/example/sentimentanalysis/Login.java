package com.example.sentimentanalysis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {


    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://sentimentanalysis-dd0c6-default-rtdb.firebaseio.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText phone = findViewById(R.id.email);
        final EditText password = findViewById(R.id.password);
        final Button loginBtn = findViewById(R.id.loginBtn);
        final View registerNowBtn = findViewById(R.id.registerNowBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String phoneTxt = phone.getText().toString();
                final String passwordTxt = password.getText().toString();

                if(phoneTxt.isEmpty() || passwordTxt.isEmpty()) {
                    Toast.makeText(Login.this, "please enter your mobile or password ", Toast.LENGTH_SHORT).show();
                }
                else{
                    databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // check if mobile/phone is exist iid firebase database
                            if(snapshot.hasChild(phoneTxt)){
                                //mobiile iis exist in firebase database
                                //now get password of user from firebase data and match it with entered password
                                final String getpassword = snapshot.child(phoneTxt).child("password").getValue(String.class);
                                if (getpassword.equals(passwordTxt)){
                                    Toast.makeText(Login.this,"Seccessfully Logged in",Toast.LENGTH_SHORT).show();

                                    // open mainactivity on success
                                    startActivity(new Intent(Login.this, Home.class));
                                    finish();
                                }
                                else {
                                    Toast.makeText(Login.this,"wrong password",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(Login.this,"wrong mobile",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                }
        });
        registerNowBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // open Register activty
                startActivity(new Intent(Login.this,register.class));
            }
            });

        }

        }
