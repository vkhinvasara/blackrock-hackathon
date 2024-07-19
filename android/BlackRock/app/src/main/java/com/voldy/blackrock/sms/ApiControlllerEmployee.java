package com.voldy.blackrock.sms;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiControlllerEmployee {

    // give url of your api folder
    static final String url = "https://transaction-parser.vercel.app/";

    private static ApiControlllerEmployee clientObject;

    // Create object of retrofit
    private static Retrofit retrofit;

    ApiControlllerEmployee() {
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized ApiControlllerEmployee getInstance() {
        if (clientObject == null)
            clientObject = new ApiControlllerEmployee();
        return clientObject;
    }

    public FetchCurrentOrdersApi getFetchCurrentOrdersApi() {
        return retrofit.create(FetchCurrentOrdersApi.class);
    }
}
