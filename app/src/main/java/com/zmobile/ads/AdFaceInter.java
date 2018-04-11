package com.zmobile.ads;

/**
 * Created by lukasz on 08.09.2017.
 */

public class AdFaceInter extends AdClass {

    private static AdFaceInter ourInstance;// = new AdFaceBanner();

    public static AdFaceInter getInstance(AdsGeneralHandler ads) {
        if (ourInstance == null) ourInstance = new AdFaceInter(ads);
        ///ourInstance.ads = ads;

        return ourInstance;
    }

    private AdFaceInter() {
    }

    private AdFaceInter(AdsGeneralHandler ads){
        this.ads = ads;
        this.view = ads.fbBannerAdView;
        this.nameVal = ads.FACE_BAN;
        this.name = "faceInt";
        //loaded = ads.faceInterstitial.isAdLoaded();
    }

    public boolean isLoaded() {
        return ads.faceInterstitial.isAdLoaded();
    }

    public boolean showAd() {
        ads.showFaceInter();
        //ads.setAdsVisible(view);
        showCount++;
        return false;
    }

    public boolean showInter() {
        return false;
    }
}

