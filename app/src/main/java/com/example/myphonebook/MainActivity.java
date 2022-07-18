package com.example.myphonebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {
    public static String CONTACT_ENTRY="CONTACT_ENTRY";
    private boolean PermissionRequested=false;
    private ContactListProvider myContactListProvider;
    private ActualContactListProvider myActualContactListProvider;
    RecyclerView recyclerView;
    MyRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myContactListProvider = new DummyContactListProvider();

        recyclerView = findViewById(R.id.contactList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(getApplicationContext());

        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    void switchToActualData(){
        myActualContactListProvider = new ActualContactListProvider(getApplicationContext());
        getLoaderManager().initLoader(0,null,myActualContactListProvider);
        myActualContactListProvider.subscribe(adapter);
    }
    @Override
    protected void onResume(){
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                getApplicationContext().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            if(!PermissionRequested)
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},0);
        }
        else {
            switchToActualData();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                if (grantResults.length < 1
                        || grantResults[0] == PackageManager.PERMISSION_DENIED)
                {
                    Toast.makeText(getApplicationContext(), R.string.contacts_permission_required,
                            Toast.LENGTH_SHORT).show();
                    PermissionRequested=true;
                    myContactListProvider.subscribe(adapter);
                }
                else if (grantResults.length == 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    switchToActualData();
                break;
        }
    }


    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, Details.class);
        intent.putExtra("CONTACT_ENTRY",adapter.getItem(position));
        startActivity(intent);
    }
}