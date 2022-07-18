package com.example.myphonebook;

import android.net.Uri;
import android.util.Log;

public class PhoneBookEntry implements Entry {
    private String Name,Number;

    EMailProvider eMailProvider;
    PictureProvider pictureProvider;

    public PhoneBookEntry(String Name, String Number, String email, String picUriStr){
        this.Name=Name;
        this.Number=Number;
        eMailProvider = new EMailProvider() {
            @Override
            public boolean hasEmail() {
                return email!=null;
            }

            @Override
            public String email() {
                return email;
            }
        };
        pictureProvider=new PictureProvider() {
            @Override
            public boolean hasPicture() {
                return picUriStr!=null;
            }

            @Override
            public Uri pic() {
                if(picUriStr==null)
                    return null;
                return Uri.parse(picUriStr);
            }
        };
    }


    @Override
    public String name() {
        return Name;
    }

    @Override
    public String number() {
        return Number;
    }

    /*@Override
    public boolean hasPicture() {
        return false;
    }*/
}
