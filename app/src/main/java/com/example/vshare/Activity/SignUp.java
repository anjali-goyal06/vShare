package com.example.vshare.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vshare.databinding.ActivitySignUpBinding;
import com.example.vshare.utilities.Constants;
import com.example.vshare.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    ActivitySignUpBinding binding;

    private FirebaseAuth auth;
    FirebaseFirestore firestore;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    String TAG = "jjjjj";
    private PreferenceManager preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(SignUp.this);
        progressDialog.setTitle("Creating Account");
        preferenceManager = new PreferenceManager(getApplicationContext());

        binding.signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, SignIn.class));
            }
        });

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), UserListView.class));
            finish();
        }


        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                Log.i(TAG,"id = "+ binding.signUpEmailAddress.getText().toString() +" "+ binding.signUpPassword.getText().toString() );

                auth.createUserWithEmailAndPassword
                        (binding.signUpEmailAddress.getText().toString(),binding.signUpPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                Log.i(TAG,"task = " + task);
                                if(task.isSuccessful()){

                                    FirebaseUser fuser = auth.getCurrentUser();
                                    fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(SignUp.this, "Verification Email has been sent.", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SignUp.this, "Error! Email can't be sent. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                               //     Toast.makeText(SignUp.this, "User Created!", Toast.LENGTH_SHORT).show();

                                    String userId = fuser.getUid();
                                    DocumentReference documentReference = firestore.collection("users").document(userId);
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("fName",binding.signUpPersonName.getText().toString());
                                    user.put("email", binding.signUpEmailAddress.getText().toString());
                                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.i("ccchhh","done");
                                            Toast.makeText(SignUp.this,"document sucessfill , ",Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.i("ccchhh","faiiillll");
                                            Toast.makeText(SignUp.this,"document sfaildded , ",Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                    preferenceManager.putString(Constants.KEY_FULL_NAME,binding.signUpPersonName.getText().toString() );
                                    preferenceManager.putString(Constants.KEY_EMAIL, binding.signUpEmailAddress.getText().toString());
                                    preferenceManager.putString(Constants.KEY_USER_ID, userId);


//                                    User user = new User(binding.signUpPersonName.getText().toString(),binding.signUpEmailAddress.getText().toString(),
//                                            binding.signUpPassword.getText().toString());
//                                    Log.i(TAG,user.toString());
//                                    String id = task.getResult().getUser().getUid();
//                                    Log.i(TAG,"id = "+ id);
//                                    database.getReference().child("Users").child(id).setValue(user);
                                  //  Toast.makeText(SignUp.this,"Sign Up Successfully",Toast.LENGTH_SHORT).show();
                                 //   startActivity(new Intent(getApplicationContext(), UserListView.class));
                                }else{
                                    Toast.makeText(SignUp.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    Log.i(TAG,"task ===== " + task.getException().getMessage().toString());
                                }
                            }
                        });
            }
        });

    }
}