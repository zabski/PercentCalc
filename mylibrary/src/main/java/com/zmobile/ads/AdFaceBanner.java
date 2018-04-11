package com.zmobile.ads;

/**
 * Created by lukasz on 08.09.2017.
 */

public class AdFaceBanner extends AdClass {

    private static AdFaceBanner ourInstance;// = new AdFaceBanner();

    public static AdFaceBanner getInstance(AdsGeneralHandler ads) {
        if (ourInstance == null) ourInstance = new AdFaceBanner(ads);
        ///ourInstance.ads = ads;

        return ourInstance;
    }

    private AdFaceBanner() {
    }

    private AdFaceBanner(AdsGeneralHandler ads){
        this.ads = ads;
        this.view = ads.fbBannerContainer;
        this.nameVal = ads.FACE_BAN;
        this.name = "faceBan";
    }

    public boolean isLoaded(){
        return ads.fbBannerAdView.isEnabled();
    }

    public boolean showAd() {
        ads.showFaceBanner();
        ads.setAdsVisible(view);
        showCount++;
        return false;
    }

    public boolean showInter() {
        return false;
    }
}

