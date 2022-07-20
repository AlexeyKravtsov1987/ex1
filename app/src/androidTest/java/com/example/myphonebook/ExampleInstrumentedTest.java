package com.example.myphonebook;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.Manifest;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;




/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.myphonebook", appContext.getPackageName());
    }
    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant (Manifest.permission.READ_CONTACTS);
    @Rule
    public ActivityTestRule<MainActivity> activityScenarioRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void scrollToItemBelowFold_checkItsText() {
        final int checkedPos = 0;
        RecyclerView rv=getText(onView(ViewMatchers.withId(R.id.contactList)));
        String firstContent=((TextView)rv.findViewHolderForAdapterPosition(checkedPos).itemView.findViewById(R.id.Name)).getText().toString();
        // First scroll to the position that needs to be matched and click on it.

        onView(ViewMatchers.withId(R.id.contactList))
                .perform(RecyclerViewActions.actionOnItemAtPosition(checkedPos,click()));

        onView(withText(firstContent)).check(matches(isDisplayed()));

    }
    public RecyclerView getText(ViewInteraction matcher) {
        final RecyclerView[] text = { null };
        matcher.perform(new ViewAction() {

            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(RecyclerView.class);
            }

            @Override
            public String getDescription() {
                return "Text of the view";
            }

            @Override
            public void perform(UiController uiController, View view) {
                RecyclerView tv = (RecyclerView)view;
                text[0] = tv;
            }
        });

        return text[0];
    }
}