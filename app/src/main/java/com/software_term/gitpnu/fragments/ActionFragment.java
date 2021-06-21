package com.software_term.gitpnu.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.software_term.gitpnu.R;
import com.software_term.gitpnu.adapter.ActionAdapter;
import com.software_term.gitpnu.adapter.RepoAdapter;
import com.software_term.gitpnu.api.GithubAPI;
import com.software_term.gitpnu.api.GithubClient;
import com.software_term.gitpnu.model.GithubAction;
import com.software_term.gitpnu.model.GithubRepo;
import com.software_term.gitpnu.model.WorkflowRun;
import com.software_term.gitpnu.windows.ActionActivity;
import com.software_term.gitpnu.windows.ActionLogActivity;
import com.software_term.gitpnu.windows.RepoActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActionFragment extends Fragment implements ActionAdapter.OnNoteListener {

    private String m_token;
    RecyclerView m_recyclerView;
    List<WorkflowRun> m_datasource = new ArrayList<>();
    ActionAdapter m_adapter;

    public ActionFragment() {
        // Required empty public constructor
    }

    public static ActionFragment newInstance(String token) {
        ActionFragment fragment = new ActionFragment();
        Bundle args = new Bundle();
        args.putString("token", token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        m_token = getArguments().getString("token");
        loadWorkflowRuns();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("ActionFragment", "onCreateView");
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_action, container, false);

        m_recyclerView = (RecyclerView) rootView.findViewById(R.id.actions_recycler_view);
        m_recyclerView.setHasFixedSize(true);
        m_adapter = new ActionAdapter(m_datasource, R.layout.action_list_item, getActivity(), this);
        m_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        m_recyclerView.setAdapter(m_adapter);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bottom_nav, menu);
    }

    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(getActivity(), ActionLogActivity.class);
        intent.putExtra("token", m_token);
        intent.putExtra("commit_title", m_datasource.get(position).getHeadCommit().getMessage());
        intent.putExtra("login", m_datasource.get(position).getRepository().getOwner().getLogin());
        intent.putExtra("repo", m_datasource.get(position).getRepository().getName());
        intent.putExtra("run_id", m_datasource.get(position).getId());
        startActivity(intent);
    }

    public void loadWorkflowRuns(){
        GithubClient apiService = GithubAPI.getClient().create(GithubClient.class);
        Call<GithubAction> call = apiService.getActions("snowapril", "gl_shaded_gltfscene", String.format("token %s", m_token));
        call.enqueue(new Callback<GithubAction>() {
            @Override
            public void onResponse(Call<GithubAction> call, Response<GithubAction> response) {
                m_datasource.clear();
                m_datasource.addAll(response.body().getWorkflowRuns());
                m_adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<GithubAction> call, Throwable t) {
                // Log error here since request failed
                Log.e("Repos", t.toString());
            }
        });
    }
}