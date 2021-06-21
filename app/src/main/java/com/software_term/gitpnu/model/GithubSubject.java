package com.software_term.gitpnu.model;

import com.google.gson.annotations.SerializedName;

public class GithubSubject {
    @SerializedName("title")
    private String title;

    @SerializedName("latest_comment_url")
    private String latestCommentURL;

    @SerializedName("url")
    private String url;

    @SerializedName("type")
    private String type;

    public String getType() {
        return type;
    }

    public String getLatestCommentURL() {
        return latestCommentURL;
    }

    public String getTitle() {
        return title;
    }
}
