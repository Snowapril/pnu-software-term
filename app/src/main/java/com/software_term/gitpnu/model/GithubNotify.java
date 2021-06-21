package com.software_term.gitpnu.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GithubNotify {

    @SerializedName("id")
    private String id;

    @SerializedName("repository")
    private GithubRepo repository;

    @SerializedName("subject")
    private GithubSubject subject;

    @SerializedName("reason")
    private String reason;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("last_read_at")
    private String lastReadAt;

    public GithubRepo getRepository() {
        return repository;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getLastReadAt() {
        return lastReadAt;
    }

    public String getReason() {
        return reason;
    }

    public GithubSubject getSubject() {
        return subject;
    }

    public String getId() {
        return id;
    }
}