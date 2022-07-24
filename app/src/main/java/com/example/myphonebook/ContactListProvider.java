package com.example.myphonebook;

import java.util.List;

public interface ContactListProvider {
    List<PhoneBookEntry> getContacts();
    void subscribe(ContactListReceiver clr);
    void unsubscribe(ContactListReceiver clr);
}
