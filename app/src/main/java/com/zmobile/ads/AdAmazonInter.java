package com.zmobile.ads;

/**
 * Created by lukasz on 08.09.2017.
 */

public class AdAmazonInter extends AdClass {

    private static AdAmazonInter ourInstance;// = new AdAmazonBanner();

    public static AdAmazonInter getInstance(AdsGeneralHandler ads) {
        if (ourInstance == null) ourInstance = new AdAmazonInter(ads);
        ///ourInstance.ads = ads;1
        return ourInstance;
    }

    private AdAmazonInter() {
    }

    private AdAmazonInter(AdsGeneralHandler ads){
        this.ads = ads;
        this.view = ads.amazonAd;
        this.nameVal = ads.AMAZON;
        this.name = "amazonInt";
    }

    public boolean isLoaded(){
        return ads.adsAmazon.isInterstitalLoaded();
    }

    public boolean showAd() {
        ads.adsAmazon.showInterstitial();
        //ads.setAdsVisible(view);
        showCount++;
        return false;
    }

    public boolean showInter() {
        return false;
    }
}