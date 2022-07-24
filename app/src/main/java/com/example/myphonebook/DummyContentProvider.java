package com.example.myphonebook;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

public class DummyContentProvider extends ContentProvider {

    SQLiteDatabase dummyDB;
    SQLiteOpenHelper dbHelper;
    public static final String PROVIDER_NAME = "com.example.myphonebook.provider";

    static final int uriCodePhone = 1;
    static final int uriCodeEmail = 2;
    static final UriMatcher uriMatcher;

    static {

        // to match the content URI
        // every time user access table under content provider
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // to access whole table
        uriMatcher.addURI(PROVIDER_NAME, "PHONES", uriCodePhone);
        uriMatcher.addURI(PROVIDER_NAME, "EMAIL", uriCodeEmail);

        // to access a particular row
        // of the table
        uriMatcher.addURI(PROVIDER_NAME, "PHONES/*", uriCodePhone);
        uriMatcher.addURI(PROVIDER_NAME, "EMAIL/*", uriCodeEmail);
    }

    @Override
    public boolean onCreate() {
        if(dummyDB==null) {
            dbHelper = new SQLiteOpenHelper(getContext(), null, null, 1) {
                @Override
                public void onCreate(SQLiteDatabase db) {
                    String SQL_CREATE_PHONE_TABLE =  "CREATE TABLE " + "PHONES" + " ("
                            + ContactsContract.CommonDataKinds.Phone._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " TEXT NOT NULL, "
                            + ContactsContract.CommonDataKinds.Phone.NUMBER + " TEXT, "
                            + ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " INTEGER NOT NULL, "
                            + ContactsContract.CommonDataKinds.Photo.PHOTO_URI + " TEXT);";
                    String SQL_CREATE_EMAIL_TABLE =  "CREATE TABLE " + "EMAIL" + " ("
                            + ContactsContract.CommonDataKinds.Email._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + ContactsContract.CommonDataKinds.Email.ADDRESS + " TEXT, "
                            + ContactsContract.CommonDataKinds.Email.CONTACT_ID + " INTEGER NOT NULL);";

                    // Execute the SQL statement
                    db.execSQL(SQL_CREATE_PHONE_TABLE);
                    db.execSQL(SQL_CREATE_EMAIL_TABLE);

                }

                @Override
                public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

                }
            };
            dummyDB=dbHelper.getWritableDatabase();
            if(dummyDB==null)
                return false;
        }
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortingOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (uriMatcher.match(uri)) {
            case uriCodePhone:
                qb.setTables("PHONES");
                break;
            case uriCodeEmail:
                qb.setTables("EMAIL");
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        Cursor c = qb.query(dummyDB, projection, selection, selectionArgs,
                null, null, sortingOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        String TABLE_NAME="";
        switch (uriMatcher.match(uri)) {
            case uriCodePhone:
                TABLE_NAME="PHONES";
                break;
            case uriCodeEmail:
                TABLE_NAME="EMAIL";
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        long rowID = dummyDB.insert(TABLE_NAME, "", contentValues);

        final String URL = "content://" + PROVIDER_NAME + "/"+TABLE_NAME;
        final Uri CONTENT_URI = Uri.parse(URL);

        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLiteException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
