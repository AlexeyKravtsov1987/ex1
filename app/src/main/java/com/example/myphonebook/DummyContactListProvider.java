package com.example.myphonebook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DummyContactListProvider implements ContactListProvider{
    List<Entry> dummy;
    List<ContactListReceiver> subscribers;
    public DummyContactListProvider() {
        subscribers = new ArrayList<>();
        dummy = Arrays.asList(new Entry[]{
                new PhoneBookEntry("First AS", "+01", "a@b.c", "android.resource://com.example.myphonebook/" + R.drawable.avatar),
                new PhoneBookEntry("Second CSD", "+02", "b@b.c", "android.resource://com.example.myphonebook/" + R.drawable.avatar),
                new PhoneBookEntry("Third FV", "+03", "c@b.c", null),
                new PhoneBookEntry("No Mail", "+04", null, "android.resource://com.example.myphonebook/" + R.drawable.avatar),
        });
    }


    @Override
    public List<Entry> getContacts() {
        return dummy;
    }

    @Override
    public void subscribe(ContactListReceiver clr) {
        subscribers.add(clr);
        clr.setData(this);
    }

    @Override
    public void unsubscribe(ContactListReceiver clr) {
        if(subscribers.contains(clr))
            subscribers.remove(clr);
        clr.setData(null);
    }
}
