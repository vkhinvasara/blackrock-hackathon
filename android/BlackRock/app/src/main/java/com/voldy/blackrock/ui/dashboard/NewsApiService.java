package com.voldy.blackrock.ui.dashboard;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NewsApiService {
    @GET("finance-news")
    Call<List<NewsArticleModel>> getFinanceNews();
}
