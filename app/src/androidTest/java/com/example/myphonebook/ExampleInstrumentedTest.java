package com.example.myphonebook;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import java.util.ArrayList;
import java.util.List;


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

    private MainActivity mainActivity;
    @Rule
    public
    ActivityScenarioRule a = new ActivityScenarioRule<>(MainActivity.class);
    @Test
    public void createDummyContentProvider() throws InterruptedException {
        a.getScenario().onActivity(activity -> {
            mainActivity=(MainActivity) activity;
        });
        final String firstName=" The First ";
        final String firstPhone=" +1 001 ";
        final String firstMail ="a@b.c";
        final String secondName=" The Second ";
        final String secondPhone=" +1 002 ";
        final String secondPicUriStr="android.resource://com.example.myphonebook/" + R.drawable.avatar;

        ContentValues phoneValues = new ContentValues();
        ContentValues emailValues = new ContentValues();
        Uri phoneURI=Uri.parse("content://" + DummyContentProvider.PROVIDER_NAME + "/PHONES");
        Uri emailURI=Uri.parse("content://" + DummyContentProvider.PROVIDER_NAME + "/EMAIL");


        mainActivity.setSourceUris(phoneURI,emailURI);

        a.getScenario().moveToState(Lifecycle.State.STARTED);
        a.getScenario().moveToState(Lifecycle.State.RESUMED);

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        ContentResolver contentResolver = appContext.getContentResolver();
        ContactListProvider dummy= mainActivity.getCurrentContactListProvider();
        List<PhoneBookEntry> list=dummy.getContacts();
        assertEquals(list.size(),0);

        // Create contact 0
        phoneValues.put( ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, firstName );
        phoneValues.put( ContactsContract.CommonDataKinds.Phone.NUMBER , firstPhone);
        phoneValues.put( ContactsContract.CommonDataKinds.Phone.CONTACT_ID, 0);
        emailValues.put(ContactsContract.CommonDataKinds.Email.ADDRESS , firstMail);
        emailValues.put(ContactsContract.CommonDataKinds.Email.CONTACT_ID,0);


        // inserting into database through content URI
        contentResolver.insert(phoneURI, phoneValues);
        contentResolver.insert(emailURI, emailValues);

        // Create contact 1
        phoneValues.put( ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, secondName );
        phoneValues.put( ContactsContract.CommonDataKinds.Phone.NUMBER , secondPhone);
        phoneValues.put( ContactsContract.CommonDataKinds.Phone.CONTACT_ID, 1);
        phoneValues.put( ContactsContract.CommonDataKinds.Phone.PHOTO_URI , secondPicUriStr);

        // inserting into database through content URI
        contentResolver.insert(phoneURI, phoneValues);

        Thread.sleep(100);
        list=dummy.getContacts();
        assertEquals(list.size(),2);

        PhoneBookEntry entry = list.get(0);
        // check contact 0
        assertEquals(entry.name(),firstName);
        assertEquals(entry.number(),firstPhone);
        assertEquals(entry.getEMail(),firstMail);
        assertEquals(entry.hasPicture(),false);

        entry = list.get(1);
        // check contact 1
        assertEquals(entry.name(),secondName);
        assertEquals(entry.number(),secondPhone);
        assertEquals(entry.getPictureUri(),Uri.parse(secondPicUriStr));
        assertEquals(entry.hasEMail(),false);
    }
    @Test
    public void scrollToItemBelowFold_checkItsText() {

        ActivityScenario scenario=a.getScenario();
        scenario.onActivity(activity -> mainActivity=(MainActivity)activity);
        scenario.moveToState(Lifecycle.State.RESUMED);

        final int checkedPos = 0;
        RecyclerView rv=getText(onView(ViewMatchers.withId(R.id.contactList)));
        if(rv.findViewHolderForAdapterPosition(0)==null)
            return;
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