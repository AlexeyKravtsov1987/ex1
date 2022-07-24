package com.example.myphonebook;

public interface ContactsReadAccessProvider {
    boolean hasAccess();
    boolean canAskUser();
}
