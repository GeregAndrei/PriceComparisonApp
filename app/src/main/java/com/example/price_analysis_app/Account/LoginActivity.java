package com.example.price_analysis_app.Account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.price_analysis_app.R;
import com.example.price_analysis_app.uiStuff.DrawerActivity;
import com.example.price_analysis_app.uiStuff.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class LoginActivity extends DrawerActivity {

Account account=SessionManager.getCurrentAccount();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("TESTSTSTSTST",account.toString());
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_activity);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loginActivityLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EditText usernameEditText = findViewById(R.id.loginUsername);
        EditText passwordEditText = findViewById(R.id.loginPassword);

        Button register=findViewById(R.id.btnRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        Button loginButton = findViewById(R.id.btnLogIn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, get the Firebase user
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            // Optionally, fetch more user data from Firestore using the user's UID

                            // Navigate to HomeActivity
                            db.collection("conturi").document(firebaseUser.getUid()).get().addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    // Convert Firestore document to your Account object
                                    Account account = documentSnapshot.toObject(Account.class);
                                    // Store account in session for later use
                                    SessionManager.setCurrentAccount(account);
                                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    Log.d("Aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", account.toString());
                                    // Navigate to HomeActivity
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    finish();
                                } else {
                                    // Handle case where account details are missing
                                    Toast.makeText(LoginActivity.this, "Account details not found", Toast.LENGTH_SHORT).show();
                                    Log.e("Firestore", task.getException().toString());
                                }
                            }).addOnFailureListener(e -> {
                                // Handle Firestore retrieval error
                                Toast.makeText(LoginActivity.this, "Error fetching account details", Toast.LENGTH_SHORT).show();
                                Log.e("Firestore", "Error fetching account details", e);
                            });
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        Log.e("Authentication", "Authentication failed", task.getException());
                    }
                });

            }
        });


    }


}