package com.example.myphonebook;

public class PhoneBookEntry implements Entry {
    private String Name,Number,EMail;

    EMailProvider eMailProvider;
    PictureProvider pictureProvider;

    public PhoneBookEntry(String Name, String Number, EMailProvider email, PictureProvider pic){
        this.Name=Name;
        this.Number=Number;
        eMailProvider=email;
        pictureProvider=pic;
    }
    public PhoneBookEntry(String Name, String Number, String email, boolean hasPic){
        this.Name=Name;
        this.Number=Number;
        this.EMail=email;
        eMailProvider = new EMailProvider() {
            @Override
            public boolean hasEmail() {
                return EMail!=null;
            }

            @Override
            public String email() {
                return EMail;
            }
        };
        pictureProvider=new PictureProvider() {
            @Override
            public boolean hasPicture() {
                return hasPic;
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
