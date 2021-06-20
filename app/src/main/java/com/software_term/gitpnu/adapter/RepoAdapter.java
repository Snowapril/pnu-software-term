package com.software_term.gitpnu.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.software_term.gitpnu.R;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.ConstantCallSite;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.software_term.gitpnu.model.GithubRepo;
import com.software_term.gitpnu.windows.RepoActivity;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.RepoViewHolder>  {

    private List<GithubRepo> m_repos;
    private int m_rowLayout;
    private Context m_context;
    private OnNoteListener m_onNoteListener;

    public RepoAdapter(List<GithubRepo> repos, int rowLayout, Context context, OnNoteListener onNoteListener) {
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

    public static class RepoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout reposLayout;
        ImageView avatar;
        TextView repoName;
        TextView repoURL;
        TextView repoDate;
        TextView repoID;
        Bitmap avatarBitmap;
        OnNoteListener onNoteListener;

        public RepoViewHolder(@NonNull View v, OnNoteListener onNoteListener) {
            super(v);
            reposLayout = (LinearLayout) v.findViewById(R.id.repo_item_layout);
            avatar = (ImageView) v.findViewById(R.id.avatar_img);
            repoName = (TextView) v.findViewById(R.id.repo_name);
            repoURL = (TextView) v.findViewById(R.id.repo_url);
            repoDate = (TextView) v.findViewById(R.id.repo_date);
            repoID = (TextView) v.findViewById(R.id.repo_id);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    @Override
    public RepoAdapter.RepoViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(m_rowLayout, parent, false);
        return new RepoViewHolder(view, m_onNoteListener);
    }

    private class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RepoViewHolder holder, final int position) {

        ImageDownloader task = new ImageDownloader();
        try {
            holder.avatarBitmap = task.execute(m_repos.get(position).getOwner().getAvatarUrl()).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.avatar.setImageBitmap(holder.avatarBitmap);
        holder.avatar.getLayoutParams().height=220;
        holder.avatar.getLayoutParams().width=220;

        holder.repoName.setText(m_repos.get(position).getName());
        holder.repoName.setText(m_repos.get(position).getName());
        holder.repoURL.setText(m_repos.get(position).getUrl());
        holder.repoDate.setText(m_repos.get(position).getUpdatedDate());
        holder.repoID.setText(String.format("%d", m_repos.get(position).getId()));
    }

    @Override
    public int getItemCount() { return m_repos.size();}

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}
