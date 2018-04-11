package com.zmobile.ads;

/**
 * Created by lukasz on 08.09.2017.
 */

public class AdFaceNative extends AdClass {

    private static AdFaceNative ourInstance;// = new AdFaceNative();

    public static AdFaceNative getInstance(AdsGeneralHandler ads) {
        if (ourInstance == null) ourInstance = new AdFaceNative(ads);
            ///ourInstance.ads = ads;

        return ourInstance;
    }

    private AdFaceNative() {
    }

    private AdFaceNative(AdsGeneralHandler ads){
        this.ads = ads;
        this.view = ads.nativeAdContainer;
        this.nameVal = ads.FACE;
        this.name = "faceNat";
    }

    public boolean isLoaded(){
        return ads.fbNativeAds.isAdLoaded();
    }

    public boolean showAd() {
        ads.showFacebookNativeAd(ads.act.faceNativeLayoutId);
        ads.setAdsVisible(view);
        showCount++;
        return false;
    }

    public boolean showInter() {
        return false;
    }
}
