package com.example.myphonebook;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ActualContactListProvider implements
        ContactListProvider, android.app.LoaderManager.LoaderCallbacks<Cursor> {

    private final Context ctx;
    List<ContactListReceiver> subscribers;
    public Cursor mCursor=null;

    public ActualContactListProvider(Context ctx){
        this.ctx=ctx;
        subscribers=new ArrayList<>();
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return contactsLoader();
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor d) {
        mCursor=d;
        for(ContactListReceiver r:subscribers)
            r.setData(this.getContacts());
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        mCursor=null;
        for(ContactListReceiver r:subscribers)
            r.setData(this.getContacts());
    }

    private Loader<Cursor> contactsLoader() {
        Uri contactsUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // The content URI of the phone contacts
        String[] projection = {                                  // The columns to return for each row
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Photo.PHOTO_URI
        } ;
        String selection = null;                                 //Selection criteria
        String[] selectionArgs = {};                             //Selection criteria
        String sortOrder = null;                                 //The sort order for the returned rows
        return new CursorLoader(
                ctx,
                contactsUri,
                projection,
                selection,
                selectionArgs,
                sortOrder);
    }

    @Override
    public List<PhoneBookEntry> getContacts() {
        ArrayList <PhoneBookEntry> res = new ArrayList<>();
        if(mCursor==null ||
            !mCursor.moveToFirst())
            return res;

        do {
            String nameStr = mCursor.getString(0);
            String phoneStr = mCursor.getString(1);
            String emailStr = getEmail(mCursor.getLong(2));
            String photoUriStr = mCursor.getString(3);
            res.add(new PhoneBookEntry(nameStr, phoneStr, emailStr, photoUriStr));
        } while(mCursor.moveToNext());
        return res;
    }

    @Override
    public void subscribe(ContactListReceiver clr) {
        subscribers.add(clr);
    }

    @Override
    public void unsubscribe(ContactListReceiver clr) {
            subscribers.remove(clr);
    }
    @SuppressLint("Range")
    public String getEmail(long contactId) {
        ContentResolver contentResolver = ctx.getContentResolver();
        ArrayList<String> emails = new ArrayList<>();
        try (Cursor cursor = contentResolver
                .query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID
                                + "="
                                + contactId, null, null)) {
            while (cursor.moveToNext())
                emails.add(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)));
            if (emails.size() > 0)
                return emails.get(0);
        } catch (Exception e) {
        }
        return null;
    }
}
