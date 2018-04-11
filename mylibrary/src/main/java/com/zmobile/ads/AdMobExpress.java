package com.zmobile.ads;

/**
 * Created by lukasz on 08.09.2017.
 */

public class AdMobExpress extends AdClass {

    private static  AdMobExpress ourInstance;// = new AdMobExpress();

    public static AdMobExpress getInstance(AdsGeneralHandler ads) {
        if (ourInstance == null) ourInstance = new AdMobExpress(ads);
            ///ourInstance.ads = ads;
        return ourInstance;
    }

    private AdMobExpress() {
    }

    public AdMobExpress(AdsGeneralHandler ads){
        this.ads = ads;
        this.view = ads.adViewExpress;
        this.nameVal = ads.ADMOB_EXP;
        this.name = "admobExp";
    }

    public boolean isLoaded(){
        return ads.adView.isEnabled();
    }

    public boolean showAd() {
        ads.showAdMobExpressNativeAd();
        ads.setAdsVisible(view);
        showCount++;
        return false;
    }

    public boolean showInter() {
        return false;
    }
}
