package com.software_term.gitpnu.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GithubIssue {

    @SerializedName("user")
    private RepoOwner user;

    @SerializedName("assignees")
    private List<GithubIssueAssignee> assignees;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("title")
    private String title;

    @SerializedName("state")
    private String state;

    public RepoOwner getUser() {
        return user;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public List<GithubIssueAssignee> getAssignees() {
        return assignees;
    }

    public String getTitle() {
        return title;
    }

    public String getState() {
        return state;
    }
}