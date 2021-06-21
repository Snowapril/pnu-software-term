package com.software_term.gitpnu.model;

import com.google.gson.annotations.SerializedName;

public class HeadCommit {
    @SerializedName("id")
    private String id;

    @SerializedName("message")
    private String message;

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
