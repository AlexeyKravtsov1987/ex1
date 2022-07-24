package com.example.myphonebook;

import android.net.Uri;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneBookEntry entry = (PhoneBookEntry) o;
        return Name.equals(entry.Name) && Number.equals(entry.Number) && Objects.equals(eMail, entry.eMail) && Objects.equals(pictureUri, entry.pictureUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Name, Number, eMail, pictureUri);
    }
}
