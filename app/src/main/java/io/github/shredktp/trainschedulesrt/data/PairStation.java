package io.github.shredktp.trainschedulesrt.data;

/**
 * Created by Korshreddern on 11-Feb-17.
 */

public class PairStation {
    private String startStation;
    private String endStation;
    private int count;
    private int isSeeItFirst;
    private long timestamp;

    public PairStation(String startStation, String endStation, int count, int isSeeItFirst, long timestamp) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.count = count;
        this.isSeeItFirst = isSeeItFirst;
        this.timestamp = timestamp;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public int isSeeItFirst() {
        return isSeeItFirst;
    }

    public void setSeeItFirst(int seeItFirst) {
        isSeeItFirst = seeItFirst;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "PairStation{" +
                "\nstartStation='" + startStation + '\'' +
                ",\nendStation='" + endStation + '\'' +
                ",\ncount='" + count + '\'' +
                ",\nisSeeItFirst=" + isSeeItFirst +
                ",\ntimestamp=" + timestamp +
                "\n}";
    }
}
