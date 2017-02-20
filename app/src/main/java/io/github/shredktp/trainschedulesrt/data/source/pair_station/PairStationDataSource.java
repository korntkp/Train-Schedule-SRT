package io.github.shredktp.trainschedulesrt.data.source.pair_station;

import java.util.ArrayList;

import io.github.shredktp.trainschedulesrt.data.PairStation;

/**
 * Created by Korshreddern on 11-Feb-17.
 */

public interface PairStationDataSource {

    long add(PairStation pairStation);

    long updateSeeItFirst(PairStation pairStation);

    PairStation getSeeFirstPairStation() throws Exception;
    ArrayList<PairStation> getAllPairStation();

    long deleteAll();
}
