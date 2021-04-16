package edu.neu.madcourse.wewell.model;

public class ActivitySummary {
    private String totalDistance;
    private String totalRuns;
    private String avgPace;
    private String avgCalories;

    public ActivitySummary(String totalDistance, String totalRuns, String avgPace, String avgCalories) {
        this.totalDistance = totalDistance;
        this.totalRuns = totalRuns;
        this.avgPace = avgPace;
        this.avgCalories = avgCalories;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getTotalRuns() {
        return totalRuns;
    }

    public void setTotalRuns(String totalRuns) {
        this.totalRuns = totalRuns;
    }

    public String getAvgPace() {
        return avgPace;
    }

    public void setAvgPace(String avgPace) {
        this.avgPace = avgPace;
    }

    public String getAvgCalories() {
        return avgCalories;
    }

    public void setAvgCalories(String avgCalories) {
        this.avgCalories = avgCalories;
    }
}
