package com.software_term.gitpnu.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.software_term.gitpnu.R;
import com.software_term.gitpnu.model.GithubRepo;

import java.util.List;

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ActionViewHolder>  {

    private List<GithubRepo> m_repos;
    private int m_rowLayout;
    private Context m_context;
    private OnNoteListener m_onNoteListener;

    public ActionAdapter(List<GithubRepo> repos, int rowLayout, Context context, OnNoteListener onNoteListener) {
        this.setRepos(repos);
        this.setRowLayout(rowLayout);
        this.setContext(context);
        this.m_onNoteListener = onNoteListener;
    }

    public List<GithubRepo> getRepos() {return m_repos;}

    public void setRepos(List<GithubRepo> repos) {this.m_repos = repos;}

    public int getRowLayout() {return m_rowLayout;}

    public void setRowLayout(int rowLayout) {this.m_rowLayout = rowLayout;}

    public Context getContext() {return m_context;}

    public void setContext(Context context) {this.m_context = context;}

    public static class ActionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout reposLayout;
        OnNoteListener onNoteListener;

        public ActionViewHolder(@NonNull View v, OnNoteListener onNoteListener) {
            super(v);
            reposLayout = (LinearLayout) v.findViewById(R.id.action_item_layout);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    @Override
    public ActionAdapter.ActionViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(m_rowLayout, parent, false);
        return new ActionViewHolder(view, m_onNoteListener);
    }

    @Override
    public void onBindViewHolder(ActionViewHolder holder, final int position) {
    }

    @Override
    public int getItemCount() { return m_repos.size();}

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}
