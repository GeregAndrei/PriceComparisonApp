package com.example.price_analysis_app.Account;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.price_analysis_app.R;
import com.example.price_analysis_app.uiStuff.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    String password = "";
    String email = "";
    String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText usernameEditText = findViewById(R.id.registerUsername2);
        EditText passwordEditText = findViewById(R.id.registerPassword);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Button registerButton = findViewById(R.id.btnRegister2);
        registerButton.setOnClickListener(v -> {

            email = usernameEditText.getText().toString().trim();
            password = passwordEditText.getText().toString().trim();
            username = usernameEditText.getText().toString().trim();
            registerUser(email, password, username, db, mAuth);
        });
    }

    private void registerUser(String email, String password, String username, FirebaseFirestore db, FirebaseAuth mAuth) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {

                            Account account = new Account();
                            account.setUsername(username);
                            account.setEmail(email);

                            account.setPassword(password);
                            account.setBookmarkedItems(new ArrayList<>());
                            account.setId("1234567890");



                            db.collection("conturi").document(firebaseUser.getUid())
                                    .set(account)
                                    .addOnSuccessListener(aVoid -> {

                                        SessionManager.setCurrentAccount(account);

                                        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(RegisterActivity.this, "Failed to save account details.", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {

                        Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

}