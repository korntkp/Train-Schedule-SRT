package io.github.shredktp.trainschedulesrt.data;

/**
 * Created by Korshreddern on 25-Jan-17.
 */

public class TrainSchedule {
    private String number;
    private String type;
    private String startTime;
    private String endTime;

    public static final String TRAIN_SCHEDULE_TABLE_NAME = "TrainSchedule";

    public class Column {
        public static final String ID = "_id";
        public static final String NAME = "number";
        public static final String TYPE = "type";
        public static final String START_TIME = "startTime";
        public static final String END_TIME = "endTime";
    }

    public TrainSchedule(String number, String type, String startTime, String endTime) {
        this.number = number;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
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
                "\nnumber='" + number + '\'' +
                ",\ntype='" + type + '\'' +
                ",\nstartTime='" + startTime + '\'' +
                ",\nendTime='" + endTime + '\'' +
                "\n}";
    }
}
