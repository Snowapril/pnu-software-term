package com.software_term.gitpnu.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class GithubRepo {

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("id")
    private int id;

    @SerializedName("html_url")
    private String url;

    @SerializedName("updated_at")
    private String updatedDate;

    @SerializedName("language")
    private String language;

    @SerializedName("stargazers_count")
    private int starCount;

    @SerializedName("watchers_count")
    private int watcherCount;

    @SerializedName("forks_count")
    private int forkCount;

    @SerializedName("open_issues_count")
    private int openIssueCount;

    @SerializedName("open_issues")
    private int openIssues;

    @SerializedName("owner")
    private RepoOwner owner;

    @SerializedName("license")
    private GithubLicense license;

    public GithubRepo(
            String language,
            String description,
            String name) {
        this.setName(name);
        this.setDescription(description);
        this.setLanguage(language);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getForkCount() {
        return forkCount;
    }

    public void setForkCount(int forkCount) {
        this.forkCount = forkCount;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RepoOwner getOwner() {
        return owner;
    }

    public int getStarCount() {
        return starCount;
    }

    public int getWatcherCount() {
        return watcherCount;
    }

    public int getOpenIssueCount() {
        return openIssueCount;
    }

    public int getOpenIssues() {
        return openIssues;
    }

    public String getLicenseName() {
        return license == null ? null : license.getName();
    }
}