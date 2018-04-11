package com.zmobile.ads;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdView;
import com.facebook.ads.NativeAdViewAttributes;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;
import com.zmobile.ads.facebook.FacebookNativeAdFragment;
import com.zmobile.percentcalc.ActivityTemplate;
import com.zmobile.percentcalc.R;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;


/**
 * Created by lukasz on 05.07.2017.
 */

public class AdsGeneralHandler {

    final String TAG = "ShowAds: "+getClass().getSimpleName();

    public static final int ADMOB = 0;
    public static final int FACE = 1;
    public static final int AMAZON = 2;
    public static final int FACE_BAN = 3;
    public static final int ADMOB_EXP = 4;
    public static final int ADMOB_NAT = 5;
    public static int lastInterShown = -1;
    public static int lastBannerShown = -1;

    Context context;
    ActivityTemplate act0;
    AdviewsContainer act;

    int ads, fb_ads, log;

    static String admobNativeId;
    static String faceInterId;
    String fbBannerId;
    static int faceInterShown = 0;
    static int admobInterShown = 0;
    static int amazonInterShown = 0;

    static int faceNativeShown = 0;
    static int faceBannerShown = 0;
    static int admobExpresShown = 0;
    static int admobBannerShown = 0;
    static int amazonBannerShown = 0;

    //int fb_adView_end_id = R.id.native_ad_container_end;
    //int fb_adView_front_id = R.id.native_ad_container_front;
    public FrameLayout fb_adView_end, fb_adView_front;


    int admobNativeLayoutId = 3;

    // ----- General -------

    double adShowBias = 0.2;
    static boolean adShown = false;
    static int interShown = 0;
    boolean showNativeAds = false;
    boolean showExpressAds = true;
    boolean showFaceAds = true;
    boolean showAmazonAds = true;

    // ----- AdMob ---------

    private static String ADMOB_APP_ID;
    AdRequest adRequest;
    public com.google.android.gms.ads.AdView adView;
    AdmobNativeAds admobNativeAds;
    public FrameLayout admobNativeContainer;

    // admob interstitial

    InterstitialAd interstitial;
    String interAdId;

    // abmob Native Express

    public NativeExpressAdView adViewExpress;

    /// ----- Facebook ------

    //facebook native ads
    public FrameLayout nativeAdContainer;
    FrameLayout fbBannerContainer;
    com.facebook.ads.AdView fbBannerAdView;
    private NativeAdView.Type viewType = NativeAdView.Type.HEIGHT_100;
    private NativeAdViewAttributes adViewAttributes = new NativeAdViewAttributes();
    String fbNativeId;
    //String fb_adNative_id = "703728306395880_725003760935001";
    int faceNativeLayoutId = R.layout.ad_unit;
    FacebookNativeAds fbNativeAds;
    private LinearLayout fb_adView;
    private NativeAd nativeAd;
    private AdChoicesView adChoicesView;

    com.facebook.ads.InterstitialAd faceInterstitial;

    // ------- Amazon

    com.amazon.device.ads.AdLayout amazonAd;
    AdsAmazonHandle adsAmazon;

    // ----- Remove ads

    HandlerPurchase handlerIAP;
    public boolean adsRemoved = false;

    PriorityQueue<AdClass> bannerQ;
    PriorityQueue<AdClass> interQ;



    /// ---------------- Constructor --------------------------

