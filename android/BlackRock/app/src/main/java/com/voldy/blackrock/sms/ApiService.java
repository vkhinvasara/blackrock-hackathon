package com.voldy.blackrock.sms;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("getTransactionInfo")
    Call<TransactionInfoResponse> getTransactionInfo(@Body String sms);
}