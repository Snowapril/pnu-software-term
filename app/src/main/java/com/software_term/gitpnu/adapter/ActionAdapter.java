package com.software_term.gitpnu.adapter;


import android.content.Context;
import android.graphics.Bitmap;
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
import com.software_term.gitpnu.model.GithubIssueAssignee;
import com.software_term.gitpnu.model.GithubRepo;
import com.software_term.gitpnu.model.WorkflowRun;

import java.util.List;

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ActionViewHolder>  {

    private List<WorkflowRun> m_workflowRuns;
    private int m_rowLayout;
    private Context m_context;
    private OnNoteListener m_onNoteListener;

    public ActionAdapter(List<WorkflowRun> runs, int rowLayout, Context context, OnNoteListener onNoteListener) {
        this.setWorkflowRuns(runs);
        this.setRowLayout(rowLayout);
        this.setContext(context);
        this.m_onNoteListener = onNoteListener;
    }

    public List<WorkflowRun> getWorkflowRuns() {return m_workflowRuns;}

    public void setWorkflowRuns(List<WorkflowRun> runs) {this.m_workflowRuns = runs;}

    public int getRowLayout() {return m_rowLayout;}

    public void setRowLayout(int rowLayout) {this.m_rowLayout = rowLayout;}

    public Context getContext() {return m_context;}

    public void setContext(Context context) {this.m_context = context;}

    public static class ActionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout actionsLayout;
        View runState;
        TextView headCommitTitle;
        TextView runName;
        TextView headCommitID;
        TextView runCreatedAt;
        TextView runHeadBranch;
        OnNoteListener onNoteListener;

        public ActionViewHolder(@NonNull View v, OnNoteListener onNoteListener) {
            super(v);
            actionsLayout = (LinearLayout) v.findViewById(R.id.action_item_layout);
            runState = (View)v.findViewById(R.id.run_state);
            headCommitTitle = (TextView)v.findViewById(R.id.head_commit_title);
            runName = (TextView)v.findViewById(R.id.run_name);
            headCommitID = (TextView)v.findViewById(R.id.commit_id);
            runCreatedAt = (TextView)v.findViewById(R.id.run_created_at);
            runHeadBranch = (TextView)v.findViewById(R.id.run_head_branch);
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

        holder.headCommitTitle.setText(m_workflowRuns.get(position).getHeadCommit().getMessage());
        holder.runName.setText(m_workflowRuns.get(position).getName());
        holder.headCommitID.setText(m_workflowRuns.get(position).getHeadCommit().getId().substring(0, 7));
        holder.runCreatedAt.setText(m_workflowRuns.get(position).getCreatedAt());
        holder.runHeadBranch.setText(m_workflowRuns.get(position).getHeadBranch());

        String state = m_workflowRuns.get(position).getConclusion();
        if (state.equals("success"))
            holder.runState.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gh_run_success));
        else
            holder.runState.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.gh_run_fail));
    }

    @Override
    public int getItemCount() { return m_workflowRuns.size();}

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}