    public AdsGeneralHandler(Context context, AdviewsContainer act){
                            //, AdLayout amazonAd, com.google.android.gms.ads.AdView adView, NativeExpressAdView adViewExpress, NativeAd nativeAd, FrameLayout fbBannerContainer){

        this.context = context;
        this.act0 = (ActivityTemplate)context;
        this.act = act;//(ActivityTemplate)context;
        this.amazonAd = act.amazonAd;
        this.adView = act.adView;
        this.adViewExpress = act.adViewExpress;
        this.fbBannerContainer = act.fbBannerContainer;
        this.nativeAdContainer = act.nativeAdContainer;
        this.admobNativeContainer = act.admobNativeContainer;
        if (adView!=null)
            adView.setAdListener(getAdMobBannerListener);

        AdRequestBuilder AdBuilder = AdRequestBuilder.getInstance();
        adRequest = AdBuilder.build();

        ADMOB_APP_ID = context.getString(R.string.admob_app_id);
        MobileAds.initialize(context, ADMOB_APP_ID);
        //String interAd = getString(R.string.inter_ad);
        interAdId = context.getString(R.string.admob_inter_id);
        //admobNativeId = getString(R.string.admob_native_main_id);
        faceInterId = context.getString(R.string.facebook_inter_id);

        fbBannerId = context.getString(R.string.facebook_banner_id);


        fbNativeId = context.getString(R.string.facebook_native_id);

        ads = context.getResources().getInteger(R.integer.ads);
        fb_ads = context.getResources().getInteger(R.integer.fb_ads);
        log = context.getResources().getInteger(R.integer.log);
        //adShown = false;

        try {
            handlerIAP = HandlerPurchase.getInstance((ActivityTemplate) context);
            adsRemoved = handlerIAP.mHasRemovedAds;
        }catch (Exception e){
            Log.e(this.getClass().getName(),e.getMessage().toString());
        }

        fbNativeAds = new FacebookNativeAds();
        admobNativeAds = new AdmobNativeAds(this);
        adsAmazon = new AdsAmazonHandle(context, amazonAd);

        initQueues();

        if (!adsRemoved) {

            loadFacebookFullAd();
            initAdMobInter();

            //adsAmazon.loadAd();

            loadInter();
            //loadAds();
            //showAds();
        }
    }

    public void initQueues(){
        bannerQ=new PriorityQueue<AdClass>(5, new Comparator<AdClass>() {
            public int compare(AdClass t1, AdClass t2) {
                if (t1.showCount < t2.showCount) return -1;
                if (t1.showCount > t2.showCount) return 1;
                return 0;
            }
        });

        bannerQ.add(AdFaceBanner.getInstance(this));
        bannerQ.add(AdmobBanner.getInstance(this));
        bannerQ.add(AdAmazonBanner.getInstance(this));
        bannerQ.add(AdFaceNative.getInstance(this));
        bannerQ.add(AdMobExpress.getInstance(this));

        interQ=new PriorityQueue<AdClass>(3, new Comparator<AdClass>() {
            public int compare(AdClass t1, AdClass t2) {
                if (t1.showCount < t2.showCount) return -1;
                if (t1.showCount > t2.showCount) return 1;
                return 0;
            }
        });

        interQ.add(AdFaceInter.getInstance(this));
        interQ.add(AdmobInter.getInstance(this));
        interQ.add(AdAmazonInter.getInstance(this));
    }

    private void initAdMobInter(){
        interAdId = context.getString(R.string.admob_inter_id);
        interstitial = new InterstitialAd(context);
        interstitial.setAdUnitId(interAdId);
    }

    public void showInterstitial(){
        showInterstitial5();
    }

    /// ---------------- Methods --------------------------

    public void loadAdMobInter(){
        try {
            if (interstitial != null) {
                String id = interstitial.getAdUnitId();
                if (id == null) {
                    interAdId = context.getString(R.string.admob_inter_id);
                    interstitial.setAdUnitId(interAdId);
                }
                interstitial.setAdListener(getAdMobInterListener);
                if (!interstitial.isLoaded() && !interstitial.isLoading())
                    interstitial.loadAd(adRequest);
            }
        }catch (Exception e){
            toastMsg("AdMob Inter Init error: "+e.getMessage(),2);
        }
    }

    public void loadFaceInter() {
        try {
            if (faceInterstitial!=null) {
                if (!faceInterstitial.isAdLoaded())
                    faceInterstitial.loadAd();
            }
        } catch (Exception e) {
            toastMsg("Face Inter Init error: " + e.getMessage(), 2);
        }
    }

