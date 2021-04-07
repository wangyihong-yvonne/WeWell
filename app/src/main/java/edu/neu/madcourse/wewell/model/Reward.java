package edu.neu.madcourse.wewell.model;

public class Reward {
    private String title;
    private int type;
    private long progress;
    private long goal;
    private long finishedDistance;
    private boolean isDone;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public long getGoal() {
        return goal;
    }

    public void setGoal(long goal) {
        this.goal = goal;
    }

    public long getFinishedDistance() {
        return finishedDistance;
    }

    public void setFinishedDistance(long finishedDistance) {
        this.finishedDistance = finishedDistance;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
