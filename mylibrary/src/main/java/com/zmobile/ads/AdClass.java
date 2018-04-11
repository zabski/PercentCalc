package com.zmobile.ads;

import android.view.View;

import com.amazon.device.ads.Ad;

/**
 * Created by lukasz on 08.09.2017.
 */

abstract public class AdClass implements AdType {

    //private static final AdClass ourInstance = new AdClass();

    View view;
    int nameVal;
    public String name;
    public int showCount = 0;
    boolean loaded;
    AdsGeneralHandler ads;

    /*
    public static AdClass getInstance() {
        return ourInstance;
    }

    private AdClass(){
    }*/

    abstract public boolean isLoaded();

    public boolean showAd(){
        return false;
    }

    //abstract public boolean showInter();

}
