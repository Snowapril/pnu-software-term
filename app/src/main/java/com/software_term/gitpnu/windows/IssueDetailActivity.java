package com.software_term.gitpnu.windows;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.software_term.gitpnu.R;
import com.software_term.gitpnu.adapter.IssueAdapter;
import com.software_term.gitpnu.api.GithubAPI;
import com.software_term.gitpnu.api.GithubClient;
import com.software_term.gitpnu.databinding.ActivityIssueBinding;
import com.software_term.gitpnu.model.GithubIssue;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IssueDetailActivity extends AppCompatActivity {

    private String m_token;
    private ActivityIssueBinding m_binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_binding = ActivityIssueBinding.inflate(getLayoutInflater());
        View view = m_binding.getRoot();
        setContentView(view);

        m_token = getIntent().getStringExtra("token");
        String owner = getIntent().getStringExtra("owner");
        String repo = getIntent().getStringExtra("repo");
    }
}