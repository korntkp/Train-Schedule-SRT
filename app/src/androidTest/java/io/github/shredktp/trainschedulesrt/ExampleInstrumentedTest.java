package io.github.shredktp.trainschedulesrt;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.github.shredktp.trainschedulesrt.Utils.ConnectionUtil;
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

    private static final int RECYCLER_START_STATION_POSITION_BKK = 0;
    private static final int RECYCLER_END_STATION_POSITION_KRA_BIAT = 1;

    private static final int RECYCLER_START_STATION_POSITION_KUD_JIK = 13;
    private static final int RECYCLER_END_STATION_POSITION_KUM_PWA_PEE = 14;

    private static final String EXPECTED_START_STATION_BKK = "กรุงเทพ";
    private static final String EXPECTED_START_STATION_KRA_BIAT = "กระเบียด";
    private static final String EXPECTED_START_STATION_KUD_JIK = "กุดจิก";
    private static final String EXPECTED_END_STATION_KUM_PWA_PEE = "กุมภวาปี";

    private Context appContext;
    private IdlingResource idlingResource;

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class) {
            @Override
            public MainActivity getActivity() {
                return super.getActivity();
            }
    };

    @Before
    public void setup() {
        appContext = InstrumentationRegistry.getTargetContext();
        idlingResource = mainActivityActivityTestRule.getActivity().getIdlingResource();
        Espresso.registerIdlingResources(idlingResource);
    }

    @Test
    public void useAppContext() throws Exception {
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
                .perform(RecyclerViewActions.actionOnItemAtPosition(RECYCLER_START_STATION_POSITION_KUD_JIK, click()));

        onView(withId(R.id.btn_select_start_station))
                .check(matches(withText(EXPECTED_START_STATION_KUD_JIK)));
    }

    @Test
    public void selectEndStation() {
        onView(withId(R.id.btn_select_end_station)).perform(click());

        onView(withId(R.id.select_station_recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.select_station_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(RECYCLER_END_STATION_POSITION_KUM_PWA_PEE, click()));

        onView(withId(R.id.btn_select_end_station))
                .check(matches(withText(EXPECTED_END_STATION_KUM_PWA_PEE)));
    }

    @Test
    public void seeSchedule() {
        onView(withId(R.id.btn_select_start_station)).perform(click());

        onView(withId(R.id.select_station_recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.select_station_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(RECYCLER_START_STATION_POSITION_BKK, click()));

        onView(withId(R.id.btn_select_start_station))
                .check(matches(withText(EXPECTED_START_STATION_BKK)));


        onView(withId(R.id.btn_select_end_station)).perform(click());

        onView(withId(R.id.select_station_recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.select_station_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(RECYCLER_END_STATION_POSITION_KRA_BIAT, click()));

        onView(withId(R.id.btn_select_end_station))
                .check(matches(withText(EXPECTED_START_STATION_KRA_BIAT)));

        onView(withId(R.id.btn_see_schedule)).perform(click());

        if (ConnectionUtil.isConnected(appContext)) {
            onView(withId(R.id.recycler_view_schedule)).check(matches(isDisplayed()));
//            onView(withId(R.menu.see_schedule_toolbar_menu)).check(matches(isDisplayed()));
        } else {
            onView(withId(R.id.layout_detail)).check(matches(isDisplayed()));
            onView(withId(R.id.tv_detail)).check(matches(isDisplayed()));
        }
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            Espresso.unregisterIdlingResources(idlingResource);
        }
    }
}
