package com.voldy.blackrock.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.voldy.blackrock.R;
import com.voldy.blackrock.databinding.ActivityFullNewsBinding;
import com.voldy.blackrock.databinding.ActivityMainBinding;
import com.voldy.blackrock.sms.SmsModel;

import java.util.ArrayList;

public class FullNewsActivity extends AppCompatActivity {

    private ActivityFullNewsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFullNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NewsArticleModel newsArticleModel = (NewsArticleModel) getIntent().getSerializableExtra("article");


        Glide.with(getApplicationContext())
                .load(newsArticleModel.getUrlToImage())
                .placeholder(R.drawable.loading) // Placeholder image
                .error(R.drawable.error) // Error image if loading fails
                .into(binding.imageNews);

        binding.textAuthor.setText(newsArticleModel.getAuthor());
        binding.textTitle.setText(newsArticleModel.getTitle());
        binding.textDescription.setText(newsArticleModel.getDescription());

    }
}
