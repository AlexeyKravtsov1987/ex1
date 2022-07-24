package com.example.myphonebook;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.platform.app.InstrumentationRegistry;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    public static String CONTACT_ENTRY="CONTACT_ENTRY";
    private Loader myLoader;
    private int myLoaderId=0;
    private boolean PermissionRequested=false;
    private Uri PhoneUri=ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    private Uri EMailUri=ContactsContract.CommonDataKinds.Email.CONTENT_URI;
    private ContactListProvider myContactListProvider;
    private RecyclerView recyclerView;
    private ContactsRecyclerViewAdapter adapter;
    private ContactsReadAccessProvider accessProvider = new ContactsReadAccessProvider() {
        @Override
        public boolean hasAccess() {
            return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    getApplicationContext().checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED);
        }

        @Override
        public boolean canAskUser() {
            return !PermissionRequested;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.contactList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactsRecyclerViewAdapter(getApplicationContext());

        recyclerView.setAdapter(adapter);
    }

    private void switchToContactListProvider(ContactListProvider clp){
        if(myContactListProvider!=null)
            myContactListProvider.unsubscribe(adapter);
        myContactListProvider=clp;
        myContactListProvider.subscribe(adapter);
    }

    private ContactListProvider initActualContactListProvider(Uri phoneUri, Uri emailUri){
        ContactListProvider res = new ActualContactListProvider(getApplicationContext(),
                phoneUri, emailUri);
        getLoaderManager().enableDebugLogging(true);
        if(myLoader==null)
            myLoader = getLoaderManager().initLoader(myLoaderId,null,(ActualContactListProvider) res);
        else {
            getLoaderManager().destroyLoader(myLoaderId);
            myLoader=getLoaderManager().initLoader(myLoaderId++,null,(ActualContactListProvider)res);}
        return res;
    }

    private ContactListProvider initDummyContactListProvider(){
        return new DummyContactListProvider();
    }

    public void setSourceUris(Uri newPhoneUri,Uri newEMailUri){
        PhoneUri=newPhoneUri;
        EMailUri=newEMailUri;
    }

    public ContactListProvider getCurrentContactListProvider(){
        return myContactListProvider;
    }

    public void setAccessProvider(ContactsReadAccessProvider crap){
        accessProvider=crap;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onResumeEx(){
        if (!accessProvider.hasAccess()) {
            if (accessProvider.canAskUser())
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 0);
            else
                switchToContactListProvider(initDummyContactListProvider());
        } else
            switchToContactListProvider(
                    initActualContactListProvider(PhoneUri,EMailUri));

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        onResumeEx();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0){
                if (grantResults.length < 1
                        || grantResults[0] == PackageManager.PERMISSION_DENIED)
                {
                    Toast.makeText(getApplicationContext(), R.string.contacts_permission_required,
                            Toast.LENGTH_SHORT).show();
                    PermissionRequested=true;
                    switchToContactListProvider(initDummyContactListProvider());
                }
                else if (grantResults.length == 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    switchToContactListProvider(
                            initActualContactListProvider(PhoneUri,EMailUri));
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(myContactListProvider!=null)
            myContactListProvider.unsubscribe(adapter);
    }

}