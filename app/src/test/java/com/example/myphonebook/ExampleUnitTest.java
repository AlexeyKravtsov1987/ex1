package com.example.myphonebook;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

import java.util.List;

import javax.inject.Inject;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
public class ExampleUnitTest {

    @Test
    public void getContactsWithoutPermission(){
        MainActivity activity=new MainActivity();
        activity.setAccessProvider(
                new ContactsReadAccessProvider() {
                    @Override
                    public boolean hasAccess() {
                        return false;
                    }

                    @Override
                    public boolean canAskUser() {
                        return false;
                    }
                }
        );
        activity.onResumeEx();
        ContactListProvider provider = activity.getCurrentContactListProvider();
        assertNotNull(provider);
        List<PhoneBookEntry> contacts = provider.getContacts();
        assertNotNull(contacts);
        assertEquals(provider instanceof ActualContactListProvider,false);
        assertEquals(contacts.size(),4);
    }
}