package com.software_term.gitpnu.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.software_term.gitpnu.R;
import com.software_term.gitpnu.adapter.RepoAdapter;
import com.software_term.gitpnu.api.GithubAPI;
import com.software_term.gitpnu.api.GithubClient;
import com.software_term.gitpnu.model.GithubRepo;
import com.software_term.gitpnu.windows.RepoActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements RepoAdapter.OnNoteListener {

    private String m_token;
    RecyclerView m_recyclerView;
    List<GithubRepo> m_datasource = new ArrayList<>();
    RepoAdapter m_adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String token) {
        HomeFragment fragment = new HomeFragment();
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
        loadRepositories();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("HomeFragment", "onCreateView");
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_home, container, false);
        m_recyclerView = (RecyclerView) rootView.findViewById(R.id.repos_recycler_view);

        m_recyclerView.setHasFixedSize(true);
        m_adapter = new RepoAdapter(m_datasource, R.layout.repo_list_item, getActivity(), this);
        m_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        m_recyclerView.setAdapter(m_adapter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        m_recyclerView = (RecyclerView)getView().findViewById(R.id.repos_recycler_view);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bottom_nav, menu);
    }

    public void loadRepositories(){
        GithubClient apiService =
                GithubAPI.getClient().create(GithubClient.class);

        Call<List<GithubRepo>> call = apiService.getRepos(String.format("token %s", m_token));
        call.enqueue(new Callback<List<GithubRepo>>() {
            @Override
            public void onResponse(Call<List<GithubRepo>> call, Response<List<GithubRepo>> response) {
                m_datasource.clear();
                m_datasource.addAll(response.body());
                m_adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<GithubRepo>> call, Throwable t) {
                // Log error here since request failed
                Log.e("Repos", t.toString());
            }
        });
    }

    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(getActivity(), RepoActivity.class);
        intent.putExtra("token", m_token);
        intent.putExtra("repo", m_datasource.get(position).getName());
        intent.putExtra("login", m_datasource.get(position).getOwner().getLogin());
        startActivity(intent);
    }
}