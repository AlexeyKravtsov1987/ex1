package com.example.myphonebook;

import android.net.Uri;

import java.io.Serializable;

public interface PictureProvider extends Serializable {
    boolean hasPicture();
    Uri pic();
}
