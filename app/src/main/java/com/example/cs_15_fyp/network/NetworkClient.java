package com.example.cs_15_fyp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {
    // Use 10.0.2.2 for Android emulator to access host machine
    private static final String BASE_URL = "http://10.0.2.2:2001/";
    private static Retrofit retrofit;

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static Retrofit getRetrofitClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static RestaurantService getRestaurantService() {
        return getRetrofitClient().create(RestaurantService.class);
    }
} 