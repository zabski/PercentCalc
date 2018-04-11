/*
 * Copyright (C) 2015 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zmobile.ads;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.zmobile.ads.AdsGeneralHandler;
//import com.zmobile.saveplan.ActivityTemplate;
import com.zmobile.mylibrary.R;

import java.util.List;

/**
 * A simple activity class that displays native ad formats.
 */
public class AdmobNativeAds { //extends ActionBarActivity {

    final String TAG = "ShowAds: "+getClass().getName();
    private static String ADMOB_AD_UNIT_ID;// = "ca-app-pub-3940256099942544/2247696110";
    private static String ADMOB_APP_ID;// = "ca-app-pub-3940256099942544~3347511713";

    private Button mRefresh;
    private CheckBox mRequestAppInstallAds;
    private CheckBox mRequestContentAds;
//    boolean isGrid = true;
    Context ctx;
    Activity act;
    View adView;
    View nativeAd;
    //int log;

    //public AdmobNativeAds(Activity context, AdsGeneralHandler adsHandle){
    public AdmobNativeAds(AdsGeneralHandler adsHandle){
        //this.ctx = context;
        //this.act = context;
        //adView = ((ActivityTemplate)act).adView;
        adView = adsHandle.adView;
        nativeAd = adsHandle.nativeAdContainer;
        //nativeAd = ((ActivityTemplate)act).nativeAdContainer;
        //log = ctx.getResources().getInteger(R.integer.log);
        ADMOB_AD_UNIT_ID = adsHandle.context.getString(R.string.admob_express_small_id);
        ADMOB_APP_ID = adsHandle.context.getString(R.string.admob_app_id);
    }

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, ADMOB_APP_ID);

        mRefresh = (Button) findViewById(R.id.btn_refresh);
        mRequestAppInstallAds = (CheckBox) findViewById(R.id.cb_appinstall);
        mRequestContentAds = (CheckBox) findViewById(R.id.cb_content);

        mRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshAd(mRequestAppInstallAds.isChecked(),
                        mRequestContentAds.isChecked());
            }
        });

        refreshAd(mRequestAppInstallAds.isChecked(),
                mRequestContentAds.isChecked());

    }
    */

    /**
     * Populates a {@link NativeAppInstallAdView} object with data from a given
     * {@link NativeAppInstallAd}.
     *
     * @param nativeAppInstallAd the object containing the ad's assets
     * @param adView             the view to be populated
     */
    private void populateAppInstallAdView(NativeAppInstallAd nativeAppInstallAd,
                                          NativeAppInstallAdView adView) {
        adView.setHeadlineView(adView.findViewById(R.id.appinstall_headline));
        adView.setImageView(adView.findViewById(R.id.appinstall_image));
        adView.setBodyView(adView.findViewById(R.id.appinstall_body));
        adView.setCallToActionView(adView.findViewById(R.id.appinstall_call_to_action));
        adView.setIconView(adView.findViewById(R.id.appinstall_app_icon));
        adView.setPriceView(adView.findViewById(R.id.appinstall_price));
        adView.setStarRatingView(adView.findViewById(R.id.appinstall_stars));
        adView.setStoreView(adView.findViewById(R.id.appinstall_store));

        // Some assets are guaranteed to be in every NativeAppInstallAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAppInstallAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAppInstallAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAppInstallAd.getCallToAction());
        ((ImageView) adView.getIconView()).setImageDrawable(nativeAppInstallAd.getIcon()
                .getDrawable());

        List<NativeAd.Image> images = nativeAppInstallAd.getImages();

        if (images.size() > 0) {
            ((ImageView) adView.getImageView()).setImageDrawable(images.get(0).getDrawable());
        }

        // Some aren't guaranteed, however, and should be checked.
        if (nativeAppInstallAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAppInstallAd.getPrice());
        }

        if (nativeAppInstallAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAppInstallAd.getStore());
        }

        if (nativeAppInstallAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAppInstallAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAppInstallAd);
    }

    /**
     * Populates a {@link NativeContentAdView} object with data from a given
     * {@link NativeContentAd}.
     *
     * @param nativeContentAd the object containing the ad's assets
     * @param adView          the view to be populated
     */
    private void populateContentAdView(NativeContentAd nativeContentAd,
                                       NativeContentAdView adView) {
        adView.setHeadlineView(adView.findViewById(R.id.contentad_headline));
        adView.setImageView(adView.findViewById(R.id.contentad_image));
        adView.setBodyView(adView.findViewById(R.id.contentad_body));
        adView.setCallToActionView(adView.findViewById(R.id.contentad_call_to_action));
        adView.setLogoView(adView.findViewById(R.id.contentad_logo));
        adView.setAdvertiserView(adView.findViewById(R.id.contentad_advertiser));

        // Some assets are guaranteed to be in every NativeContentAd.
        ((TextView) adView.getHeadlineView()).setText(nativeContentAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeContentAd.getBody());
        ((TextView) adView.getCallToActionView()).setText(nativeContentAd.getCallToAction());
        ((TextView) adView.getAdvertiserView()).setText(nativeContentAd.getAdvertiser());

        List<NativeAd.Image> images = nativeContentAd.getImages();

        if (images.size() > 0) {
            ((ImageView) adView.getImageView()).setImageDrawable(images.get(0).getDrawable());
        }

        // Some aren't guaranteed, however, and should be checked.
        NativeAd.Image logoImage = nativeContentAd.getLogo();

        if (logoImage == null) {
            adView.getLogoView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getLogoView()).setImageDrawable(logoImage.getDrawable());
            adView.getLogoView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeContentAd);
    }

    /**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     *
     * @param requestAppInstallAds indicates whether app install ads should be requested
     * @param requestContentAds    indicates whether content ads should be requested
     */
    public void refreshAd(final FrameLayout frameLayout, boolean requestAppInstallAds, boolean requestContentAds, final int layoutType) {
        if (!requestAppInstallAds && !requestContentAds) {
            Toast.makeText(ctx, "At least one ad format must be checked to request an ad.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        //final ActivityTemplate callAct = (ActivityTemplate)ctx;

        AdLoader.Builder builder = new AdLoader.Builder(ctx, ADMOB_AD_UNIT_ID);

        if (requestAppInstallAds) {
            builder.forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {

                public void onAppInstallAdLoaded(NativeAppInstallAd ad) {
                    int layoutInstall = R.layout.ad_app_install;
                    switch (layoutType){
                        case 1: layoutInstall = R.layout.ad_app_install_row; break;
                        //case 2: layoutInstall = R.layout.ad_app_install_grid; break;
                        case 3: layoutInstall = R.layout.ad_app_install; break;
                    }
                    //if (isGrid) layoutInstall = R.layout.ad_app_install_grid;
                    //FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fl_adplaceholder);
                    NativeAppInstallAdView adView = (NativeAppInstallAdView) act.getLayoutInflater()
                            .inflate(layoutInstall, null);
                            //.inflate(R.layout.ad_app_install, null);
                    populateAppInstallAdView(ad, adView);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                }
            });
        }

        if (requestContentAds) {
            builder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {

                public void onContentAdLoaded(NativeContentAd ad) {
                    int layoutContent = R.layout.ad_content;
                    /*
                    if (callAct.admobNativeLayoutId == 1)
                        layoutContent = R.layout.ad_content_row;
                    if (isGrid) layoutContent = R.layout.ad_content_grid;*/
                    switch (layoutType){
                        case 1: layoutContent = R.layout.ad_content_row; break;
                        //case 2: layoutContent = R.layout.ad_content_grid; break;
                        case 3: layoutContent = R.layout.ad_content; break;
                    }
                    //FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fl_adplaceholder);
                    NativeContentAdView adView = (NativeContentAdView) act.getLayoutInflater()
                            .inflate(layoutContent, null);
                            //.inflate(R.layout.ad_content, null);
                    populateContentAdView(ad, adView);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                }
            });
        }

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                //if (log>3) Toast.makeText(((ActivityTemplate)act), "AM Failed to load native ad: " + errorCode, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "AM Failed to load native ad: " + errorCode);
                if (nativeAd!=null)
                    nativeAd.setVisibility(View.GONE);
                //((ActivityTemplate)act).loadAds();
                //((ActivityTemplate)act).showAdMobExpressNativeAd();
            }

            @Override
            public void onAdLoaded() {
                //if (log>3) Toast.makeText(((ActivityTemplate)act), "Admob native loaded", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Admob native loaded");
                /*
                if (nativeAd!=null)
                    nativeAd.setVisibility(View.VISIBLE);
                    */
                if (adView!=null)
                    adView.setVisibility(View.GONE);
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }
}
