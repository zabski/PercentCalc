package com.zmobile.ads;

import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.ads.NativeExpressAdView;
import com.zmobile.mylibrary.R;

/**
 * Created by lukasz on 28.09.2017.
 */

public class AdviewsContainer {

    public boolean showFaceNative = true;
    public boolean showFaceBanner = true;
    public boolean showAdmobBanner = true;
    public boolean showAdmobExpress = true;
    public boolean showAmazonBanner = true;

    public com.google.android.gms.ads.AdView adView;
    public com.amazon.device.ads.AdLayout amazonAd;
    public NativeExpressAdView adViewExpress;
    public FrameLayout nativeAdContainer;
    public FrameLayout fbBannerContainer;
    public FrameLayout admobNativeContainer;

    public int faceNativeLayoutId = R.layout.ad_unit;
    int admobNativeLayoutId = 3;

    public void setAdsVisible(View viewToShow){
        int state = View.VISIBLE;
        //int state = View.GONE;
        if (adView!=null)
            //adView.setVisibility(View.GONE);
            //adView.setVisibility(View.VISIBLE);
            adView.setVisibility(state);
        if (nativeAdContainer!=null)
            //nativeAdContainer.setVisibility(View.GONE);
            //nativeAdContainer.setVisibility(View.VISIBLE);
            nativeAdContainer.setVisibility(state);
        if (adViewExpress!=null)
            //adViewExpress.setVisibility(View.GONE);
            //adViewExpress.setVisibility(View.VISIBLE);
            adViewExpress.setVisibility(state);
        if (fbBannerContainer!=null)
            //fbBannerContainer.setVisibility(View.GONE);
            //fbBannerContainer.setVisibility(View.VISIBLE);
            fbBannerContainer.setVisibility(state);
        if (amazonAd!=null)
            //amazonAd.setVisibility(View.GONE);
            //amazonAd.setVisibility(View.VISIBLE);
            amazonAd.setVisibility(state);
        if (viewToShow!=null)
            viewToShow.setVisibility(View.VISIBLE);
    }
}
