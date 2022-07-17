package com.example.myphonebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Details extends AppCompatActivity {
    Entry entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        TextView email = findViewById(R.id.emailTextView);
        TextView name = findViewById(R.id.nameTextView);
        TextView number = findViewById(R.id.phoneNumberTextView);
        ImageView bg = findViewById(R.id.imageView);
        Intent intent = getIntent();
        entry = (Entry) intent.getExtras().get(MainActivity.CONTACT_ENTRY);
        if (entry != null) {
            email.setVisibility(View.GONE);
            bg.setImageDrawable(null);
            name.setText(entry.name());
            number.setText(entry.number());
            if (entry.getClass() == PhoneBookEntry.class) {
                PhoneBookEntry pbe = (PhoneBookEntry) entry;
                if (pbe.eMailProvider.hasEmail()) {
                    email.setText(pbe.eMailProvider.email());
                    email.setVisibility(View.VISIBLE);
                }
                if (pbe.pictureProvider.hasPicture()) {
                    bg.setImageDrawable(getResources().getDrawable(R.drawable.avatar));
                    bg.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}