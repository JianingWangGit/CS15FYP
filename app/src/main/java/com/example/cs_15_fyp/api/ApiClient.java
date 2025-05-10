package com.example.cs_15_fyp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // if you are using the andriod virtual emulator:
    // "http://10.0.2.2:3000/"; // special alias for localhost (127.0.0.1) on the emulator only
    // else if physical device (Same WIFI network for the laptop and phone):
    // run ipconfig in your laptop to get the IPv4 Address.
    // change to your IP when testing on device
    private static final String BASE_URL = "http://118.138.123.129:2001/";
    //"http://192.168.59.248:2001/" "http://118.138.80.225:2001/"; "http://118.138.73.188:2001/
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // Add teammate's convenience methods
    public static ReviewApi getReviewApi() {
        return getClient().create(ReviewApi.class);
    }

    public static RestaurantService getRestaurantApi() {
        return getClient().create(RestaurantService.class);
    }

}
