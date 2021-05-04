package com.example.PuzzleAndAdventure.model;

public class ListData {
    private int taskId;
    private String taskName;
    private boolean isFinished;
    private boolean isSearched;

    public ListData(int taskId, String taskName, boolean isFinished, boolean isSearched){
        this.taskId = taskId;
        this.taskName = taskName;
        this.isFinished = isFinished;
        this.isSearched = isSearched;
    }

    public String getTaskName(){
        return taskName;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public boolean isSearched() {
        return isSearched;
    }
}
