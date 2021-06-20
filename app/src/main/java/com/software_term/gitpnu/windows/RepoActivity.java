package com.software_term.gitpnu.windows;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.software_term.gitpnu.R;
import com.software_term.gitpnu.api.GithubAPI;
import com.software_term.gitpnu.api.GithubClient;
import com.software_term.gitpnu.databinding.ActivityRepoBinding;
import com.software_term.gitpnu.model.GithubRepo;
import com.software_term.gitpnu.model.GithubRepoReadme;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RepoActivity extends AppCompatActivity {

    private String m_token;
    private ActivityRepoBinding m_binding;
    private Bitmap m_avatar_bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_binding = ActivityRepoBinding.inflate(getLayoutInflater());
        View view = m_binding.getRoot();
        setContentView(view);

        m_token = getIntent().getStringExtra("token");
        String login = getIntent().getStringExtra("login");
        String repo = getIntent().getStringExtra("repo");

        loadRepo(login, repo);
    }

    private class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {

            try {

                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                return myBitmap;

            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }
            return null;
        }
    }

    private void loadRepo(String username, String repo) {
        GithubClient apiService = GithubAPI.getClient().create(GithubClient.class);
        Call<GithubRepo> repoCall = apiService.getRepoDetail(username, repo, String.format("token %s", m_token));
        repoCall.enqueue(new Callback<GithubRepo>() {
            @Override
            public void onResponse(Call<GithubRepo> call, Response<GithubRepo> response) {
                final GithubRepo repoBody = response.body();

                ImageDownloader task = new ImageDownloader();
                try {
                    m_avatar_bitmap = task.execute(response.body().getOwner().getAvatarUrl()).get();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                m_binding.avatarImg.setImageBitmap(m_avatar_bitmap);
                m_binding.avatarImg.getLayoutParams().height=220;
                m_binding.avatarImg.getLayoutParams().width=220;

                m_binding.ownerLogin.setText(repoBody.getOwner().getLogin());
                m_binding.name.setText(repoBody.getName());
                m_binding.shortDescription.setText(repoBody.getDescription());
                m_binding.forksCount.setText(String.format("%d", repoBody.getForkCount()));
                m_binding.starCount.setText(String.format("%d", repoBody.getStarCount()));
                m_binding.watchersCount.setText(String.format("%d", repoBody.getWatcherCount()));
                m_binding.issueCount.setText(String.format("%d", repoBody.getOpenIssueCount()));
                m_binding.prCount.setText(String.format("%d", repoBody.getOpenIssues()));
                if (repoBody.getLicenseName() != null) {
                    m_binding.licenseType.setText(repoBody.getLicenseName());
                }
                loadReadme(username, repo);
            }

            @Override
            public void onFailure(Call<GithubRepo> call, Throwable t) {
                // Log error here since request failed
                Log.e("Repos", t.toString());
            }
        });
    }

    private void loadReadme(String username, String repo){
        GithubClient apiService = GithubAPI.getClient().create(GithubClient.class);
        Call<GithubRepoReadme> repoCall = apiService.getRepoReadme(username, repo, String.format("token %s", m_token));
        repoCall.enqueue(new Callback<GithubRepoReadme>() {
            @Override
            public void onResponse(Call<GithubRepoReadme> call, Response<GithubRepoReadme> response) {
                if (response.code() == 200)
                {
                    m_binding.repoReadme.setText(response.body().getDownloadUrl());
                    Log.d("loadReadme", "api load done");
                }
            }

            @Override
            public void onFailure(Call<GithubRepoReadme> call, Throwable t) {
                // Log error here since request failed
                Log.e("Repos", t.toString());
            }
        });
    }
}