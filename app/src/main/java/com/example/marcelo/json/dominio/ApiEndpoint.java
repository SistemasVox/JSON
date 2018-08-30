package com.example.marcelo.json.dominio;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface ApiEndpoint {


    @GET("posts/{id}")
    Call<Post> obeterPost(@Path("id") int userID);
    //Call<Post> obeterPost(int userID);
}
