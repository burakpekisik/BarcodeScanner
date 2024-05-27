package com.example.barcodescanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.journeyapps.barcodescanner.CaptureActivity;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CaptureAct extends CaptureActivity {
    private OkHttpClient client = new OkHttpClient.Builder().build();

    private Gson gson = new GsonBuilder().setLenient().create();

    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://" + BuildConfig.IP1 + ":5000/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build();

    public <T> T buildService(Class<T> service) {
        return retrofit.create(service);
    }
}