package edu.neu.madcourse.wewell.model;

public class Leader {
    private String id;
    private String name;
    private int ranking;
    private double distance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getFormatName() {
        try {
            if (name == null) return "";
            String formatName = name.length() > 5 ? name.substring(0, name.length() - 3) : name;
            formatName += "***";
            return formatName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
