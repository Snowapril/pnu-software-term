package com.software_term.gitpnu.model;

import com.google.gson.annotations.SerializedName;

public class GithubRepoReadme {

    @SerializedName("name")
    private String name;

    @SerializedName("sha")
    private String sha;

    @SerializedName("url")
    private String url;

    @SerializedName("download_url")
    private String downloadUrl;

    public String getDownloadUrl() {
        return downloadUrl;
    }
}