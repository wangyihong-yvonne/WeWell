package edu.neu.madcourse.wewell.model;

public class Activity {
    private long startTime;
    private long pace;
    private double distance; //in km
    private long runningTime;
    private int calories;

    public Activity(long startTime, long pace, double distance, long runningTime, int calories) {
        this.startTime = startTime;
        this.pace = pace;
        this.distance = distance;
        this.runningTime = runningTime;
        this.calories = calories;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getPace() {
        return pace;
    }

    public void setPace(long pace) {
        this.pace = pace;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(long runningTime) {
        this.runningTime = runningTime;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }
}
