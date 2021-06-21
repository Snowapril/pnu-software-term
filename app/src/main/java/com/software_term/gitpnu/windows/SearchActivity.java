package com.software_term.gitpnu.windows;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements IssueAdapter.OnNoteListener {

    private String m_token;
    private ActivityIssueBinding m_binding;
    RecyclerView m_recyclerView;
    List<GithubIssue> m_datasource = new ArrayList<>();
    IssueAdapter m_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_binding = ActivityIssueBinding.inflate(getLayoutInflater());
        View view = m_binding.getRoot();
        setContentView(view);

        m_token = getIntent().getStringExtra("token");
        String owner = getIntent().getStringExtra("owner");
        String repo = getIntent().getStringExtra("repo");

        m_recyclerView = (RecyclerView) findViewById(R.id.issues_recycler_view);

        m_recyclerView.setHasFixedSize(true);
        m_adapter = new IssueAdapter(m_datasource, R.layout.issue_list_item, this, repo, this);
        m_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        m_recyclerView.setAdapter(m_adapter);

        loadIssueList(owner, repo);
    }

    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(this, IssueDetailActivity.class);
        startActivity(intent);
    }

    private void loadIssueList(String owner, String repo) {
        GithubClient apiService = GithubAPI.getClient().create(GithubClient.class);
        Call<List<GithubIssue>> repoCall = apiService.getRepoIssues(owner, repo, String.format("token %s", m_token));
        repoCall.enqueue(new Callback<List<GithubIssue>>() {
            @Override
            public void onResponse(Call<List<GithubIssue>> call, Response<List<GithubIssue>> response) {
                m_datasource.clear();
                m_datasource.addAll(response.body());
                m_adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<GithubIssue>> call, Throwable t) {
                // Log error here since request failed
                Log.e("loadIssueList", t.toString());
            }
        });
    }
}