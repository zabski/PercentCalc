package com.zmobile.ads;

/**
 * Created by lukasz on 08.09.2017.
 */

public class AdAmazonBanner extends AdClass {

    private static AdAmazonBanner ourInstance;// = new AdAmazonBanner();

    public static AdAmazonBanner getInstance(AdsGeneralHandler ads) {
        if (ourInstance == null) ourInstance = new AdAmazonBanner(ads);
            ///ourInstance.ads = ads;1
        return ourInstance;
    }

    private AdAmazonBanner() {
    }

    private AdAmazonBanner(AdsGeneralHandler ads){
        this.ads = ads;
        this.view = ads.amazonAd;
        this.nameVal = ads.AMAZON;
        this.name = "amazonBan";
    }

    public boolean isLoaded(){
        return ads.adsAmazon.isAdLoaded();
    }

    public boolean showAd() {
        ads.adsAmazon.showAd();
        ads.setAdsVisible(view);
        showCount++;
        return false;
    }

    public boolean showInter() {
        return false;
    }
}
