package com.example.youtubefirebaseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseFirestore mStore=FirebaseFirestore.getInstance();

    private EditText mEmailText, mPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mEmailText=findViewById(R.id.sign_email);
        mPasswordText=findViewById(R.id.sign_password);

        findViewById(R.id.sign_success).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mAuth.createUserWithEmailAndPassword(mEmailText.getText().toString().trim(), mPasswordText.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if(task.isSuccessful()){
                            FirebaseUser user=mAuth.getCurrentUser();
                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
                            if(user!=null){
                                HashMap<String, Object> userMap = new HashMap<>();
                                userMap.put("FirebaseID.documentID",user.getUid());
                                userMap.put("FirebaseID.email",mEmailText.getText().toString().trim());
                                userMap.put("FirebaseID.password",mPasswordText.getText().toString().trim());
                                mStore.collection(FirebaseID.user).document(user.getUid()).set(userMap, SetOptions.merge());
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("Users");
                                reference.child(user.getUid()).setValue(userMap);

                                finish();
                            }
                        }else{
                            Toast.makeText(SignupActivity.this, "Sign up error.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}