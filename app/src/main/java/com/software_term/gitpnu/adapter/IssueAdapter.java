package com.software_term.gitpnu.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.software_term.gitpnu.R;
import com.software_term.gitpnu.api.ImageDownloader;
import com.software_term.gitpnu.model.GithubIssue;
import com.software_term.gitpnu.model.GithubIssueAssignee;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.IssueViewHolder>  {

    private List<GithubIssue> m_issues;
    private int m_rowLayout;
    private Context m_context;
    private OnNoteListener m_onNoteListener;
    private String m_reponame;

    public IssueAdapter(List<GithubIssue> repos, int rowLayout, Context context, String reponame, OnNoteListener onNoteListener) {
        this.setIssues(repos);
        this.setRowLayout(rowLayout);
        this.setContext(context);
        this.m_reponame = reponame;
        this.m_onNoteListener = onNoteListener;
    }

    public List<GithubIssue> getIssues() {return m_issues;}

    public void setIssues(List<GithubIssue> repos) {this.m_issues = repos;}

    public int getRowLayout() {return m_rowLayout;}

    public void setRowLayout(int rowLayout) {this.m_rowLayout = rowLayout;}

    public Context getContext() {return m_context;}

    public void setContext(Context context) {this.m_context = context;}

    public static class IssueViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout reposLayout;
        TextView issuer;
        TextView repoName;
        TextView issueUpdateDate;
        TextView issueTitle;
        View issueState;
        LinearLayout assigneeAvatarLayout;
        OnNoteListener onNoteListener;
        List<Bitmap> assigneeAvatars;

        public IssueViewHolder(@NonNull View v, OnNoteListener onNoteListener) {
            super(v);
            reposLayout = (LinearLayout) v.findViewById(R.id.issue_item_layout);
            issueState = (View) v.findViewById(R.id.issue_state);
            issuer = (TextView) v.findViewById(R.id.issuer);
            repoName = (TextView) v.findViewById(R.id.repo_name);
            issueTitle = (TextView) v.findViewById(R.id.issue_title);
            issueUpdateDate = (TextView) v.findViewById(R.id.issue_update_date);
            assigneeAvatarLayout = (LinearLayout)v.findViewById(R.id.issue_assignee_avatars);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    @Override
    public IssueAdapter.IssueViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(m_rowLayout, parent, false);
        return new IssueViewHolder(view, m_onNoteListener);
    }

    @Override
    public void onBindViewHolder(IssueViewHolder holder, final int position) {

        holder.issuer.setText(m_issues.get(position).getUser().getLogin());
        holder.issueUpdateDate.setText(m_issues.get(position).getUpdatedAt());
        holder.repoName.setText(m_reponame);
        holder.issueTitle.setText(m_issues.get(position).getTitle());

        String state = m_issues.get(position).getState();
        if (state.equals("open"))
            holder.issueState.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gh_run_success));
        else
            holder.issueState.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gh_run_fail));

        List<GithubIssueAssignee> assignees = m_issues.get(position).getAssignees();
        for (GithubIssueAssignee assignee : assignees){
            ImageDownloader task = new ImageDownloader();
            Bitmap bitmap = null;
            try {
                bitmap = task.execute(assignee.getAvatarUrl()).get();
                holder.assigneeAvatars.add(bitmap);

                ImageView imageView = new ImageView(getContext());
                imageView.setImageBitmap(bitmap);
                imageView.getLayoutParams().height=220;
                imageView.getLayoutParams().width=220;
                holder.assigneeAvatarLayout.addView(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() { return m_issues.size();}

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}
