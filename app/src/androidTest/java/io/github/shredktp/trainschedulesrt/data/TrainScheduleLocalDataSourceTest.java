package io.github.shredktp.trainschedulesrt.data;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import io.github.shredktp.trainschedulesrt.data.source.train_schedule.TrainScheduleLocalDataSource;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Korshreddern on 23-Feb-17.
 */

@RunWith(AndroidJUnit4.class)
public class TrainScheduleLocalDataSourceTest {

    private static final String START_STATION = "a";
    private static final String END_STATION = "s";
    private static final String NUMBER = "d";
    private static final String TYPE = "f";
    private static final String START_TIME = "1";
    private static final String END_TIME = "2";

    private TrainScheduleLocalDataSource trainScheduleLocalDataSource;

    @Before
    public void setup() {
        trainScheduleLocalDataSource = TrainScheduleLocalDataSource.getInstance(
                InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void testPreConditions() {
        assertNotNull(trainScheduleLocalDataSource);
    }

    @Test
    public void saveSchedule_retrievesSchedule() {
        ArrayList<TrainSchedule> trainScheduleArrayList = new ArrayList<>();
        TrainSchedule trainSchedule = new TrainSchedule(START_STATION, END_STATION, NUMBER, TYPE, START_TIME, END_TIME);

        trainScheduleArrayList.add(trainSchedule);

        trainScheduleLocalDataSource.add(trainScheduleArrayList);
        ArrayList<TrainSchedule> trainScheduleArrayListResult =
                trainScheduleLocalDataSource.getTrainScheduleByStation(START_STATION, END_STATION);

        assertEquals(trainSchedule.getStartStation(), trainScheduleArrayListResult.get(0).getStartStation());
        assertEquals(trainSchedule.getEndStation(), trainScheduleArrayListResult.get(0).getEndStation());
        assertEquals(trainSchedule.getNumber(), trainScheduleArrayListResult.get(0).getNumber());
        assertEquals(trainSchedule.getType(), trainScheduleArrayListResult.get(0).getType());
        assertEquals(trainSchedule.getStartTime(), trainScheduleArrayListResult.get(0).getStartTime());
        assertEquals(trainSchedule.getEndTime(), trainScheduleArrayListResult.get(0).getEndTime());
    }

    @After
    public void cleanUp() {
        trainScheduleLocalDataSource.deleteAll();
    }
}
