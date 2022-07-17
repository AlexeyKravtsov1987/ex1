package com.example.myphonebook;
import android.content.Loader;
import android.os.Bundle;

import androidx.annotation.Nullable;

import java.util.List;

public class ActualContactListProvider <D> implements
        ContactListProvider, android.app.LoaderManager.LoaderCallbacks<D> {

    @Override
    public Loader<D> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(android.content.Loader<D> loader, D d) {

    }

    @Override
    public void onLoaderReset(android.content.Loader<D> loader) {

    }



    @Override
    public List<Entry> getContacts() {
        return null;
    }
}
