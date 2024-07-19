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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.voldy.blackrock.MainActivity;
import com.voldy.blackrock.common.UserRegistrationValidator;
import com.voldy.blackrock.databinding.ActivityRegisterBinding;
import com.voldy.blackrock.model.UserRegistration;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "user_details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
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
        binding.btnSignUp.setOnClickListener(v -> {
            UserRegistration user = getUserData();
            if (UserRegistrationValidator.isValid(user)) {
                registerUser(user);
            } else {
                showSnackbar("User registration is invalid. Please check your inputs.");
            }
        });

        binding.tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

        binding.btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

    }

    private UserRegistration getUserData() {
        String name = binding.etName.getText().toString().trim();
        String mobileNumber = binding.etMobileNumber.getText().toString().trim();
        int salary = getIntValue(binding.etSalary);
        int foodExpense = 0;
        int healthExpense = 0;
        int livingExpense = 0;
        int transportExpense = 0;
        int payables = 0;
        int miscellaneous = 0;
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        return new UserRegistration(name, mobileNumber, salary, foodExpense, healthExpense, livingExpense,
                transportExpense, payables, miscellaneous, email, password);
    }

    private int getIntValue(android.widget.EditText editText) {
        String value = editText.getText().toString().trim();
        return TextUtils.isEmpty(value) ? 0 : Integer.parseInt(value);
    }

    private void registerUser(UserRegistration user) {
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            storeUserDataInSharedPreferences(user);
                            storeUserDataInFirestore(user);
                        } else {
                            showSnackbar("Registration failed: " + task.getException().getMessage());
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

    private void storeUserDataInFirestore(UserRegistration user) {
        firebaseFirestore.collection("userDetails")
                .document(user.getEmail())
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            showSnackbar("Registration successful.");
                            startMainActivity();
                        } else {
                            showSnackbar("Failed to store user data: " + task.getException().getMessage());
                        }
                    }
                });
    }

    private void startMainActivity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showSnackbar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }
}
