package com.example.myphonebook;

import java.io.Serializable;

public interface Entry extends Serializable {
    String name();
    String number();
}
