package com.software_term.gitpnu.model;

import com.google.gson.annotations.SerializedName;

public class RepoOwner {
    @SerializedName("login")
    private String login;

    @SerializedName("avatar_url")
    private String avatarUrl;

    public String getLogin() {
        return login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
