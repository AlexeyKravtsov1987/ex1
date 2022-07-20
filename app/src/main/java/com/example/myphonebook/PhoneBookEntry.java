package com.example.myphonebook;

import android.net.Uri;

import java.io.Serializable;

public class PhoneBookEntry implements Serializable {
    private final String Name;
    private final String Number;
    private final String eMail;
    private final String pictureUri;

    public PhoneBookEntry(String Name, String Number, String email, String picUriStr){
        this.Name=Name;
        this.Number=Number;
        this.eMail=email;
        this.pictureUri=picUriStr;
    }


    public String name() {
        return Name;
    }


    public String number() {
        return Number;
    }

    public boolean hasEMail(){
        return eMail!=null;
    }
    public String getEMail(){
        return eMail;
    }
    public boolean hasPicture(){
        return pictureUri!=null;
    }
    public Uri getPictureUri(){
        return Uri.parse(pictureUri);
    }
}
