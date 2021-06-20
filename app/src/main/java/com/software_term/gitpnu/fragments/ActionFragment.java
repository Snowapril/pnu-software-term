package com.software_term.gitpnu.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.software_term.gitpnu.model.GithubAction;
import com.software_term.gitpnu.windows.ActionActivity;
import com.software_term.gitpnu.windows.RepoActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActionFragment extends Fragment implements RepoAdapter.OnNoteListener {

    private String m_token;
    RecyclerView m_recyclerView;
    List<GithubAction> m_datasource = new ArrayList<>();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("ActionFragment", "onCreateView");
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_home, container, false);
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
        Intent intent = new Intent(getActivity(), ActionActivity.class);
        intent.putExtra("token", m_token);
        startActivity(intent);
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}