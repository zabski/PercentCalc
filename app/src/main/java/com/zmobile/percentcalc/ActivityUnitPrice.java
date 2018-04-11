package com.zmobile.percentcalc;

//import com.example.tutorial1.R;

//import com.zmobile.payplan.R;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.device.ads.AdLayout;
import com.google.android.gms.ads.*;
import com.zmobile.ads.AdRequestBuilder;
import com.zmobile.ads.AdsAmazonHandle;
//import com.google.analytics.tracking.android.EasyTracker;


public class ActivityUnitPrice extends ActivityTemplate implements OnClickListener, OnItemSelectedListener, OnFocusChangeListener,
	Updatable, OnKeyListener {
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		adView.pause();
		super.onPause();		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		adView.resume();
	}

	Button bOblicz;
	Button bClear;
	Button bDel;
	View focusedView;
	EditText editSmallUnits;
	EditText editSmallPrice;
	EditText editSmallUnitPrice;
	EditText editBigUnits;
	EditText editBigPrice;
	EditText editBigUnitPrice;		
	EditText editWouldCost;
	EditText editSavedAmount;
	
	ArrayList<EditText> editList = new ArrayList<EditText>();// = {editWplata, editKwota, editFuture, editProcent};
	ArrayList<EditText> nonEditList = new ArrayList<EditText>();
	ArrayList<TextView> symbols = new ArrayList<TextView>();
	TextView textDeposit;
	TextView textZysk;	
	TextView tvDepPeriod;
	TextView tvSymbol1;
	TextView tvSymbol2;
	TextView tvSymbol3;
	TextView tvSymbol4;
	TextView tvSymbol5;
	TextView tvSymbol6;
	TextView tvSymbol7;
	TextView tvSymbol8;
	
	Spinner ddownKapital;
	Spinner ddownOkres;
	DialogSymbol dialogSymbol;// = new DialogSymbol(this);
	AlertDialog dialog;
	
	//LinearLayout layResult;
	
	//double kapitalizacja;
	double compoundsPerYear = 1;
	double numMonthPeriod;
	double procentNetto;
	double okres;
	double timeUnitInYears = 1;
	public String listArray[] = { "Example1", "Example2", "Example3", "Example4", "Example5"};
	String mSymbol = " ";
	Spinner ddSymbol;
	//SymbolListner symbolListner = new SymbolListner();
	Listner periodListner = new Listner();
	private InterstitialAd interstitial;	
	AdRequest adRequest;		
	//static AdView adView;
	PersonInfo lib;
	SharedPreferences settings;
	MenuHelper menuHelp;
	int ads;
	//HandlerPurchase handlerIAP;
	//com.amazon.device.ads.AdLayout amazonAd;
	//AdsAmazonHandle adsAmazon;
	
	class Listner implements OnItemSelectedListener  {
		

		@Override
		public void onItemSelected(AdapterView<?> arg0, View view, int pos,
				long arg3) {
			// TODO Auto-generated method stub
			switch(pos){
			case 0:
				numMonthPeriod = 1;
				timeUnitInYears = 1.0/12.0;
				break;
			case 1:
				numMonthPeriod = 12;
				timeUnitInYears = 1;
				break;		
			}	
			Calculate();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
  @Override
  public void onStart() {
	    super.onStart();
	    // The rest of your onStart() code.
	    //EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	    //adView = (AdView) findViewById(R.id.unit_adView);

	    //editKwota.requestFocus();
	    focusedView = editSmallPrice;
	    //Update();
		mSymbol = settings.getString("symbol", "$");
		for(TextView tv: symbols) tv.setText(mSymbol);
		/*tvSymbol1.setText(mSymbol);
		tvSymbol2.setText(mSymbol);
		tvSymbol3.setText(mSymbol);
		//tvSymbol4.setText(mSymbol);
		tvSymbol5.setText(mSymbol);
		tvSymbol6.setText(mSymbol);
		tvSymbol7.setText(mSymbol);
		tvSymbol8.setText(mSymbol);
		*/
  }

	  @Override
	  public void onStop() {
	    super.onStop();
	    // The rest of your onStop() code.
	    //EasyTracker.getInstance(this).activityStop(this);  // Add this method.
		//settings = getApplicationContext().getSharedPreferences("Settings", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("symbol", mSymbol);
		// Commit the edits!
		editor.commit();
		//removeAdView();
	  }
	  
  	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
	  	//removeAdView();
		menuHelp.dispose();
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		TAG = this.getClass().getSimpleName();
		setContentView(R.layout.activity_unit_price);
		/*
		amazonAd = (AdLayout) findViewById(R.id.amazonAdView);
		adView = (com.google.android.gms.ads.AdView) findViewById(R.id.adView);
		//adView = (AdView) findViewById(R.id.unit_adView);
		nativeAdContainer = (FrameLayout) findViewById(R.id.native_ad_container_end);
		adViewExpress = (NativeExpressAdView) findViewById(R.id.adViewExpress);
		*/
		setUpAdLayouts();
		super.onCreate(savedInstanceState);

		//layResult = (LinearLayout) findViewById(R.id.layResult);
		bOblicz = (Button) findViewById(R.id.bOblicz);
		editSmallUnits = (EditText) findViewById(R.id.editSmallUnits);
		editSmallPrice = (EditText) findViewById(R.id.editSmallPrice);
		editSmallUnitPrice = (EditText) findViewById(R.id.editSmallUnitPrice);
		editBigUnits = (EditText) findViewById(R.id.editBigUnits);
		editBigPrice = (EditText) findViewById(R.id.editBigPrice);
		editBigUnitPrice = (EditText) findViewById(R.id.editBigUnitPrice);		
		editWouldCost = (EditText) findViewById(R.id.editWouldCost);
		editSavedAmount = (EditText) findViewById(R.id.editSavedAmount);
		
		tvSymbol1 = (TextView) findViewById(R.id.tvSymbol1);
		tvSymbol2 = (TextView) findViewById(R.id.tvSymbol2);
		tvSymbol3 = (TextView) findViewById(R.id.tvSymbol3);
		tvSymbol4 = (TextView) findViewById(R.id.tvSymbol4);
		tvSymbol5 = (TextView) findViewById(R.id.tvSymbol5);
		tvSymbol6 = (TextView) findViewById(R.id.tvSymbol6);
		symbols.add(tvSymbol1);
		symbols.add(tvSymbol2);
		symbols.add(tvSymbol3);
		symbols.add(tvSymbol4);
		symbols.add(tvSymbol5);
		symbols.add(tvSymbol6);
		//tvSymbol7 = (TextView) findViewById(R.id.tvSymbol7);
		//tvSymbol8 = (TextView) findViewById(R.id.tvSymbol8);
		
		bOblicz.setOnClickListener(this);
		bClear = (Button) findViewById(R.id.bClear);
		bClear.setOnClickListener(this);
		bDel = (Button) findViewById(R.id.bDel);
		bDel.setOnClickListener(this);
		//editList.add(editOkres);
		editList.add(editSmallUnits);
		editList.add(editSmallPrice);			
		
		editList.add(editBigUnits);	
		editList.add(editBigPrice);			
				
		nonEditList.add(editBigUnitPrice);
		nonEditList.add(editSmallUnitPrice);
		nonEditList.add(editWouldCost);
		nonEditList.add(editSavedAmount);
		
		for (EditText view : editList){
			   view.setOnFocusChangeListener(this);
			   //view.setOnKeyListener(this);
			   //view.addTextChangedListener(getTextWatcher);
			//if (view!=editProcent && view!=editOkres)
				view.addTextChangedListener(new NumberTextWatcher(view));
		}	
		
		//lib = new PersonInfo(this);
		lib = PersonInfo.getInstance(this);
		settings = getApplicationContext().getSharedPreferences("Settings", 0);		

		dialogSymbol = DialogSymbol.getInstance(this);
		//dialogSymbol = new DialogSymbol(this);
		//menuHelp = new MenuHelper(this);
		menuHelp = MenuHelper.getInstance(this);
		//fbNativeAds.createAndLoadNativeAd(this, nativeAdContainer, R.layout.ad_unit);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		Drawable bkg = getResources().getDrawable(R.drawable.input);
		if (arg0.getId() == R.id.bDel){
			if (focusedView == null){
				focusedView = this.getCurrentFocus();
			}
			((EditText)focusedView).setText("");
			((EditText)focusedView).setBackgroundDrawable(bkg);
	        if (imm != null) {
	            imm.showSoftInput(focusedView,0);
	        }
			for (EditText view : nonEditList){
				   view.setText("");					   
				   //view.setBackground(bkg);				   
				   //view.setBackgroundDrawable(bkg);
			}
	        //layResult.setVisibility(View.GONE);
		}	
		if (arg0.getId() == R.id.bClear){
			/*editKwota.setText("");
			editProcent.setText("");
			editTarget.setText("");
			editOkres.setText("");*/
			for (EditText view : editList){
				   view.setText("");					   
				   //view.setBackground(bkg);				   
				   view.setBackgroundDrawable(bkg);
			}
			for (EditText view : nonEditList){
				   view.setText("");					   
				   //view.setBackground(bkg);				   
				   //view.setBackgroundDrawable(bkg);
			}			
			//editKwota.setSelected(true);
			editList.get(0).requestFocus();
	        if (imm != null) {
	            imm.showSoftInput(editSmallPrice,0);
	        }
	        //layResult.setVisibility(View.GONE);
		}		
		if (arg0.getId() == R.id.bOblicz){

			Calculate();
		}
		
	}
	/*
	boolean twoOfThree(String str1, String str2, String str 3){
		int emptyCount = 0;
		boolean isNull1 = str1.equals(""); 
		boolean isNull2 =  str2.equals("");
		boolean isNull3 = str3.equals("");
		
		
		if emptyCount 
	}*/
	
	// gets array of strings, returns one empty string, if more or none is empty, returns null
	ArrayList<EditText> oneEmpty(EditText[] inputs){
		int emptyCount = 0;
		//String emptyStr = null;
		ArrayList<EditText> emptyStr = new ArrayList<EditText>();
		int len = inputs.length;
		for(int i=0; i<len; i++){
			String str = inputs[i].getText().toString(); 
			if (str.isEmpty()){
				emptyCount++;
				emptyStr.add(inputs[i]);
			}
		}
		
		//if (emptyCount > 1) return null;		
		return emptyStr;
	}
	
	// gets array of strings, returns one empty string, if more or none is empty, returns null
	ArrayList<EditText> twoEmpty(EditText[] inputs){
		int emptyCount = 0;
		//String emptyStr = null;
		ArrayList<EditText> emptyStr = new ArrayList<EditText>();
		int len = inputs.length;
		for(int i=0; i<len; i++){
			String str = inputs[i].getText().toString(); 
			if (str.isEmpty()){
				emptyCount++;
				emptyStr.add(inputs[i]);
			}
		}		
		//if (emptyCount > 2) return null;
		return emptyStr;
	}	
	
	private void Calculate(){
		
		String smallPriceStr = editSmallPrice.getText().toString();		
		String smallUnitPriceStr = editSmallUnitPrice.getText().toString();
		String smallUnitsStr = editSmallUnits.getText().toString();
		String bigPriceStr = editBigPrice.getText().toString();		
		String bigUnitPriceStr = editBigUnitPrice.getText().toString();
		String bigUnitsStr = editBigUnits.getText().toString();
		
		String wouldCostStr = editWouldCost.getText().toString();
		String savedAmountStr = editSavedAmount.getText().toString();
		
		Drawable bkgInput = getResources().getDrawable(R.drawable.input);
		for(EditText emptyField: editList){
			emptyField.setBackgroundDrawable(bkgInput);	
		}	
								
		/*
		boolean kwotaNull = kwotaStr.equals(""); 
		boolean procentNull =  procentStr.equals("");
		boolean targetNull = targetStr.equals("");
		*/
		EditText[] smallStrs = {editSmallPrice, editSmallUnits};//, editSmallUnitPrice
		EditText[] bigStrs = {editBigPrice, editBigUnits};//, editBigUnitPrice
		//String oneEmpty = oneEmpty(strs);
		ArrayList<EditText> smallEmpty = twoEmpty(smallStrs);
		ArrayList<EditText> bigEmpty = twoEmpty(bigStrs);
		/*
		if (twoEmpty.contains(editAfter)){
			String msg = getResources().getString(R.string.fill_in_bill);
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
			return;
		}*/
		
		if (smallEmpty.size() > 0 || bigEmpty.size() > 0){
			//String msg = getResources().getString(R.string.leave_one_empty);
			String msg = getResources().getString(R.string.fill_all_non_framed);
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
			return;
		}
		double result = 0;
		String resultStr = "";
		EditText resultField = null;
								
		double smallUnits = lib.strToDbl(smallUnitsStr,0);//Double.parseDouble(targetStr);
		double smallPrice = lib.strToDbl(smallPriceStr,0);//Double.parseDouble(kwotaStr);		
		double smallUnitPrice = lib.strToDbl(smallUnitPriceStr, 0);//Double.parseDouble(okresStr);
		double bigUnits = lib.strToDbl(bigUnitsStr,0);//Double.parseDouble(targetStr);
		double bigPrice = lib.strToDbl(bigPriceStr,0);//Double.parseDouble(kwotaStr);		
		double bigUnitPrice = lib.strToDbl(bigUnitPriceStr, 0);//Double.parseDouble(okresStr);		
		
		double savedAmount = lib.strToDbl(savedAmountStr,0);
		double wouldCost = lib.strToDbl(wouldCostStr,0);
		/*
		if (smallEmpty.contains(editSmallUnits)){
			smallUnits = smallPrice/smallUnitPrice;
			smallUnitsStr = lib.strFormat(smallUnits);
			editSmallUnits.setText(smallUnitsStr);
		}
		if (smallEmpty.contains(editSmallPrice)){
			smallPrice = smallUnits*smallUnitPrice;
			smallPriceStr = lib.strFormat(smallPrice);
			editSmallPrice.setText(smallPriceStr);
		}*/	
		//if (smallEmpty.contains(editSmallUnitPrice)){
			smallUnitPrice = smallPrice/smallUnits;
			smallUnitPriceStr =  lib.strFormat(smallUnitPrice);
			editSmallUnitPrice.setText(smallUnitPriceStr);
		//}
		/*
		if (bigEmpty.contains(editBigPrice)){
			bigPrice = bigUnits*bigUnitPrice;
			bigPriceStr = lib.strFormat(bigPrice);
			editBigPrice.setText(bigPriceStr);
		}
		if (bigEmpty.contains(editBigUnits)){
			bigUnits = bigPrice/bigUnitPrice;
			bigUnitsStr = lib.strFormat(bigUnits);
			editBigUnits.setText(bigUnitsStr);
		}*/
		//if (bigEmpty.contains(editBigUnitPrice)){
			bigUnitPrice = bigPrice/bigUnits;
			bigUnitPriceStr =  lib.strFormat(bigUnitPrice);
			editBigUnitPrice.setText(bigUnitPriceStr);
		//}
		
		wouldCost = smallUnitPrice*bigUnits;
		savedAmount = wouldCost-bigPrice;																
		
		savedAmountStr = lib.strFormat(savedAmount);
		wouldCostStr = lib.strFormat(wouldCost);										
		
		editWouldCost.setText(wouldCostStr);
		editSavedAmount.setText(savedAmountStr);
		
		Drawable bkg = getResources().getDrawable(R.drawable.edit_result);
		for(EditText emptyField: smallEmpty){
			emptyField.setBackgroundDrawable(bkg);	
		}
		for(EditText emptyField: bigEmpty){
			emptyField.setBackgroundDrawable(bkg);	
		}			
		
		editWouldCost.setBackgroundDrawable(bkg);
		editSavedAmount.setBackgroundDrawable(bkg);
						
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		// TODO Auto-generated method stub
		/*
		switch (view.getId()) {
		case R.id.ddownKapital:*/
			switch(pos){
			case 0:
				compoundsPerYear = 365;
				break;
			case 1:
				compoundsPerYear = 52;
				break;
			case 2:
				compoundsPerYear = 12;
				break;
			case 3:
				compoundsPerYear = 4;
				break;
			case 4:
				compoundsPerYear = 2;
				break;				
			case 5:
				compoundsPerYear = 1;
				break;				
			}				
			/*
			break;
		case R.id.ddownOkres:
			switch(pos){
			case 1:
				numMonthPeriod = 1;
				timeUnitInYears = 1/12;
				break;
			case 2:
				numMonthPeriod = 12;
				timeUnitInYears = 1;
				break;		
			}				
			break;
		}*/
		Calculate();			
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
    public void onFocusChange(View v, boolean hasFocus){
        if(hasFocus){
          focusedView = v;
          Drawable bkg = getResources().getDrawable(R.drawable.input);
          focusedView.setBackgroundDrawable(bkg);
        } else {
            focusedView  = null;
        }
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		
		menuHelp.handleOnItemSelected(this, item);
		return true;

    }
	
	@Override
	public void Update(){
		mSymbol = dialogSymbol.getSymbol();
		for(TextView tv: symbols) tv.setText(mSymbol);
		/*tvSymbol1.setText(mSymbol);
		tvSymbol2.setText(mSymbol);		
		tvSymbol3.setText(mSymbol);
		//tvSymbol4.setText(mSymbol);
		tvSymbol5.setText(mSymbol);
		tvSymbol6.setText(mSymbol);
		tvSymbol7.setText(mSymbol);
		tvSymbol8.setText(mSymbol);*/
		settings = getApplicationContext().getSharedPreferences("Settings", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("symbol", mSymbol);
		// Commit the edits!
		editor.commit();
	}	
	/*
	class SymbolListner implements OnItemSelectedListener  {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View view, int pos, long arg3) {
			
			//mSymbol = ddSymbol.getItemAtPosition(pos).toString();
			tvSymbol1.setText(mSymbol);
			tvSymbol2.setText(mSymbol);
			// TODO Auto-generated method stub
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub			
		}		
	}
	*/

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Drawable bkg = getResources().getDrawable(R.drawable.input);
		((EditText)v).setBackgroundDrawable(bkg);
		return false;
	}
	
	TextWatcher getTextWatcher = new TextWatcher(){
		
		
	    public void afterTextChanged(Editable s) 
	    {
	    	String text = s.toString();
			// TODO Auto-generated method stub
			//FilterFavorites(text); 	    	
	    }
	    public void beforeTextChanged(CharSequence s, int start, int count, int after) 
	    {
	        /*This method is called to notify you that, within s, the count characters beginning at start are about to be replaced by new text with length after. It is an error to attempt to make changes to s from this callback.*/ 
	    }
	    public void onTextChanged(CharSequence s, int start, int before, int count) 
	    {
	    }
	};


}
