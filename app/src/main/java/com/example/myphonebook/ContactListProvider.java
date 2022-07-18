package com.example.myphonebook;

import java.util.List;

public interface ContactListProvider {
    List<Entry> getContacts();
    void subscribe(ContactListReceiver clr);
    void unsubscribe(ContactListReceiver clr);
}