    public void loadInter(){
        loadAdMobInter();
        loadFaceInter();
    }

    public void showAdmobInter(){
        interstitial.show();
        admobInterShown++;
        interShown++;
        lastInterShown = ADMOB;
        return;
    }

    public void showFaceInter(){
        faceInterstitial.show();
        faceInterShown++;
        interShown++;
        lastInterShown = FACE;
        return;
    }

    public void showAmazonInter(){
        adsAmazon.showInterstitial();
        amazonInterShown++;
        interShown++;
        lastInterShown = AMAZON;
        return;
    }

    public void loadAds(){
        if (ads > 0 ) { //&& !handlerIAP.mHasRemovedAds) {
            if (ads > 1 && adView!=null) {
                //setAdsVisible(adView);
                Log.w("Show Ads: ", "WTF?");
                adView.loadAd(adRequest);
                Log.w("Show Ads: ", "Post WTF?");
                //adView.resume();
                lastBannerShown = ADMOB;
            }

        }
        if (adView != null) {
            if (ads < 1) { // || handlerIAP.mHasRemovedAds) {
                adView.setVisibility(View.GONE);
            }else{
                adView.setVisibility(View.VISIBLE);
            }
        }
        if (fb_ads == 0 && ads == 0) { //|| handlerIAP.mHasRemovedAds) {
            if (nativeAdContainer != null) nativeAdContainer.setVisibility(View.GONE);
        }
        if (showAmazonAds){
            adsAmazon.loadAd();
        }
    }

    public void showAds0(){

        //showFacebookNativeAd(act.faceNativeLayoutId);
        //showAdmobNativeAd();
        //showAdMobExpressNativeAd();
        if (act.showFaceNative && lastBannerShown!=FACE)
            showFacebookNativeAd(act.faceNativeLayoutId);
            else hideFaceNativ();
        if (act.showFaceBanner && lastBannerShown!=FACE_BAN)
            showFaceBanner();
            else hideFaceBanner();
        /*
        if (act.showNativeAds && lastBannerShown!=ADMOB_NAT)
            showAdmobNativeAd();
        */
        if (act.showAdmobExpress && lastBannerShown!=ADMOB_EXP)
            showAdMobExpressNativeAd();
            else hideAdmobExpress();
        if (act.showAmazonBanner && lastBannerShown!=AMAZON)
            adsAmazon.showAd();
            else hideAmazonBanner();
        if (adView != null) adView.resume();

    }

    boolean checkIfGreatest(int s){
        //return false;
        int counter = 0;
        if (s<=admobBannerShown) counter++;
        if (s<=admobExpresShown) counter++;
        if (s<=faceBannerShown) counter++;
        if (s<=faceNativeShown) counter++;
        if (s<=amazonBannerShown) counter++;
        if (counter<=1) return true;
        else return false;
    }

    public void showAds(){

        //showFacebookNativeAd(act.faceNativeLayoutId);
        //showAdmobNativeAd();
        //showAdMobExpressNativeAd();

        //boolean faceNativeLoaded = fbNativeAds.isAdLoaded();
        //boolean abmobBannerLoaded = adView.req
        String className = this.getClass().toString();

        Log.d("Show Ads: "+className, "admobBan="+admobBannerShown+" faceNat="+faceNativeShown+" amazon="+amazonBannerShown+" faceBan="+faceBannerShown+" admobEx="+admobExpresShown);

        if (act.showFaceNative){
            if (!checkIfGreatest(faceNativeShown)){
                //showFacebookNativeAd(act.faceNativeLayoutId);
                showFacebookNativeAd();
                if (fbNativeAds.loadError==false) {
                    faceNativeShown++;
                    act.setAdsVisible(nativeAdContainer);
                    return;
                }
            }
        }else hideFaceNativ();

        if (act.showAdmobBanner){
            if (!checkIfGreatest(admobBannerShown) || fbNativeAds.loadError) {
                admobBannerShown++;
                act.setAdsVisible(adView);
                adView.loadAd(adRequest);
                return;
            }
        }else hideAdmobBanner();

        if (act.showAmazonBanner){
            if (!checkIfGreatest(amazonBannerShown)){
                adsAmazon.showAd();
                amazonBannerShown++;
                act.setAdsVisible(amazonAd);
                return;
            }
        }else hideAmazonBanner();

        if (act.showFaceBanner) {
            if (!checkIfGreatest(faceBannerShown)) {
                showFaceBanner();
                faceBannerShown++;
                act.setAdsVisible(fbBannerAdView);
                return;
            }
        }else hideFaceBanner();

        if (act.showAdmobExpress){
            if (!checkIfGreatest(admobExpresShown)) {
                showAdMobExpressNativeAd();
                admobExpresShown++;
                act.setAdsVisible(adViewExpress);
                return;
            }
        }else hideAdmobExpress();

        /*
        if (act.showNativeAds && lastBannerShown!=ADMOB_NAT)
            showAdmobNativeAd();
        */


    }


