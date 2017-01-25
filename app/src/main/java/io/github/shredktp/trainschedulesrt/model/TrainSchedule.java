package io.github.shredktp.trainschedulesrt.model;

/**
 * Created by Korshreddern on 25-Jan-17.
 */

public class TrainSchedule {
    private String number;
    private String type;
    private String leaveTime;
    private String arriveTime;

    public TrainSchedule(String number, String type, String leaveTime, String arriveTime) {
        this.number = number;
        this.type = type;
        this.leaveTime = leaveTime;
        this.arriveTime = arriveTime;
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

    public String getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(String leaveTime) {
        this.leaveTime = leaveTime;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    @Override
    public String toString() {
        return "TrainSchedule{" +
                "\nnumber='" + number + '\'' +
                ",\ntype='" + type + '\'' +
                ",\nleaveTime='" + leaveTime + '\'' +
                ",\narriveTime='" + arriveTime + '\'' +
                "\n}";
    }
}
