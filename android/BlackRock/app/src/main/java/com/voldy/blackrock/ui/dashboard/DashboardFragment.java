package com.voldy.blackrock.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.voldy.blackrock.databinding.FragmentDashboardBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    private NewsAdapter newsAdapter;
    private List<NewsArticleModel> articles;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);

        binding.chatbot.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ChatBotActivity.class));
        });

        setUpRecyclerView();
        fetchNewsArticles();

        return binding.getRoot();
    }


    private void setUpRecyclerView() {
        articles = new ArrayList<>();
        binding.rvData.setLayoutManager(new LinearLayoutManager(getContext()));
        newsAdapter = new NewsAdapter(getContext(), articles);
        binding.rvData.setAdapter(newsAdapter);
    }

    private void fetchNewsArticles() {
        NewsApiService apiService = RetrofitClient.getClient().create(NewsApiService.class);

        Call<List<NewsArticleModel>> call = apiService.getFinanceNews();
        call.enqueue(new Callback<List<NewsArticleModel>>() {
            @Override
            public void onResponse(Call<List<NewsArticleModel>> call, Response<List<NewsArticleModel>> response) {
                if (response.isSuccessful()) {
                    List<NewsArticleModel> fetchedArticles = response.body();
                    if (fetchedArticles != null) {
                        articles.clear();
                        articles.addAll(fetchedArticles);
                        newsAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e("MainActivity", "Request failed with status: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<NewsArticleModel>> call, Throwable t) {
                Log.e("MainActivity", "Request failed", t);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}