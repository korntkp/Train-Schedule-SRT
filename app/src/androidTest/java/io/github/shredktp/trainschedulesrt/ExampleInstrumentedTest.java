package io.github.shredktp.trainschedulesrt;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.shredktp.trainschedulesrt.show_schedule.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private static final String EXPECTED_START_STATION_BKK = "กรุงเทพ";
    private static final String EXPECTED_START_STATION_KRA_BIAT = "กระเบียด";
    private Context appContext;

    @Before
    public void setup() {
        appContext = InstrumentationRegistry.getTargetContext();
    }

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class) {
            @Override
            public MainActivity getActivity() {
                return super.getActivity();
            }
    };

    @Test
    public void useAppContext() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("io.github.shredktp.trainschedulesrt", appContext.getPackageName());
    }

    @Test
    public void checkViewInMainActivity() {
        onView(withId(R.id.btn_select_start_station)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_select_end_station)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_see_schedule)).check(matches(isDisplayed()));
    }

    @Test
    public void selectStartStation() {
        onView(withId(R.id.btn_select_start_station)).perform(click());

        onView(withId(R.id.select_station_recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.select_station_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.btn_select_start_station))
                .check(matches(withText(EXPECTED_START_STATION_BKK)));
    }

    @Test
    public void selectEndStation() {
        onView(withId(R.id.btn_select_end_station)).perform(click());

        onView(withId(R.id.select_station_recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.select_station_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.btn_select_end_station))
                .check(matches(withText(EXPECTED_START_STATION_KRA_BIAT)));
    }

    @Test
    public void seeSchedule() {
        onView(withId(R.id.btn_select_start_station)).perform(click());

        onView(withId(R.id.select_station_recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.select_station_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.btn_select_start_station))
                .check(matches(withText(EXPECTED_START_STATION_BKK)));


        onView(withId(R.id.btn_select_end_station)).perform(click());

        onView(withId(R.id.select_station_recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.select_station_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.btn_select_end_station))
                .check(matches(withText(EXPECTED_START_STATION_KRA_BIAT)));

        onView(withId(R.id.btn_see_schedule)).perform(click());

//        onView(withId(R.id.list_view_schedule)).check(matches(isDisplayed()));
//        onView(withId(R.id.fab_see_it_first)).check(matches(isDisplayed()));
    }
}
