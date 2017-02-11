package io.github.shredktp.trainschedulesrt.data;

/**
 * Created by Korshreddern on 11-Feb-17.
 */

public class PairStation {
    private String startStation;
    private String endStation;
    private boolean isSeeItFirst;

    public PairStation(String startStation, String endStation, boolean isSeeItFirst) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.isSeeItFirst = isSeeItFirst;
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

    public boolean isSeeItFirst() {
        return isSeeItFirst;
    }

    public void setSeeItFirst(boolean seeItFirst) {
        isSeeItFirst = seeItFirst;
    }

    @Override
    public String toString() {
        return "PairStation{" +
                "\nstartStation='" + startStation + '\'' +
                ",\nendStation='" + endStation + '\'' +
                ",\nisSeeItFirst=" + isSeeItFirst +
                "\n}";
    }
}
