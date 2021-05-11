package com.software_term.gitpnu.api;
import com.software_term.gitpnu.model.GithubUser;
import com.software_term.gitpnu.model.GithubRepo;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GithubClient {
    @Headers("Accept: application/json")
    @POST("login/oauth/access_token")
    @FormUrlEncoded
    Call<AccessToken> getAccessToken(
            @Field("client_id") String clientID,
            @Field("client_secret") String clientSecret,
            @Field("code") String code);

    @GET("/users/{user}")
    Call<GithubUser> getUser(@Path("user") String user);

    @GET("/user/repos")
    Call<List<GithubRepo>> getRepos(@Header("Authorization") String token);

    @GET("/users/{user}/repos")
    Call<List<GithubRepo>> getReposForUser(@Path("user") String user);
}
