package com.zmobile.ads;

import android.app.Activity;
import android.widget.FrameLayout;

import com.google.android.gms.ads.NativeExpressAdView;

/**
 * Created by lukasz on 27.07.2017.
 */

public interface ActivityInterface {

    public boolean showFaceNative = true;
    public boolean showFaceBanner = true;
    public boolean showAdmobBanner = true;
    public boolean showAdmobExpress = true;
    public boolean showAmazonBanner = true;

    public com.google.android.gms.ads.AdView adView = null;
    public com.amazon.device.ads.AdLayout amazonAd = null;
    public NativeExpressAdView adViewExpress = null;
    public FrameLayout nativeAdContainer = null;
    public FrameLayout fbBannerContainer = null;

    //public int faceNativeLayoutId = R.layout.ad_unit;
    int admobNativeLayoutId = 3;

    Activity act = null;
}
