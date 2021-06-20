package com.software_term.gitpnu.model;

import com.google.gson.annotations.SerializedName;

public class GithubLicense {
    @SerializedName("key")
    private String key;

    @SerializedName("name")
    private String name;

    @SerializedName("url")
    private String url;

    public String getName() {
        return name;
    }
}
