package com.example.exampleproject.RetroFit;

import android.app.Application;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFitClient {

    private static Retrofit retrofit = null;

    /**
     * returns an instance of Retrofit client.
     *
     * @return
     */
    public static Retrofit getClient() {
//        OkHttpClient httpClient = provideOkhttpClient(provideHttpCache(App.getInstance()));

        Retrofit builder =
                new Retrofit.Builder()
                        .baseUrl(URLs.API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        ).build();

//        if (retrofit == null)
//            retrofit =
//                    builder
//                            .client(
//                                    httpClient
//                            )
//                            .build();
        return builder;

    }

    static Cache provideHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(application.getCacheDir(), cacheSize);
        return cache;
    }

    /**
     * Creates and returns OkHttpClient instance with cache enabled.
     * <p/>HttpLoggingInterceptor is used to intercept and log network traffic
     *
     * @param cache Cache
     * @return OkHttpClient
     */
//    static OkHttpClient provideOkhttpClient(Cache cache) {
//        OkHttpClient.Builder client = new OkHttpClient.Builder();
//        client.cache(cache);
//        client.connectTimeout(120, TimeUnit.SECONDS)
//                .readTimeout(120, TimeUnit.SECONDS)
//                .writeTimeout(120, TimeUnit.SECONDS);
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        client.addInterceptor(loggingInterceptor);
//        return client.build();
//    }
}
