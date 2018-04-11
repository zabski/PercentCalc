package com.zmobile.ads;

/**
 * Created by lukasz on 08.09.2017.
 */

public class AdmobBanner extends AdClass {

    private static AdmobBanner ourInstance;// = new AdmobBanner();

    public static AdmobBanner getInstance(AdsGeneralHandler ads) {
        if (ourInstance == null) ourInstance = new AdmobBanner(ads);
            ///ourInstance.ads = ads;

        return ourInstance;
    }

    private AdmobBanner(){}

    private AdmobBanner(AdsGeneralHandler ads){
        this.ads = ads;
        this.view = ads.adView;
        this.nameVal = ads.ADMOB;
        this.name = "admobBan";
    }

    public boolean isLoaded(){
        return ads.adView.isEnabled();
    }

    public boolean showAd() {
        ads.adView.resume();
        ads.setAdsVisible(view);
        showCount++;
        return false;
    }

    public boolean showInter() {
        return false;
    }
}

