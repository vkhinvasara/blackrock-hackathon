package com.voldy.blackrock.ui.dashboard;

import android.os.Bundle;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.voldy.blackrock.databinding.ActivityChatbotBinding;
import com.voldy.blackrock.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class ChatBotActivity extends AppCompatActivity {

    private ActivityChatbotBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatbotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.wvChatBot.getSettings().setJavaScriptEnabled(true);

        binding.wvChatBot.setWebViewClient(new WebViewClient());

        binding.wvChatBot.loadUrl("https://app.fastbots.ai/embed/clypu92mq004qr9bbv3xhmvxi");
    }

}
