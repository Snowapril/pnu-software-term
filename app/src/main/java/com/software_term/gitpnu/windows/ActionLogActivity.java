package com.software_term.gitpnu.windows;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Spanned;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.software_term.gitpnu.api.GithubAPI;
import com.software_term.gitpnu.api.GithubClient;
import com.software_term.gitpnu.api.ImageDownloader;
import com.software_term.gitpnu.api.TextDownloader;
import com.software_term.gitpnu.databinding.ActivityActionLogBinding;
import com.software_term.gitpnu.databinding.ActivityRepoBinding;
import com.software_term.gitpnu.model.GithubRepo;
import com.software_term.gitpnu.model.GithubRepoReadme;

import org.commonmark.node.Node;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.noties.markwon.Markwon;
import io.noties.markwon.ext.tables.TablePlugin;
import io.noties.markwon.image.ImageItem;
import io.noties.markwon.image.ImagesPlugin;
import io.noties.markwon.image.SchemeHandler;
import io.noties.markwon.image.data.DataUriSchemeHandler;
import io.noties.markwon.image.file.FileSchemeHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActionLogActivity extends AppCompatActivity {

    private String m_token;
    private ActivityActionLogBinding m_binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_binding = ActivityActionLogBinding.inflate(getLayoutInflater());
        View view = m_binding.getRoot();
        setContentView(view);

        m_token = getIntent().getStringExtra("token");
        String commit_title = getIntent().getStringExtra("commit_title");
        String login = getIntent().getStringExtra("login");
        String repo = getIntent().getStringExtra("repo");
        String run_id = getIntent().getStringExtra("run_id");

        m_binding.runCommitMessage.setText(commit_title);
        loadActionLogURL(login, repo, run_id);
    }

    private void loadActionLogURL(String login, String repo, String run_id) {
        GithubClient apiService = GithubAPI.getClient().create(GithubClient.class);

        Call<Void> call = apiService.getActionLogURL(login, repo, run_id, String.format("token %s", m_token));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                loadActionLog(response.raw().request().url().toString());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Log error here since request failed
                Log.e("Repos", t.toString());
            }
        });
    }

    private void loadActionLog(String logUrl){
        String actionLogContents = null;
        TextDownloader task = new TextDownloader();
        try {
            actionLogContents = task.execute(logUrl).get();
            m_binding.actionLog.setText(actionLogContents);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}