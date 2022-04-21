package com.example.exampleproject.RetroFit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.exampleproject.Tools;
import com.example.exampleproject.databinding.ActivityRetrofitBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitActivity extends AppCompatActivity {
    ActivityRetrofitBinding binding;
    List<LoginResponse>list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityRetrofitBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        list=new ArrayList<>();


        if (Tools.isConnected(this)) {
            getDataFromAPI();
        } else {
            Toast.makeText(this, "Unable to connect network, please try again", Toast.LENGTH_LONG).show();
        }
    }

    private void getDataFromAPI() {

        RetroFitInterface client = RetroFitClient.getClient().create(RetroFitInterface.class);
        Call<List<LoginResponse>> call = client.getUserLogin("sa","sa");
        call.enqueue(new Callback<List<LoginResponse>>() {
            @Override
            public void onResponse(Call<List<LoginResponse>> call, Response<List<LoginResponse>> response) {

                list=response.body();

                Log.d("TAG","Response = "+list.size());

                for (int i=0;i<list.size();i++){
                    Log.d("ResponseRes",list.get(i).UserExists);
                }
            }

            @Override
            public void onFailure(Call<List<LoginResponse>> call, Throwable t) {
                Log.d("ResponseResE",t.toString());

            }
        });



    }
}