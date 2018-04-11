package com.zmobile.ads;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
//import com.zmobile.saveplan.ActivityTemplate;
import com.zmobile.mylibrary.R;

import static com.zmobile.ads.AdsGeneralHandler.ADMOB;
import static com.zmobile.ads.AdsGeneralHandler.FACE;

/**
 * Created by lukasz on 2016-02-02.
 */
public class FacebookNativeAds implements AdListener {

    protected static final String TAG = FacebookNativeAds.class.getSimpleName();
    Context context;
    //AdsGeneralHandler adsHandler;

    private FrameLayout nativeAdContainer;
    //private LinearLayout nativeAdContainer;
    /*
    private Button showNativeAdButton;
    private Button showNativeAdListButton;
    private Button showNativeAdHScrollButton;
    private Button showNativeAdTemplateButton;
    */
    private LinearLayout fb_adView;
    private NativeAd nativeAd;
    private AdChoicesView adChoicesView;
    public boolean loadError = false;
    int log;
    AdsGeneralHandler adsHandle;

    public void FacebookNativeAds(AdsGeneralHandler ads){
        this.adsHandle = ads;
    }

    public View createAndLoadNativeAd(Context ctx, FrameLayout nativeAdContainer, int templateId, String fbNativeId) {

        //nativeAdContainer = (LinearLayout) findViewById(R.id.native_ad_container);
        context = ctx;
        ///adsHandler = (AdsGeneralHandler)ctx;
        this.nativeAdContainer = nativeAdContainer;
        log = ctx.getResources().getInteger(R.integer.log);

        //String fbNativeId = ctx.getResources().getString(R.string.facebook_native_id);

        LayoutInflater inflater = LayoutInflater.from(ctx);
        try {
            fb_adView = (LinearLayout) inflater.inflate(templateId, nativeAdContainer, false);
        }catch (Exception e){
            String err = e.getMessage();
            Exception e2 = (Exception)e.getCause();
            if (log>3) Toast.makeText(context, "Fb native inflate Error: "+err, Toast.LENGTH_LONG).show();
            Log.e("Show Ads:", "Fb native inflate Error: "+err);
        }
        if (nativeAdContainer!=null && fb_adView!=null)
            nativeAdContainer.addView(fb_adView);
        else return null;

        //nativeAdStatus = (TextView) findViewById(R.id.native_ad_status);
        //showNativeAdButton = (Button) findViewById(R.id.load_native_ad_button);
		/*
		showNativeAdListButton = (Button) findViewById(R.id.load_native_ad_list_button);
		showNativeAdHScrollButton = (Button) findViewById(R.id.load_native_ad_hscroll);
		showNativeAdTemplateButton = (Button) findViewById(R.id.load_native_ad_template_button);
		*/
        nativeAd = new NativeAd(ctx, fbNativeId);

        // Set a listener to get notified when the ad was loaded.
        nativeAd.setAdListener(FacebookNativeAds.this);

        // When testing on a device, add its hashed ID to force test ads.
        // The hash ID is printed to log cat when running on a device and loading an ad.
        // AdSettings.addTestDevice("THE HASHED ID AS PRINTED TO LOG CAT");

        // Initiate a request to load an ad.
        nativeAd.loadAd(NativeAd.MediaCacheFlag.ALL);

        return fb_adView;
    }

    @Override
    public void onError(Ad ad, AdError error) {
        //nativeAdStatus.setText("Ad failed to load: " + error.getErrorMessage());

        if (log>3) Toast.makeText(context, "Fb native Ad Error: "+error.getErrorMessage(), Toast.LENGTH_LONG).show();
        loadError = true;
        //Log.d(TAG,error.getErrorMessage());
        Log.e(TAG+" Show Ads: ","Fb native Ad Error: "+error.getErrorMessage());

        //((ActivityTemplate) context).loadAdsOnError();
        //ActivityTemplate ctx = ((ActivityTemplate) context);
        if (nativeAdContainer!=null) nativeAdContainer.setVisibility(View.GONE);
        //ctx.onAdLoadError();
        //AdsGeneralHandler.this.showAds();
        //ctx.onFbNativeAdError();
        adsHandle.showAds();

    }

