package com.voldy.br2;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.voldy.br2.databinding.ActivityGlideBinding;

public class GlideActivity extends AppCompatActivity {

    private ActivityGlideBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGlideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getImage();
    }

    private void getImage(){
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://blackrock-68d3b.appspot.com");
        StorageReference storageRef = storage.getReference();

        StorageReference pathReference = storageRef.child("linkedin.png");

        pathReference.getDownloadUrl().addOnSuccessListener( new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext()).load(uri).into(binding.ivPicasso);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(GlideActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}