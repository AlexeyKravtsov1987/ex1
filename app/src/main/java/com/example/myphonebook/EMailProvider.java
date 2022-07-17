package com.example.myphonebook;

import java.io.Serializable;

public interface EMailProvider extends Serializable {
    boolean hasEmail();
    String email();
}
