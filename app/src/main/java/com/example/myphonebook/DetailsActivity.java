package com.example.myphonebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        TextView email = findViewById(R.id.emailTextView);
        TextView name = findViewById(R.id.nameTextView);
        TextView number = findViewById(R.id.phoneNumberTextView);
        ImageView bg = findViewById(R.id.imageView);
        Intent intent = getIntent();
        PhoneBookEntry entry = (PhoneBookEntry) intent.getExtras().get(MainActivity.CONTACT_ENTRY);
        if (entry != null) {
            email.setVisibility(View.GONE);
            bg.setImageDrawable(null);
            name.setText(entry.name());
            number.setText(entry.number());
            if (entry.getClass() == PhoneBookEntry.class) {
                if (entry.hasEMail()) {
                    email.setText(entry.getEMail());
                    email.setVisibility(View.VISIBLE);
                }
                if (entry.hasPicture()) {
                    bg.setImageURI(entry.getPictureUri());
                    bg.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}