/*
 * Copyright (c) 2016-present, Facebook, Inc. All rights reserved.
 *
 * You are hereby granted a non-exclusive, worldwide, royalty-free license to
 * use, copy, modify, and distribute this software in source code or binary
 * form for use in connection with the web services and APIs provided by
 * Facebook.
 *
 * As with any software that integrates with the Facebook platform, your use of
 * this software is subject to the Facebook Developer Principles and Policies
 * [http://developers.facebook.com/policy/]. This copyright notice shall be
 * included in all copies or substantial portions of the software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.zmobile.ads.facebook;

import android.os.Bundle;
import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.zmobile.percentcalc.R;

import java.util.ArrayList;
import java.util.List;

//import com.facebook.audiencenetwork.adssample.R;

public class FacebookNativeAdFragment extends Fragment {
    private NativeAd nativeAd;
    private LinearLayout nativeAdContainer;
    private LinearLayout adView;
    //int layout = R.layout.ad_unit_row;
    int layout = R.layout.native_ad_layout;
    //int layout = R.layout.ad_unit_grid;
    //int layout = R.layout.ad_unit;
    //int layout = R.layout.ad_unit_no_img;
    View ctxView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_native_ad, container, false);
        ctxView = v;
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showNativeAd();
    }

    private void showNativeAd() {
        String nativeId = getString(R.string.facebook_native_id);
        nativeAd = new NativeAd(getContext(), nativeId);
        nativeAd.setAdListener(new AdListener() {

            @Override
            public void onError(Ad ad, AdError error) {
                // Ad error callback
                int a = 1;
                int b = 2;

                String err = error.getErrorMessage();
                Log.d(this.getClass().getSimpleName(),"Facebook native load error: "+err);
                int c = a + b;
            }

            @Override
            public void onAdLoaded(Ad ad) {

                // Add the Ad view into the ad container.
                View v = getView();
                if (v == null) v = ctxView;
                if (v == null) return;
                try{
                    nativeAdContainer = (LinearLayout) v.findViewById(R.id.native_ad_container_end);
                }catch (Exception e){
                    String str = e.getMessage();
                }
                if (nativeAdContainer == null) return;
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                adView = (LinearLayout) inflater.inflate(layout,
                        nativeAdContainer, false);
                nativeAdContainer.addView(adView);

                // Create native UI using the ad metadata.
                ImageView nativeAdIcon = (ImageView) adView.findViewById(R.id.native_ad_icon);
                TextView nativeAdTitle = (TextView) adView.findViewById(R.id.native_ad_title);
                MediaView nativeAdMedia = (MediaView) adView.findViewById(R.id.native_ad_media);
                TextView nativeAdSocialContext = (TextView) adView.findViewById(R.id
                        .native_ad_social_context);
                TextView nativeAdBody = (TextView) adView.findViewById(R.id.native_ad_body);
                Button nativeAdCallToAction = (Button) adView.findViewById(R.id
                        .native_ad_call_to_action);

                // Set the Text.
                nativeAdTitle.setText(nativeAd.getAdTitle());
                nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
                nativeAdBody.setText(nativeAd.getAdBody());
                nativeAdCallToAction.setText(nativeAd.getAdCallToAction());

                // Download and display the ad icon.
                NativeAd.Image adIcon = nativeAd.getAdIcon();
                NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

                // Download and display the cover image.
                nativeAdMedia.setNativeAd(nativeAd);

                // Add the AdChoices icon
                LinearLayout adChoicesContainer = (LinearLayout) getView().findViewById(R.id
                        .ad_choices_container);
                AdChoicesView adChoicesView = new AdChoicesView(getContext(), nativeAd, true);
                adChoicesContainer.addView(adChoicesView);

                // Register the Title and CTA button to listen for clicks.
                List<View> clickableViews = new ArrayList<>();
                clickableViews.add(nativeAdTitle);
                clickableViews.add(nativeAdCallToAction);
                clickableViews.add(nativeAdBody);
                clickableViews.add(nativeAdIcon);
                clickableViews.add(nativeAdMedia);
                nativeAd.registerViewForInteraction(nativeAdContainer, clickableViews);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // On logging impression callback
            }
        });

        // Request an ad
        nativeAd.loadAd();
    }
}
