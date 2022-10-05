package com.liu.retrofit2demo;

import org.junit.Test;

import static org.junit.Assert.*;

import android.util.Log;

import com.liu.retrofit2demo.bean.Repo;
import com.liu.retrofit2demo.bean.Repo2;
import com.liu.retrofit2demo.service.GitHubService;
import com.liu.retrofit2demo.wrapper.RetrofitWrapper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest2 {

    @Test
    public void getUserRepo() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GitHubService gitHubService = retrofit.create(GitHubService.class);
        Call<List<Repo2>> call = gitHubService.listRepos2("octocat");

        call.enqueue(new Callback<List<Repo2>>() {
            @Override
            public void onResponse(Call<List<Repo2>> call, Response<List<Repo2>> response) {
                //接收结果
                List<Repo2> list = response.body();
                System.out.println("----------------------------");
                System.out.println(list.toString());
                System.out.println("----------------------------");
            }

            @Override
            public void onFailure(Call<List<Repo2>> call, Throwable t) {
                t.printStackTrace();
            }
        });
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}