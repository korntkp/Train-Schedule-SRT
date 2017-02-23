package io.github.shredktp.trainschedulesrt.data;

/**
 * Created by Korshreddern on 25-Jan-17.
 */

public class TrainSchedule {
    private String startStation;
    private String endStation;
    private String number;
    private String type;
    private String startTime;
    private String endTime;

    public TrainSchedule(String startStation, String endStation, String number, String type, String startTime,
                         String endTime) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.number = number;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "TrainSchedule{" +
                "\nstartStation='" + startStation + '\'' +
                "\nendStation='" + endStation + '\'' +
                ",\nnumber='" + number + '\'' +
                ",\ntype='" + type + '\'' +
                ",\nstartTime='" + startTime + '\'' +
                ",\nendTime='" + endTime + '\'' +
                "\n}";
    }
}
