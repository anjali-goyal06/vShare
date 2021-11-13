package com.example.vshare.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vshare.databinding.ActivitySignInBinding;
import com.example.java.utilities.Constants;
import com.example.java.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class SignIn extends AppCompatActivity {

    ActivitySignInBinding binding;

    ProgressDialog progressDialog;
    FirebaseDatabase database;
    private PreferenceManager preferenceManager;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        preferenceManager = new PreferenceManager(getApplicationContext());

        progressDialog = new ProgressDialog(SignIn.this);
        progressDialog.setTitle("Login Accound");
        progressDialog.setMessage("Wait for a while");

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), UserListView.class));
            finish();
        }

        binding.signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, SignUp.class));
            }
        });

        //  Sign In page Activity
        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                auth.signInWithEmailAndPassword(binding.signInEmailAddress.getText().toString(),
                        binding.signInPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {

                            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                            String userId = auth.getCurrentUser().getUid();
                            DocumentReference documentReference = firestore.collection(Constants.KEY_COLLECTION_USERS).document(userId);
                            documentReference.addSnapshotListener(SignIn.this, new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                    preferenceManager.putString(Constants.KEY_FULL_NAME, value.getString(Constants.KEY_FULL_NAME));
                                    preferenceManager.putString(Constants.KEY_USER_ID, value.getId());
                                    preferenceManager.putString(Constants.KEY_EMAIL, value.getString(Constants.KEY_EMAIL));
                                }
                            });
                            Toast.makeText(SignIn.this, "done succcesssfulllyy", Toast.LENGTH_SHORT).show();

                        //    startActivity(new Intent(getApplicationContext(), UserListView.class));
                        //    finishAffinity();

                        } else {
                            Toast.makeText(SignIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}