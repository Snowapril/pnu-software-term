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
import androidx.recyclerview.widget.RecyclerView;

import com.software_term.gitpnu.R;
import com.software_term.gitpnu.api.ImageDownloader;
import com.software_term.gitpnu.model.GithubIssueAssignee;
import com.software_term.gitpnu.model.GithubNotify;

import java.util.List;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.NotifyHolder>  {

    private List<GithubNotify> m_notifications;
    private int m_rowLayout;
    private Context m_context;
    private OnNoteListener m_onNoteListener;

    public NotifyAdapter(List<GithubNotify> notifications, int rowLayout, Context context, OnNoteListener onNoteListener) {
        this.setNotifications(notifications);
        this.setRowLayout(rowLayout);
        this.setContext(context);
        this.m_onNoteListener = onNoteListener;
    }

    public int getRowLayout() {return m_rowLayout;}

    public void setRowLayout(int rowLayout) {this.m_rowLayout = rowLayout;}

    public Context getContext() {return m_context;}

    public void setContext(Context context) {this.m_context = context;}

    public void setNotifications(List<GithubNotify> m_notifications) {
        this.m_notifications = m_notifications;
    }

    public List<GithubNotify> getNotifications() {
        return this.m_notifications;
    }

    public static class NotifyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout reposLayout;
        TextView ownerLogin;
        TextView repoName;
        TextView notifyID;
        TextView notifyUpdateDate;
        ImageView avatar;
        OnNoteListener onNoteListener;
        Bitmap avatarBitmap;

        public NotifyHolder(@NonNull View v, OnNoteListener onNoteListener) {
            super(v);
            reposLayout = (LinearLayout) v.findViewById(R.id.issue_item_layout);
            ownerLogin = (TextView) v.findViewById(R.id.owner_login);
            repoName = (TextView) v.findViewById(R.id.repo_name);
            notifyID = (TextView) v.findViewById(R.id.notify_id);
            notifyUpdateDate = (TextView)v.findViewById(R.id.notify_date);
            avatar = (ImageView)v.findViewById(R.id.avatar_img);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    @Override
    public NotifyAdapter.NotifyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(m_rowLayout, parent, false);
        return new NotifyHolder(view, m_onNoteListener);
    }

    @Override
    public void onBindViewHolder(NotifyHolder holder, final int position) {
        holder.ownerLogin.setText(m_notifications.get(position).getRepository().getOwner().getLogin());
        holder.repoName.setText(m_notifications.get(position).getRepository().getName());
        holder.notifyID.setText(m_notifications.get(position).getId());
        holder.notifyUpdateDate.setText(m_notifications.get(position).getUpdatedAt());

        ImageDownloader task = new ImageDownloader();
        try {
            holder.avatarBitmap = task.execute(m_notifications.get(position).getRepository().getOwner().getAvatarUrl()).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.avatar.setImageBitmap(holder.avatarBitmap);
        holder.avatar.getLayoutParams().height=220;
        holder.avatar.getLayoutParams().width=220;
    }

    @Override
    public int getItemCount() { return m_notifications.size();}

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}
