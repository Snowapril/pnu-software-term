package com.software_term.gitpnu.api;
import com.software_term.gitpnu.model.GithubAction;
import com.software_term.gitpnu.model.GithubIssue;
import com.software_term.gitpnu.model.GithubNotify;
import com.software_term.gitpnu.model.GithubRepoReadme;
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
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GithubClient {
    @Headers("Accept: application/json")
    @POST("login/oauth/access_token")
    @FormUrlEncoded
    Call<AccessToken> getAccessToken(
            @Field("client_id") String clientID,
            @Field("client_secret") String clientSecret,
            @Field("code") String code);

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/user")
    Call<GithubUser> getAuthroizedUser(@Header("Authorization") String token);

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/notifications")
    Call<List<GithubNotify>> getNotifications(@Header("Authorization") String token);

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/repos/{owner}/{repo}/actions/runs")
    Call<GithubAction> getActions(@Path("owner") String owner, @Path("repo") String repo, @Header("Authorization") String token);

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/repos/{owner}/{repo}/actions/runs/{run_id}/logs")
    Call<Void> getActionLogURL(@Path("owner") String owner, @Path("repo") String repo, @Path("run_id") String run_id, @Header("Authorization") String token);

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/user/starred/{owner}/{repo}")
    Call<Void> checkUserStarred(@Path("owner") String owner, @Path("repo") String repo, @Header("Authorization") String token);

    @Headers("Accept: application/vnd.github.v3+json")
    @PUT("/user/starred/{owner}/{repo}")
    Call<Void> starRepository(@Path("owner") String owner, @Path("repo") String repo, @Header("Authorization") String token);

    @GET("/users/{user}")
    Call<GithubUser> getUser(@Path("user") String user);

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/user/repos")
    Call<List<GithubRepo>> getRepos(@Header("Authorization") String token);

    @GET("/users/{user}/repos")
    Call<List<GithubRepo>> getReposForUser(@Path("user") String user);

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/repos/{user}/{repo}")
    Call<GithubRepo> getRepoDetail(@Path("user") String user, @Path("repo") String repo, @Header("Authorization") String token);

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/repos/{user}/{repo}/readme")
    Call<GithubRepoReadme> getRepoReadme(@Path("user") String user, @Path("repo") String repo, @Header("Authorization") String token);

    @Headers("Accept: application/vnd.github.v3+json")
    @GET("/repos/{owner}/{repo}/issues?state=all")
    Call<List<GithubIssue>> getRepoIssues(@Path("owner") String owner, @Path("repo") String repo, @Header("Authorization") String token);
}
