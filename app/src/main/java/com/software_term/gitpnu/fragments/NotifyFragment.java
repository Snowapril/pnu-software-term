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
import com.software_term.gitpnu.adapter.NotifyAdapter;
import com.software_term.gitpnu.api.GithubAPI;
import com.software_term.gitpnu.api.GithubClient;
import com.software_term.gitpnu.model.GithubNotify;
import com.software_term.gitpnu.model.GithubRepo;
import com.software_term.gitpnu.windows.IssueActivity;
import com.software_term.gitpnu.windows.IssueDetailActivity;
import com.software_term.gitpnu.windows.RepoActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotifyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotifyFragment extends Fragment implements NotifyAdapter.OnNoteListener {

    private String m_token;
    RecyclerView m_recyclerView;
    List<GithubNotify> m_datasource = new ArrayList<>();
    NotifyAdapter m_adapter;

    public NotifyFragment() {
        // Required empty public constructor
    }

    public static NotifyFragment newInstance(String token) {
        NotifyFragment fragment = new NotifyFragment();
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
        loadNotifications();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("NotifyFragment", "onCreateView");
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_notify, container, false);
        m_recyclerView = (RecyclerView) rootView.findViewById(R.id.notify_recycler_view);

        m_recyclerView.setHasFixedSize(true);
        m_adapter = new NotifyAdapter(m_datasource, R.layout.notify_list_item, getActivity(), this);
        m_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        m_recyclerView.setAdapter(m_adapter);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        m_recyclerView = (RecyclerView)getView().findViewById(R.id.notify_recycler_view);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bottom_nav, menu);
    }

    public void loadNotifications(){
        GithubClient apiService = GithubAPI.getClient().create(GithubClient.class);

        Call<List<GithubNotify>> call = apiService.getNotifications(String.format("token %s", m_token));
        call.enqueue(new Callback<List<GithubNotify>>() {
            @Override
            public void onResponse(Call<List<GithubNotify>> call, Response<List<GithubNotify>> response) {
                m_datasource.clear();
                m_datasource.addAll(response.body());
                m_adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<GithubNotify>> call, Throwable t) {
                // Log error here since request failed
                Log.e("Notifications", t.toString());
            }
        });
    }

    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(getActivity(), IssueDetailActivity.class);
        intent.putExtra("token", m_token);
        startActivity(intent);
    }
}