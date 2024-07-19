package com.voldy.blackrock.sms;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FetchCurrentOrdersApi {
    @POST("getTransactionInfo")
    Call<Data> getTransaction(
            @Body SmsData lead_id
    );
}
