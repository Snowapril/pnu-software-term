package com.software_term.gitpnu.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.software_term.gitpnu.R;

import java.util.List;

import com.software_term.gitpnu.model.GithubRepo;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.RepoViewHolder>  {

    private List<GithubRepo> repos;
    private int rowLayout;
    private Context context;

    public RepoAdapter(List<GithubRepo> repos, int rowLayout, Context context) {
        this.setRepos(repos);
        this.setRowLayout(rowLayout);
        this.setContext(context);
    }

    public List<GithubRepo> getRepos() {return repos;}

    public void setRepos(List<GithubRepo> repos) {this.repos = repos;}

    public int getRowLayout() {return rowLayout;}

    public void setRowLayout(int rowLayout) {this.rowLayout = rowLayout;}

    public Context getContext() {return context;}

    public void setContext(Context context) {this.context = context;}

    public static class RepoViewHolder extends RecyclerView.ViewHolder {
        LinearLayout reposLayout;
        TextView repoName;
        TextView repoDescription;
        TextView repolanguage;


        public RepoViewHolder(View v) {
            super(v);
            reposLayout = (LinearLayout) v.findViewById(R.id.repo_item_layout);
            repoName = (TextView) v.findViewById(R.id.repoName);
            repoDescription = (TextView) v.findViewById(R.id.repoDescription);
            repolanguage = (TextView) v.findViewById(R.id.repoLanguage);
        }
    }

    @Override
    public RepoAdapter.RepoViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new RepoViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RepoViewHolder holder, final int position) {
        holder.repoName.setText(repos.get(position).getName());
        holder.repoDescription.setText(repos.get(position).getDescription());
        holder.repolanguage.setText(repos.get(position).getLanguage());
    }

    @Override
    public int getItemCount() { return repos.size();}
}
