package com.example.exampleproject.RetroFit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.example.exampleproject.RetroFit.URLs.API_FOLDER;
import static com.example.exampleproject.RetroFit.URLs.BASE_URL;

public interface RetroFitInterface {


    @GET(BASE_URL + API_FOLDER +"UserLogin")
    Call<List<LoginResponse>> getUserLogin(@Query("sLoginName") String sLoginName, @Query("sPassword") String sPassword);
}
