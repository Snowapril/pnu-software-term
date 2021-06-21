package com.software_term.gitpnu.model;

import com.google.gson.annotations.SerializedName;

public class WorkflowRun {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("repository")
    private GithubRepo repository;

    @SerializedName("head_branch")
    private String headBranch;

    @SerializedName("event")
    private String event;

    @SerializedName("status")
    private String status;

    @SerializedName("conclusion")
    private String conclusion;

    @SerializedName("head_commit")
    private HeadCommit headCommit;

    @SerializedName("created_at")
    private String createdAt;

    public String getName() {
        return name;
    }

    public String getHeadBranch() {
        return headBranch;
    }

    public String getEvent() {
        return event;
    }

    public String getStatus() {
        return status;
    }

    public String getConclusion() {
        return conclusion;
    }

    public HeadCommit getHeadCommit() {
        return headCommit;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public GithubRepo getRepository() {
        return repository;
    }

    public String getId() {
        return id;
    }
}
