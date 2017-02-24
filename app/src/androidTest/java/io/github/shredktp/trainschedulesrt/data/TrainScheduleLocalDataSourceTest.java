package io.github.shredktp.trainschedulesrt.data;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.shredktp.trainschedulesrt.data.source.train_schedule.TrainScheduleLocalDataSource;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Korshreddern on 23-Feb-17.
 */

@RunWith(AndroidJUnit4.class)
public class TrainScheduleLocalDataSourceTest {

    private TrainScheduleLocalDataSource trainScheduleLocalDataSource;

    @Before
    public void setup() {
        trainScheduleLocalDataSource = TrainScheduleLocalDataSource.getInstance(
                InstrumentationRegistry.getContext());
    }

//    @After
//    public void cleanUp() {
//        trainScheduleLocalDataSource.deleteAll();
//    }

    @Test
    public void testPreConditions() {
        assertNotNull(trainScheduleLocalDataSource);
    }


}
