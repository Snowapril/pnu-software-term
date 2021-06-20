package com.software_term.gitpnu.windows;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Spanned;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.software_term.gitpnu.R;
import com.software_term.gitpnu.api.GithubAPI;
import com.software_term.gitpnu.api.GithubClient;
import com.software_term.gitpnu.api.ImageDownloader;
import com.software_term.gitpnu.api.TextDownloader;
import com.software_term.gitpnu.databinding.ActivityRepoBinding;
import com.software_term.gitpnu.model.GithubRepo;
import com.software_term.gitpnu.model.GithubRepoReadme;

import org.commonmark.node.Node;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import io.noties.markwon.Markwon;
import io.noties.markwon.ext.tables.TablePlugin;
import io.noties.markwon.image.ImagesPlugin;
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

                m_binding.issue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RepoActivity.this, IssueActivity.class);
                        intent.putExtra("token", m_token);
                        intent.putExtra("owner", repoBody.getOwner().getLogin());
                        intent.putExtra("repo", repoBody.getName());
                        startActivity(intent);
                    }
                });
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
                    String readmeContents = null;
                    TextDownloader task = new TextDownloader();
                    try {
                        final Context context = getApplicationContext();
                        readmeContents = task.execute(response.body().getDownloadUrl()).get();

                        final Markwon markwon = Markwon.builder(context)
                                .usePlugin(ImagesPlugin.create())
                                .usePlugin(TablePlugin.create(context))
                                .build();
                        // parse markdown to commonmark-java Node
                        final Node node = markwon.parse(readmeContents);
                        final Spanned markdown = markwon.render(node);
                        markwon.setParsedMarkdown(m_binding.repoReadme, markdown);
                        Log.d("loadReadme", "api load done");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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