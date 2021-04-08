package com.example.game03.model;

public class ListData {
    private int taskId;
    private String taskName;
    private boolean isFinished;

    public ListData(int taskId, String taskName, boolean isFinished){
        this.taskId = taskId;
        this.taskName = taskName;
        this.isFinished = isFinished;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getTaskName(){
        return taskName;
    }

    public boolean isFinished() {
        return isFinished;
    }
}
