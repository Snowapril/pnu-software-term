package com.software_term.gitpnu.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GithubAction {

    @SerializedName("total_count")
    private int count;

    @SerializedName("workflow_runs")
    private List<WorkflowRun> workflowRuns;

    public List<WorkflowRun> getWorkflowRuns() {
        return workflowRuns;
    }
}