    public void showAds3(){
        /*
        PriorityQueue<Integer> queue=new PriorityQueue<Integer>();
        queue.add(ADMOB);
        queue.add(ADMOB_EXP);
        queue.add(FACE);
        queue.add(FACE_BAN);
        queue.add(AMAZON);
        queue.*/



        AdClass minAd = bannerQ.poll();
        minAd.showAd();
        bannerQ.add(minAd);

        Iterator<AdClass> adsIiter = bannerQ.iterator();
        String report = "AdsBan: ";
        while (adsIiter.hasNext()) {
            AdClass ad = adsIiter.next();
            report += ad.name+"="+ad.showCount+" ";
        }
        Log.d("ShowAds: ", report);

    }

    private void hideFaceNativ(){
        if (nativeAdContainer!=null)
            nativeAdContainer.setVisibility(View.GONE);
    }

    private void hideFaceBanner(){
        if (fbBannerContainer!=null)
            fbBannerContainer.setVisibility(View.GONE);
    }

    private void hideAdmobExpress(){
        if (adViewExpress!=null)
            adViewExpress.setVisibility(View.GONE);
    }

    private void hideAdmobBanner(){
        if (adView!=null)
            adView.setVisibility(View.GONE);
    }

    private void hideAmazonBanner(){
        if (amazonAd!=null)
            amazonAd.setVisibility(View.GONE);
    }

