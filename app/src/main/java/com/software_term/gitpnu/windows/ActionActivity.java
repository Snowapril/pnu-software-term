package com.software_term.gitpnu.windows;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.software_term.gitpnu.R;
import com.software_term.gitpnu.api.GithubAPI;
import com.software_term.gitpnu.api.GithubClient;
import com.software_term.gitpnu.model.GithubRepo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActionActivity extends AppCompatActivity {

    private String m_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo);

        m_token = getIntent().getStringExtra("token");
    }
}