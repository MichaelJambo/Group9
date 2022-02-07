package com.example.nchikumbeapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nchikumbeapp.R;
import com.example.nchikumbeapp.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText Username,Mobile,Email,Password,mConfirm_Password;
    Button mbtnRegister;
    FirebaseAuth fAuth;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Username = findViewById(R.id.inputUsername);
        Mobile = findViewById(R.id.inputMobile);
        Email = findViewById(R.id.inputEmail);
        Password = findViewById(R.id.inputPassword);
        mConfirm_Password = findViewById(R.id.inputConfirmPassword);
        mbtnRegister = findViewById(R.id.btnRegister);

        fAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }

        mbtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();


                if (TextUtils.isEmpty(email)){
                    Email.setError("Email is required.");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Password.setError("Password is Required.");
                    return;
                }
                if (password.length() < 6){
                    Password.setError("Password must be greater than or equal to 6 characters");
                    return;
                }


                //register user in firebase

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                           // UserModel userModel = new UserModel();
                            String id = task.getResult().getUser().getUid();

                           // database.getReference().child("Users").child(id).setValue(userModel);

                            Toast.makeText(RegisterActivity.this, "Account Created Successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));

                        }else{
                            Toast.makeText(RegisterActivity.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });

        TextView btn=findViewById(R.id.alreadyHaveAccount);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
    }
}