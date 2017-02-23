package io.github.shredktp.trainschedulesrt.data.source;

import java.util.ArrayList;

import io.github.shredktp.trainschedulesrt.data.Station;
import io.github.shredktp.trainschedulesrt.data.source.station.StationDataSource;

/**
 * Created by Korshreddern on 09-Feb-17.
 */

public class StationRepository implements StationDataSource {
    @Override
    public int countStation() {
        return 0;
    }

    @Override
    public long addStation(String name) {
        return 0;
    }

    @Override
    public long addStation(String name, String line) {
        return 0;
    }

    @Override
    public long addStation(ArrayList<Station> stationArrayList) {
        return 0;
    }

    @Override
    public long addStation(Station[] station) {
        return 0;
    }

    @Override
    public ArrayList<Station> getAllStation() {
        return null;
    }

    @Override
    public Station getStation(String name) {
        return null;
    }

    @Override
    public Station getStation(String name, String line) {
        return null;
    }

    @Override
    public ArrayList<Station> searchStation(String piecesOfStation) {
        return null;
    }

    @Override
    public int deleteAllStation() {
        return 0;
    }
}
