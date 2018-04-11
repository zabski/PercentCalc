package com.zmobile.percentcalc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

//import com.facebook.FacebookSdk;
//import com.facebook.appevents.AppEventsLogger;
import com.amazon.device.ads.AdLayout;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdView;
import com.facebook.ads.NativeAdViewAttributes;
//import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.zmobile.ads.AdRequestBuilder;
import com.zmobile.ads.FacebookNativeAds;
import com.zmobile.foodendpoint.customerApi.model.OfyCustomer;

public class ActivityMenu extends ActivityTemplate implements Updatable {

	static final Uri APP_URI = Uri.parse("android-app://com.example.android.recipes/http/recipe-app.com/recipes");
	static final Uri WEB_URL = Uri.parse("http://recipe-app.com/recipes/");
	private GoogleApiClient mClient;

	String classes[] = {"ActivityDiscount", "ActivityUnitPrice", "ActivityLoanCalc", "ActivityRegular", "ActivityTip", "InfoActivity"};

	String menuImages[] = {"saving_round_164", "deposit_round_164", "pig_round_164", "procent_round_164", "gold_icon2", "info_128"};
	ArrayList<ListItem> menuItems;

	AdRequest adRequest;
	//static AdView adView;
	int ads, fb_ads;
	//HandlerPurchase handlerIAP;
	ListView mlist;

	//facebook native ads
	FacebookNativeAds fbNativeAds;
	//private LinearLayout nativeAdContainer;
	private NativeAdView.Type viewType = NativeAdView.Type.HEIGHT_100;
	private NativeAdViewAttributes adViewAttributes = new NativeAdViewAttributes();

	private TextView privacyText;
	//private AdView adView;
	private NativeAd nativeAd;
	String fb_adNative_id;

	// Color set
	private int backgroundColor;
	private int rowBackgroundColor;
	private int titleColor;
	private int linkColor;
	private int contentColor;
	private int borderColor;

	SharedPreferences settings;// = getApplicationContext().getSharedPreferences("Settings", 0);
	Boolean remarket;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;

	/*
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		String activityName = classes[position];
		super.onListItemClick(l, v, position, id);
		Class ourClass = null;
		try {
			ourClass = Class.forName("com.zmobile.percentcalc." + activityName);
			Intent ourIntent = new Intent(ActivityMenu.this, ourClass);
			startActivity(ourIntent);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}*/

	AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v,
								int position, long id) {
			String activityName = classes[position];
			//super.onListItemClick(l, v, position, id);
			//MobileCore.showOfferWall(this, null);
			Class ourClass = null;
			try {
				ourClass = Class.forName("com.zmobile.percentcalc." + activityName);
				Intent ourIntent = new Intent(ActivityMenu.this, ourClass);

				startActivity(ourIntent);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        TAG = this.getClass().getSimpleName();
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_menu);
		/*
		amazonAd = (AdLayout) findViewById(R.id.amazonAdView);
        nativeAdContainer = (FrameLayout) findViewById(R.id.native_ad_container_end);
        adViewExpress = (NativeExpressAdView) findViewById(R.id.adViewExpress);
        adView = (com.google.android.gms.ads.AdView) findViewById(R.id.adView);
		fbBannerContainer = (FrameLayout) findViewById(R.id.fb_banner_ad);*/
		setUpAdLayouts();
		super.onCreate(savedInstanceState);
		showFaceNative = false;

		TAG = ActivityMenu.class.getSimpleName();
		mlist = (ListView) findViewById(R.id.list);
		privacyText = (TextView) findViewById(R.id.privacyPolicy);
		privacyText.setClickable(true);
		privacyText.setMovementMethod(LinkMovementMethod.getInstance());
		String text = "<a href='https://docs.google.com/document/d/1KU_5kCrjkvKUBFU790Az9KCPuqTEUPAc9J739mziMi8'> Privacy Policy </a>";
		privacyText.setText(Html.fromHtml(text));

		//FacebookSdk.sdkInitialize(getApplicationContext());

		//AdRequestBuilder AdBuilder = new AdRequestBuilder();
		//AdRequestBuilder AdBuilder = AdRequestBuilder.getInstance();
		//adRequest = AdBuilder.build();
		//ads = getResources().getInteger(R.integer.ads);
		//fb_ads = getResources().getInteger(R.integer.fb_ads);
		//handlerIAP = HandlerPurchase.getInstance(this);

		String menuTitles[] = {getString(R.string.discount), getString(R.string.price_unit),
				getString(R.string.LoanCalc), getString(R.string.regular_percent),
				getString(R.string.tip), getString(R.string.info)};
		menuItems = new ArrayList<ListItem>();

		for (int i = 0; i < 5; i++) {
			menuItems.add(i, new ListItem(menuTitles[i], "", "@drawable/" + menuImages[i]));
			//[i].title = menuTitles[i];
			//menuItems[i].img = "@drawable/ic_launcher";
		}

		//full screen
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		//setListAdapter(new ArrayAdapter<String>(Menu.this, android.R.layout.simple_list_item_1, menuTitles));		

		//setListAdapter(new ImageArrayAdapter(ActivityMenu.this, android.R.layout.simple_list_item_1, menuItems));

		mlist.setAdapter(new ImageArrayAdapter(ActivityMenu.this, android.R.layout.simple_list_item_1, menuItems));//, R.layout.list_item));
		mlist.setOnItemClickListener(listener);


		//facebook native ads
		//nativeAdContainer = (FrameLayout) findViewById(R.id.native_ad_container_mid);
		//statusText = (TextView) findViewById(R.id.status);

		int cl_bkg = getResources().getColor(R.color.aaa);
		backgroundColor = Color.LTGRAY;
		rowBackgroundColor = Color.TRANSPARENT;
		titleColor = Color.WHITE;//Color.argb(0xff, 0x4e, 0x56, 0x65);
		linkColor = Color.WHITE;//Color.argb(0xff, 0x3b, 0x59, 0x98);
		contentColor = Color.WHITE;//;Color.argb(0xff, 0x4e, 0x56, 0x65);
		borderColor = Color.YELLOW;//Color.GRAY;

		//createAndLoadNativeAd();
		//fbNativeAds = new FacebookNativeAds();
		//fbNativeAds.createAndLoadNativeAd(this, nativeAdContainer, R.layout.ad_unit_no_img);

		settings = getApplicationContext().getSharedPreferences("Settings", 0);
		String googleMail = "zmobapp@gmail.com";
		boolean userSaved = settings.getBoolean("userDataSaved", false);
		//if (!userSaved)
		//	googleMail = saveCustomerData();
		remarket = settings.getBoolean("remarket", false);
		if (!remarket)
			RemarketHttpCall.get();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		//adView.pause();
		super.onPause();
		//AppEventsLogger.deactivateApp(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//adView.resume();
		//AppEventsLogger.activateApp(this);
	}

	public void onStart() {
		super.onStart();
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.connect();
		// The rest of your onStart() code.
		//EasyTracker.getInstance(this).activityStart(this);  // Add this method.
		/*
		if (ads == 0 || handlerIAP.mHasRemovedAds)
			adView.setVisibility(View.GONE);
		else
			adView.loadAd(adRequest);
		*/
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"Percentage", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://z-mobile-apps.blogspot.com/*"),
				// TODO: Make sure this auto-generated app deep link URI is correct.
				Uri.parse("android-app://com.zmobile.percentcalc/http/z-mobile-apps.blogspot.com/*")
		);
		AppIndex.AppIndexApi.start(client, viewAction);
	}

	@Override
	public void onStop() {
		super.onStop();
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"Percentage", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://z-mobile-apps.blogspot.com/*"),
				// TODO: Make sure this auto-generated app deep link URI is correct.
				Uri.parse("android-app://com.zmobile.percentcalc/http/z-mobile-apps.blogspot.com/*")
		);
		AppIndex.AppIndexApi.end(client, viewAction);
		// The rest of your onStop() code.
		//EasyTracker.getInstance(this).activityStop(this);  // Add this method.

		int sta = RemarketHttpCall.status;
		if (!remarket && sta == 200) {
			remarket = true;
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("remarket", true);
			editor.commit();
		}
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.disconnect();
	}

	String saveCustomerData() {

		long todayMillis = Calendar.getInstance().getTimeInMillis();
		long today = todayMillis / (1000 * 60 * 60 * 24);
		String possibleEmail = "";
		PackageManager pm = getPackageManager();
		int hasPerm = pm.checkPermission(android.Manifest.permission.GET_ACCOUNTS, getPackageName());
		if (hasPerm <= PackageManager.PERMISSION_GRANTED) {
			if (Build.VERSION.SDK_INT >= 23)
				requestPermissions(new String[]{android.Manifest.permission.GET_ACCOUNTS}, 0);


			Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
			Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
			//.get(context).getAccounts();
			for (Account account : accounts) {
				if (emailPattern.matcher(account.name).matches()) {
					possibleEmail = encodeEmail(account.name);
					OfyCustomer customer = new OfyCustomer();
					customer.setAddr(possibleEmail);
					customer.setAppName("PercentCalc");
					customer.setProducer(android.os.Build.MANUFACTURER);
					customer.setModel(android.os.Build.MODEL);
					customer.setDevice(android.os.Build.DEVICE);
					customer.setCountry(Locale.getDefault().getCountry());
					customer.setLang(Locale.getDefault().getLanguage());
					customer.setDate(today);
					new AddCustomerAsyncTask().execute(new Pair<Context, OfyCustomer>(this, customer));
				}
			}
		}
		return possibleEmail;
	}

	String encodeEmail(String email){
		String encoded = "";
		String encoded2 = "";
		for(int i=0; i<email.length(); i++){
			char c = email.charAt(i);
			char p = ++c;
			c--;
			char m = --c;
			encoded += p;
			encoded2 += m;
		}
		return encoded;
	}


/*
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater blowUp = getMenuInflater();
		blowUp.inflate(R.menu.cool_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		//return super.onOptionsItemSelected(item);
		switch(item.getItemId()){
		case R.id.aboutUs:
			Intent i = new Intent("com.example.tutorial1.ABOUT");
			startActivity(i);
			break;
		case R.id.preferences:
			Intent j = new Intent("com.example.tutorial1.PREFS");
			startActivity(j);
			break;
		case R.id.exit:
			finish();
			break;
		}
		return false;
	}
*/

}
