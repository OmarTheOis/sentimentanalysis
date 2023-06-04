package com.example.sentimentanalysis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class register extends AppCompatActivity {
    // create object of datareference class to access firebase's realtime database
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://sentimentanalysis-dd0c6-default-rtdb.firebaseio.com");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText fullname = findViewById(R.id.fullname);
        final EditText email = findViewById(R.id.email);
        final EditText phone = findViewById(R.id.phone);
        final EditText password = findViewById(R.id.password);
        final EditText conPassword = findViewById(R.id.conPassword);
        final Button registerBtn = findViewById(R.id.registerNowBtn);
        final TextView loginNowBtn = findViewById(R.id.LoginNow);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get data from EditTexts into Striing Variable
                final String fullnameTxt = fullname.getText().toString();
                final String emailTxt = email.getText().toString();
                final String phoneTxt = phone.getText().toString();
                final String passwordTxt = password.getText().toString();
                final String conPasswordTxt = conPassword.getText().toString();

                // check if user fill all field before send
                if(fullnameTxt.isEmpty()|| emailTxt.isEmpty()||phoneTxt.isEmpty()||passwordTxt.isEmpty()){
                    Toast.makeText(register.this,"please fill all field",Toast.LENGTH_LONG).show();
                }
                // check if password not match
                else if (!passwordTxt.equals(conPasswordTxt)) {
                    Toast.makeText(register.this,"Password are not matching",Toast.LENGTH_SHORT).show();

                }
                else {

                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // check if phone not registered before
                            if (snapshot.hasChild(phoneTxt)) {
                                Toast.makeText(register.this,"phone already registered ",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                //send data to firebase realtime database
                                //we are using phone number as uniqe idetity of every user
                                // so all the other details of users comes under phone number
                                databaseReference.child("user").child(phoneTxt).child("fullname").setValue(fullnameTxt);
                                databaseReference.child("user").child(phoneTxt).child("email").setValue(emailTxt);
                                databaseReference.child("user").child(phoneTxt).child("password").setValue(passwordTxt);
                                // show seccess message then finish activty
                                Toast.makeText(register.this,"user registered seccessfuly",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }


            }
        });
        loginNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}