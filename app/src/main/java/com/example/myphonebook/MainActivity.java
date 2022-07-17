package com.example.myphonebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {
    public static String CONTACT_ENTRY="CONTACT_ENTRY";
    private ContactListProvider myContactListProvider;
    private ActualContactListProvider myActualContactListProvider;
    MyRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myActualContactListProvider = new ActualContactListProvider<Cursor>();
        getLoaderManager().initLoader(0,null,myActualContactListProvider);
        setContactListProvider();
        RecyclerView recyclerView = findViewById(R.id.contactList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, myContactListProvider.getContacts());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    public void setContactListProvider(){
        ContactListProvider p = new ContactListProvider() {
            Entry[] dummy = new Entry[]{
                    new PhoneBookEntry("First AS","+01","a@b.c",false),
                    new PhoneBookEntry("Second CSD","+02","b@b.c",true),
                    new PhoneBookEntry("Third FV","+03","c@b.c",false),
                    new PhoneBookEntry("No Mail","+04",null,true),
            };
            @Override
            public List<Entry> getContacts() {
                return Arrays.asList(dummy);
            }
        };
        setMyContactListProvider(p);
    }

    public void setMyContactListProvider(ContactListProvider p){
        myContactListProvider=p;
    }

    public void onEntryClick(View sender){
        Intent intent = new Intent(this, Details.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, Details.class);
        intent.putExtra("CONTACT_ENTRY",adapter.getItem(position));
        startActivity(intent);
    }
}