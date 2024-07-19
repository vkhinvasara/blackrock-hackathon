package com.voldy.blackrock.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.voldy.blackrock.MainActivity;
import com.voldy.blackrock.databinding.ActivityLoginBinding;
import com.google.android.material.snackbar.Snackbar;
import com.voldy.blackrock.model.UserRegistration;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "user_details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

        setEventListeners();
    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
    }

    private void setEventListeners() {
        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                showSnackbar("Please enter your email.");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                showSnackbar("Please enter your password.");
                return;
            }

            loginUser(email, password);
        });
        binding.tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });
        binding.btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });
    }

    private void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            retrieveUserDataFromFirestore(email);
                        } else {
                            showSnackbar("Login failed: " + task.getException().getMessage());
                        }
                    }
                });
    }

    private void retrieveUserDataFromFirestore(String email) {
        firebaseFirestore.collection("userDetails")
                .document(email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                UserRegistration user = document.toObject(UserRegistration.class);
                                if (user != null) {
                                    showSnackbar("Login successful.");
                                    storeUserDataInSharedPreferences(user);
                                    startMainActivity();
                                } else {
                                    showSnackbar("User data not found.");
                                }
                            } else {
                                showSnackbar("User data not found.");
                            }
                        } else {
                            showSnackbar("Failed to retrieve user data: " + task.getException().getMessage());
                        }
                    }
                });
    }

    private void storeUserDataInSharedPreferences(UserRegistration user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", user.getName());
        editor.putString("mobileNumber", user.getMobileNumber());
        editor.putInt("salary", user.getSalary());
        editor.putInt("foodExpense", user.getFoodExpense());
        editor.putInt("healthExpense", user.getHealthExpense());
        editor.putInt("livingExpense", user.getLivingExpense());
        editor.putInt("transportExpense", user.getTransportExpense());
        editor.putInt("payables", user.getPayables());
        editor.putInt("miscellaneous", user.getMiscellaneous());
        editor.putString("email", user.getEmail());
        editor.apply();
    }

    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Close LoginActivity
    }

    private void showSnackbar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }
}
