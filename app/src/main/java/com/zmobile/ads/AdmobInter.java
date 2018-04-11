package com.zmobile.ads;

/**
 * Created by lukasz on 08.09.2017.
 */

public class AdmobInter extends AdClass {

    private static AdmobInter ourInstance;// = new AdmobBanner();

    public static AdmobInter getInstance(AdsGeneralHandler ads) {
        if (ourInstance == null) ourInstance = new AdmobInter(ads);
            ///ourInstance.ads = ads;

        return ourInstance;
    }

    private AdmobInter(){}

    private AdmobInter(AdsGeneralHandler ads){
        this.ads = ads;
        this.view = ads.adView;
        this.nameVal = ads.ADMOB;
        this.name = "admobInt";
        //this.loaded = ads.interstitial.isLoaded();
    }

    public boolean isLoaded() {
        return ads.interstitial.isLoaded();
    }

    public boolean showAd() {
        ads.showAdmobInter();
        //ads.setAdsVisible(view);
        showCount++;
        return false;
    }

    public boolean showInter() {
        return false;
    }
}