    @Override
    public void onAdClicked(Ad ad) {
        Toast.makeText(context, "Ad Clicked", Toast.LENGTH_SHORT).show();
    }

    public void onLoggingImpression(Ad ad) {}

    @Override
    public void onAdLoaded(Ad ad) {
        AdsGeneralHandler.lastBannerShown = FACE;
        if (log>3) Toast.makeText(context, "Fb native Ad Loaded!", Toast.LENGTH_LONG).show();
        Log.d(TAG+" Show Ads: ","Fb native Ad Loaded!");
        loadError = false;
        nativeAdContainer.setVisibility(View.VISIBLE);
        if (nativeAd == null || nativeAd != ad) {
            // Race condition, load() called again before last ad was displayed
            if (nativeAdContainer!=null) nativeAdContainer.setVisibility(View.VISIBLE);
            return;
        }

        // Unregister last ad
        nativeAd.unregisterView();

        //nativeAdStatus.setText("");

        // Using the AdChoicesView is optional, but your native ad unit should
        // be clearly delineated from the rest of your app content. See
        // https://developers.facebook.com/docs/audience-network/guidelines/native-ads#native
        // for details. We recommend using the AdChoicesView.
        if (adChoicesView == null) {
            adChoicesView = new AdChoicesView(context, nativeAd, true);
            fb_adView.addView(adChoicesView, 0);
        }

        inflateAd(nativeAd, fb_adView, context);

        // Registering a touch listener to log which ad component receives the touch event.
        // We always return false from onTouch so that we don't swallow the touch event (which
        // would prevent click events from reaching the NativeAd control).
        // The touch listener could be used to do animations.
        nativeAd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    /*
                    switch (view.getId()) {
                        case R.id.native_ad_call_to_action:
                            Log.d(TAG, "Call to action button clicked");
                            break;
                        case R.id.native_ad_media:
                            Log.d(TAG, "Main image clicked");
                            break;
                        default:
                            Log.d(TAG, "Other ad component clicked");
                    }*/
                }
                return false;
            }
        });
    }

    public static void inflateAd(NativeAd nativeAd, View adView, Context context) {
        // Create native UI using the ad metadata.
        ImageView nativeAdIcon = (ImageView) adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = (TextView) adView.findViewById(R.id.native_ad_title);
        TextView nativeAdBody = (TextView) adView.findViewById(R.id.native_ad_body);
        MediaView nativeAdMedia = (MediaView) adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext =
                (TextView) adView.findViewById(R.id.native_ad_social_context);
        Button nativeAdCallToAction = (Button) adView.findViewById(R.id.native_ad_call_to_action);

        // Setting the Text
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(View.VISIBLE);
        nativeAdTitle.setText(nativeAd.getAdTitle());
        nativeAdBody.setText(nativeAd.getAdBody());

        // Downloading and setting the ad icon.
        NativeAd.Image adIcon = nativeAd.getAdIcon();
        NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

        // Downloading and setting the cover image.
        NativeAd.Image adCoverImage = nativeAd.getAdCoverImage();
        int bannerWidth = adCoverImage.getWidth();
        int bannerHeight = adCoverImage.getHeight();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int mediaWidth = adView.getWidth() > 0 ? adView.getWidth() : metrics.widthPixels;
        nativeAdMedia.setLayoutParams(new LinearLayout.LayoutParams(
                mediaWidth,
                Math.min(
                        (int) (((double) mediaWidth / (double) bannerWidth) * bannerHeight),
                        metrics.heightPixels / 3)));
        nativeAdMedia.setNativeAd(nativeAd);

        // Wire up the View with the native ad, the whole nativeAdContainer will be clickable.
        nativeAd.registerViewForInteraction(adView);

        // You can replace the above call with the following call to specify the clickable areas.
        // nativeAd.registerViewForInteraction(adView,
        //     Arrays.asList(nativeAdCallToAction, nativeAdMedia));
    }

    public boolean isAdLoaded(){
        if (nativeAd!=null)
        return nativeAd.isAdLoaded();
        else return false;
    }

    //AdListener getAdListener = new AdListener {}

}