    public void showFaceBanner(){
        //fbBannerAdView = new com.facebook.ads.AdView(context, fbBannerId, com.facebook.ads.AdSize.RECTANGLE_HEIGHT_250);
        fbBannerAdView = new com.facebook.ads.AdView(context, fbBannerId, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        //fbBannerAdView.setAdListener();
        if (fbBannerContainer!=null)
            fbBannerContainer.addView(fbBannerAdView);
        fbBannerAdView.loadAd();
        lastBannerShown = FACE_BAN;
    }

    public void showInterstitial3(){
        double rand = Math.random();
        /*
        if (handlerIAP!=null)
            if (handlerIAP.mHasRemovedAds)
                return;*/
        if (ads == 0 || rand > adShowBias) // || adShown)
            return;
        boolean admobLoaded = interstitial.isLoaded();
        boolean faceLoaded = faceInterstitial.isAdLoaded();
        boolean amazonLoaded = adsAmazon.isInterstitalLoaded();
        interShown++;
        if (interShown % 4 < 2) {
            if (admobLoaded) {
                interstitial.show();
                admobInterShown++;
                //interstitial.loadAd(adRequest);
                //toastMsg("Admob shown: "+admobInterShown+" face: "+faceInterShown, 3);
            }else{
                interstitial.loadAd(adRequest);
                if (faceLoaded)
                    faceInterstitial.show();
                }
            //ActivityMenu.fbFullAd.show();
        }else if (interShown % 4 < 3){
            if (faceLoaded) {
                faceInterstitial.show();
                faceInterShown++;
                //toastMsg("Face shown: "+faceInterShown+" admob "+admobInterShown, 3);
            } else {
                faceInterstitial.loadAd();
                if (admobLoaded)
                    interstitial.show();
            }
        }else if (amazonLoaded) {
            adsAmazon.showInterstitial();
        }else {
                    adsAmazon.loadInterstitial();
                    if (admobLoaded) {
                        interstitial.show();

                    }
                }


        toastMsg("Inter shown: "+interShown+" Admob: "+ admobInterShown +" face: "+ faceInterShown +" amazon: "+ amazonInterShown, 2);
    }

    public void showInterstitial4() {
        double rand = Math.random();
        if (ads == 0 || rand > adShowBias) // || adShown)
            return;
        boolean admobLoaded = interstitial.isLoaded();
        boolean faceLoaded = faceInterstitial.isAdLoaded();
        boolean amazonLoaded = adsAmazon.isInterstitalLoaded();
        toastMsg("Inter shown: "+interShown+" Admob: "+ admobInterShown +" face: "+ faceInterShown +" amazon: "+ amazonInterShown, 2);

        if (faceLoaded) {
            if (lastInterShown != FACE || (!amazonLoaded && !admobLoaded)) {
                faceInterstitial.show();
                faceInterShown++;
                interShown++;
                lastInterShown = FACE;
                return;
            }
        }
        if (amazonLoaded) {
            if (lastInterShown != AMAZON || (!faceLoaded && !admobLoaded)) {
                adsAmazon.showInterstitial();
                amazonInterShown++;
                interShown++;
                lastInterShown = AMAZON;
                return;
            }
        }
        if (admobLoaded){
            if (lastInterShown != ADMOB || (!faceLoaded && !amazonLoaded)) {
                interstitial.show();
                admobInterShown++;
                interShown++;
                lastInterShown = ADMOB;
                return;
            }
        }

    }

    public void showInterstitial5() {
        double rand = Math.random();
        if (ads == 0 || rand > adShowBias) // || adShown)
            return;
        boolean admobLoaded = interstitial.isLoaded();
        boolean faceLoaded = faceInterstitial.isAdLoaded();
        boolean amazonLoaded = adsAmazon.isInterstitalLoaded();
        toastMsg("Inter shown: "+interShown+" Admob: "+ admobInterShown +" face: "+ faceInterShown +" amazon: "+ amazonInterShown, 2);
        Log.d("Show Int:", "Inter: "+interShown+" Admob: "+ admobInterShown +" face: "+ faceInterShown +" amazon: "+ amazonInterShown);
        if (admobLoaded){
            if (!faceLoaded && !amazonLoaded) {
                showAdmobInter();
                return;
            }else if (admobInterShown<=faceInterShown || admobInterShown<=amazonInterShown) {
                showAdmobInter();
                return;
            }
        }
        if (faceLoaded) {
            if (!amazonLoaded && !admobLoaded) {
                showFaceInter();
                return;
            }else if (faceInterShown<=amazonInterShown || faceInterShown<=amazonInterShown) {
                showFaceInter();
                return;
            }
        }
        if (amazonLoaded) {
            if (!faceLoaded && !admobLoaded){
                showAmazonInter();
                return;
            }else if (amazonInterShown<=faceInterShown || amazonInterShown<=admobInterShown){
                showAmazonInter();
                return;
            }
        }

    }

    public void showInterstitial6(){
        AdClass minAd = interQ.poll();
        /*
        while (!minAd.isLoaded()) {
            minAd.showCount++;
            minAd = interQ.peek();
            Log.d("ShowAds: Q: ", minAd.name+"="+minAd.showCount);
        }*/
        minAd.showAd();
        interQ.add(minAd);
        Log.d("ShowAds: Q: ", minAd.name+"="+minAd.showCount);

        Iterator<AdClass> adsIiter = interQ.iterator();
        String report = "AdsInter: ";
        while (adsIiter.hasNext()) {
            AdClass ad = adsIiter.next();
            report += ad.name+"="+ad.showCount+" ";
        }
        Log.d("ShowAds: ", report);

    }

/*
    public void setAdsVisible(View viewToShow){
        if (adView!=null)
            adView.setVisibility(View.VISIBLE);
            //adView.setVisibility(View.GONE);
        if (nativeAdContainer!=null)
            nativeAdContainer.setVisibility(View.VISIBLE);
            //nativeAdContainer.setVisibility(View.GONE);
        if (adViewExpress!=null)
            adViewExpress.setVisibility(View.VISIBLE);
            //adViewExpress.setVisibility(View.GONE);
        if (viewToShow!=null)
            viewToShow.setVisibility(View.VISIBLE);
    }*/

    public void setAdsVisible(View viewToShow){
        if (adView!=null)
            adView.setVisibility(View.GONE);
        //adView.setVisibility(View.VISIBLE);
        if (nativeAdContainer!=null)
            nativeAdContainer.setVisibility(View.GONE);
        //nativeAdContainer.setVisibility(View.VISIBLE);
        if (adViewExpress!=null)
            adViewExpress.setVisibility(View.GONE);
        //adViewExpress.setVisibility(View.VISIBLE);
        if (fbBannerContainer!=null)
            fbBannerContainer.setVisibility(View.GONE);
        //fbBannerContainer.setVisibility(View.VISIBLE);
        if (amazonAd!=null)
            amazonAd.setVisibility(View.GONE);
        //amazonAd.setVisibility(View.VISIBLE);
        if (viewToShow!=null)
            viewToShow.setVisibility(View.VISIBLE);
    }


    // ---------------------------- facebook inter ------------------------------------

    private void loadFacebookFullAd() {
        String fbFullAdId = context.getString(R.string.facebook_inter_id);
        faceInterstitial = new com.facebook.ads.InterstitialAd(context, fbFullAdId);
        faceInterstitial.setAdListener(getFacebookFullAdListener);
        faceInterstitial.loadAd();
    }

    InterstitialAdListener getFacebookFullAdListener = new InterstitialAdListener() {

        public void onInterstitialDisplayed(Ad ad) { }

        public void onInterstitialDismissed(Ad ad) {
            //ActivityMenu.fbFullAd.loadAd();
            faceInterstitial.loadAd();
        }

        public void onError(Ad ad, AdError adError) {
            //toastMsg("Face inter error: "+adError.getErrorMessage(), 3);
            String msg = "Face inter error: "+adError.getErrorMessage();
            //Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            toastMsg(msg,3);
            Log.d("Show Ads: ", msg);
        }

        public void onAdLoaded(Ad ad) {
            String msg = "Face inter loaded";
            //Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            toastMsg(msg,3);
            Log.d("Show Ads: ", msg);
        }

        public void onAdClicked(Ad ad) {}

        public void onLoggingImpression(Ad ad) {

        }
    };

    // ---------------------------- facebook native ------------------------------------

    private void showFacebookNativeAd(){
        Class fragmentClass = FacebookNativeAdFragment.class;
        android.app.Fragment fragment = null;
        //android.support.v4.app.Fragment fragment = null;
        try {
            fragment = (android.app.Fragment) fragmentClass.newInstance();
            //fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        android.app.FragmentManager fragmentManager = act0.getFragmentManager();
                //getFragmentManager();
        //android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        //getSupportFragmentManager();
        //android.support.v4.app.FragmentManager;
        //getSupportFragmentManager();
        if (nativeAdContainer!=null) {
            try {
                fragmentManager.beginTransaction().replace(R.id.native_ad_container_end, fragment).commit();
            } catch (Exception e) {
                String err = e.getMessage();
            }
        }
    }

    public void showFacebookNativeAd(int layoutId){

        //if (nativeAdContainer==null) nativeAdContainer = (FrameLayout) findViewById(R.id.native_ad_container);
        if (nativeAdContainer==null)
            return;

        //LinearLayout ll = new LinearLayout(context);
        FrameLayout ll = new FrameLayout(context);
        //LinearLayout ll = nativeAdContainer;
        //LinearLayout ll2 = ll;

        fbNativeAds = new FacebookNativeAds();
        //fbNativeAds.createAndLoadNativeAd(this, ll, R.layout.ad_unit_grid);
        fbNativeAds.createAndLoadNativeAd(context, ll, layoutId, fbNativeId);

        //v = vi.inflate(R.layout.ad_container, null);
        //ll2 = (LinearLayout) ll.getChildAt(0);
        //View adview = (View) ll2.getChildAt(1);
        //ViewGroup.LayoutParams lp = v.getLayoutParams();
        //AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ll2.getLayoutParams());
        //ll2.setLayoutParams(lp);
        //ListView lv = new ListView(c);
        //lv.addView(v,0);
        //View v2 = lv.getChildAt(0);
        //AbsListView.
        //LinearLayout.
        //ViewGroup.LayoutParams lp = convertView.getLayoutParams();
        //adview.setLayoutParams(lp);
        //((ViewGroup)ll2.getParent()).removeAllViews();

        if (ll.getChildCount()<1) {
            //showAdmobNativeAd();
            fbNativeAds.loadError = true;
            Log.e("Show Ads:", "Face native not created");
        }else {
            setAdsVisible(nativeAdContainer);
            nativeAdContainer.removeAllViews();
            nativeAdContainer.addView(ll);
        }

    }

    public void onFbNativeAdError(){
        showAdmobNativeAd();
    /*
            nativeAdContainer.setVisibility(View.GONE);
        if (adView != null) {
            if (ads > 3 && !handlerIAP.mHasRemovedAds) {
                adView.setVisibility(View.VISIBLE);
                adView.loadAd(adRequest);
            }else{
                adView.setVisibility(View.GONE);
            }
        }*/

    }

    public void onFbNativeAdLoaded(){

        if (adView != null)
            adView.setVisibility(View.GONE);
        if (nativeAdContainer!=null)
            nativeAdContainer.setVisibility(View.VISIBLE);

    }
/*

    protected void createAndLoadNativeAd() {

        LayoutInflater inflater = LayoutInflater.from(this);
        fb_adView = (LinearLayout) inflater.inflate(R.layout.ad_unit, nativeAdContainer, false);
        nativeAdContainer.addView(fb_adView);

        //nativeAdStatus = (TextView) findViewById(R.id.native_ad_status);
        //showNativeAdButton = (Button) findViewById(R.id.load_native_ad_button);

        nativeAd = new NativeAd(ActivityTemplate.this, adNativeId);

        // Set a listener to get notified when the ad was loaded.
        nativeAd.setAdListener(ActivityTemplate.this);

        // When testing on a device, add its hashed ID to force test ads.
        // The hash ID is printed to log cat when running on a device and loading an ad.
        // AdSettings.addTestDevice("THE HASHED ID AS PRINTED TO LOG CAT");

        // Initiate a request to load an ad.
        nativeAd.loadAd(NativeAd.MediaCacheFlag.ALL);


    }

	*/

    // ---------------------------- Ad Mob Native --------------------------------------------------

    public void showAdmobNativeAd(){
        //LinearLayout ll = new LinearLayout(this);
        FrameLayout fl = new FrameLayout(context);
        //LinearLayout ll = nativeAdContainer;
        //admobNativeAds = new AdmobNativeAds((Activity) context, this);
        admobNativeAds = new AdmobNativeAds(this);
        admobNativeAds.refreshAd(fl, true, true, admobNativeLayoutId);
        //LinearLayout l;
        //ll2 = ll;
        ViewGroup parent = ((ViewGroup)fl.getParent());
        if (parent!=null)
            parent.removeAllViews();
        /*
        if (ll.getChildCount()<1) {
            loadAds();
        }else{
        */
        //if (nativeAdContainer==null) nativeAdContainer = (FrameLayout) findViewById(R.id.native_ad_container);
        if (admobNativeContainer!=null){
            //setAdsVisible(nativeAdContainer);
            //nativeAdContainer.removeAllViews();
            admobNativeContainer.addView(fl);
        }
    }

    // ---------------------------- Ad Mob Native Express ------------------------------------------

    public void showAdMobExpressNativeAd(){
        //if (nativeAdContainer!=null)
        //nativeAdContainer.setVisibility(View.GONE);
        if (adViewExpress!=null){
            if (!act.showAdmobExpress){
                adViewExpress.setVisibility(View.GONE);
                return;
            }
            //adViewExpress.setVisibility(View.VISIBLE);

            adViewExpress.setAdListener(new com.google.android.gms.ads.AdListener() {
                @Override
                public void onAdFailedToLoad(int errorCode) {
                    //loadError = true;
                    //if (log>1) Toast.makeText(context, "Express Failed to load native ad: " + errorCode, Toast.LENGTH_SHORT).show();
                    toastMsg("Express Failed to load native ad: " + errorCode, 3);
                    //((ActivityTemplate)act).showAdMobExpressNativeAd();
                    //((ActivityTemplate)context).
                    //loadAds();
                    hideAdmobExpress();
                    //adViewExpress.setVisibility(View.GONE);
                }

                public void onAdLoaded(){
                    //loadError = true;//false;
                    //if (log>1) Toast.makeText(context, "Express native loaded!", Toast.LENGTH_SHORT).show();
                    toastMsg("Express native loaded!",3);
                    //setAdsVisible(adViewExpress);
                    //ActivityTemplate ctxTemp = ((ActivityTemplate) ctx);
                    //ctxTemp.nativeAdContainer.setVisibility(View.VISIBLE);
                }
            });
            if (adViewExpress.getAdSize() == null)
                adViewExpress.setAdSize(AdSize.MEDIUM_RECTANGLE);
            if (adViewExpress.getAdUnitId() == null)
                adViewExpress.setAdUnitId(context.getString(R.string.admob_express_small_id));
            adViewExpress.loadAd(new AdRequest.Builder().build());
        }
    }
    // ------------------------------ AdMob listeners -----------------------------

    com.google.android.gms.ads.AdListener getAdMobBannerListener = new com.google.android.gms.ads.AdListener(){

        @Override
        public void onAdFailedToLoad(int errorCode){
            toastMsg("AdMob banner load error: "+errorCode, 3);
            //hideAdmobBanner();
            //AdsGeneralHandler.this.act.onAdLoadError();
            AdsGeneralHandler.this.showAds();
        }

        @Override
        public void onAdLoaded(){
            lastBannerShown = ADMOB;
            toastMsg("AdMob banner loaded", 3);
        }

        //@Override
        public void onError(Ad ad, AdError adError) {
            toastMsg("AdMob inter load error: "+adError.getErrorMessage(), 3);

        }

        //@Override
        public void onAdLoaded(Ad ad) {
            toastMsg("AdMob inter loaded", 3);

        }

        //@Override
        public void onAdClicked(Ad ad) {

        }
    };

    com.google.android.gms.ads.AdListener getAdMobInterListener = new com.google.android.gms.ads.AdListener(){

        @Override
        public void onAdFailedToLoad(int errorCode){
            toastMsg("AdMob inter load error: "+errorCode, 3);
        }

        @Override
        public void onAdLoaded(){
            toastMsg("AdMob inter loaded", 3);
        }

        //@Override
        public void onError(Ad ad, AdError adError) {
            toastMsg("AdMob inter load error: "+adError.getErrorMessage(), 3);
        }

        //@Override
        public void onAdLoaded(Ad ad) {
            toastMsg("AdMob inter loaded", 3);

        }

        //@Override
        public void onAdClicked(Ad ad) {

        }
    };

    public void toastMsg(String msg, int level){

        //String msg = context.getResources().getString(stringId);
        if (log > level)
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        Log.d(TAG, msg);
    }

    public boolean hasRemovedAds(){
        if (handlerIAP != null)
            return handlerIAP.mHasRemovedAds;
        return false;
    }

}
