package com.zmobile.ads;

/**
 * Created by lukasz on 08.09.2017.
 */

public class Sinleton {
    private static final Sinleton ourInstance = new Sinleton();

    public static Sinleton getInstance() {
        return ourInstance;
    }

    private Sinleton() {
    }
}
