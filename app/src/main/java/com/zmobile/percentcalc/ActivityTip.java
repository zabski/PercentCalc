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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.device.ads.AdLayout;
import com.google.android.gms.ads.*;
import com.zmobile.ads.AdRequestBuilder;
//import com.google.analytics.tracking.android.EasyTracker;


public class ActivityTip extends ActivityTemplate implements OnClickListener, OnItemSelectedListener, OnFocusChangeListener,
	Updatable, OnKeyListener {
	
	Button bOblicz;
	Button bClear;
	Button bDel;
	View focusedView;
	EditText editTarget;
	EditText editKwota;
	EditText editOkres;
	EditText editTip;
	EditText editProcent;
	ArrayList<EditText> editList = new ArrayList<EditText>();// = {editWplata, editKwota, editFuture, editProcent};
	TextView textDeposit;
	TextView textZysk;	
	TextView tvDepPeriod;
	TextView tvSymbol1;
	TextView tvSymbol2;
	TextView tvSymbol3;
	Spinner ddownKapital;
	Spinner ddownOkres;
	DialogSymbol dialogSymbol;// = new DialogSymbol(this);
	AlertDialog dialog;
	
	LinearLayout layResult;
	
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
	
	  @Override
	  public void onStart() {
	    super.onStart();
	    // The rest of your onStart() code.
	    //EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	    //lib.adView = (AdView) findViewById(R.id.tip_adView);
		  /*
		  if (ads==0 || handlerIAP.mHasRemovedAds)
			  adView.setVisibility(View.GONE);
		  else
			  adView.loadAd(adRequest);
			  */
	    //editKwota.requestFocus();
	    focusedView = editKwota;
	    //Update();
		mSymbol = settings.getString("symbol", "$");
		tvSymbol1.setText(mSymbol);
		tvSymbol2.setText(mSymbol);
		tvSymbol3.setText(mSymbol);
	  }

	  @Override
	  public void onStop() {
	    super.onStop();
	    // The rest of your onStop() code.
		//settings = getApplicationContext().getSharedPreferences("Settings", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("symbol", mSymbol);
		// Commit the edits!
		editor.commit();
	    //EasyTracker.getInstance(this).activityStop(this);  // Add this method.
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
		setContentView(R.layout.activity_tip);
		/*
		amazonAd = (AdLayout) findViewById(R.id.amazonAdView);
		adView = (com.google.android.gms.ads.AdView) findViewById(R.id.adView);
		//adView = (AdView) findViewById(R.id.tip_adView);
		nativeAdContainer = (FrameLayout) findViewById(R.id.native_ad_container_end);
		adViewExpress = (NativeExpressAdView) findViewById(R.id.adViewExpress);*/
		setUpAdLayouts();
		super.onCreate(savedInstanceState);

		//layResult = (LinearLayout) findViewById(R.id.layResult);
		bOblicz = (Button) findViewById(R.id.bOblicz);
		editTarget = (EditText) findViewById(R.id.editTarget);
		editKwota = (EditText) findViewById(R.id.editKwota);
		editTip = (EditText) findViewById(R.id.editTip);		
		editProcent = (EditText) findViewById(R.id.editProcent);
		tvSymbol1 = (TextView) findViewById(R.id.tvSymbol1);
		tvSymbol2 = (TextView) findViewById(R.id.tvSymbol2);
		tvSymbol3 = (TextView) findViewById(R.id.tvSymbol3);
		bOblicz.setOnClickListener(this);
		bClear = (Button) findViewById(R.id.bClear);
		bClear.setOnClickListener(this);
		bDel = (Button) findViewById(R.id.bDel);
		bDel.setOnClickListener(this);
		//editList.add(editOkres);
		editList.add(editKwota);
		editList.add(editTarget);
		editList.add(editProcent);
		editList.add(editTip);
		for (EditText view : editList){
			   view.setOnFocusChangeListener(this);
			   //view.setOnKeyListener(this);
			   //view.addTextChangedListener(getTextWatcher);
			if (view!=editProcent && view!=editOkres)
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
			//editKwota.setSelected(true);
			editList.get(0).requestFocus();
	        if (imm != null) {
	            imm.showSoftInput(editKwota,0);
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
	String oneEmpty(String[] inputs){
		int emptyCount = 0;
		String emptyStr = null;
		int len = inputs.length;
		for(int i=0; i<len; i++){
			if (inputs[i].isEmpty()){
				emptyCount++;
				emptyStr = inputs[i];
			}
		}
		
		if (emptyCount > 1) return null;
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
		
		String kwotaStr = editKwota.getText().toString();
		String procentStr = editProcent.getText().toString();
		String tipStr = editTip.getText().toString();
		String targetStr = editTarget.getText().toString();
								
		Drawable bkgInput = getResources().getDrawable(R.drawable.input);
		for(EditText emptyField: editList){
			emptyField.setBackgroundDrawable(bkgInput);	
		}	
		/*
		boolean kwotaNull = kwotaStr.equals(""); 
		boolean procentNull =  procentStr.equals("");
		boolean targetNull = targetStr.equals("");
		*/
		EditText[] strs = {editKwota, editProcent, editTip, editTarget};
		//String oneEmpty = oneEmpty(strs);
		ArrayList<EditText> twoEmpty = twoEmpty(strs);
		
		if (twoEmpty.contains(editKwota)){
			String msg = getResources().getString(R.string.fill_in_bill);
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (twoEmpty.size() != 2){
			String msg = getResources().getString(R.string.leave_two_empty);
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
			return;
		}
		double result = 0;
		String resultStr = "";
		EditText resultField = null;
								
		double target = lib.strToDbl(targetStr,0);//Double.parseDouble(targetStr);
		double kwota = lib.strToDbl(kwotaStr,0);//Double.parseDouble(kwotaStr);
		double procent = lib.strToDbl(procentStr,0)/100;//Double.parseDouble(procentStr) / 100;
		double tip = lib.strToDbl(tipStr, 0);//Double.parseDouble(okresStr);
		
		if (twoEmpty.contains(editTip)){
			
			if (twoEmpty.contains(editTarget)){
				tip = kwota*procent;
				target = kwota+tip;
				//targetStr = String.format("%.2f ", target);
			}
			if (twoEmpty.contains(editProcent)){
				tip = target-kwota;
				procent = (tip/kwota);
				//procentStr = String.format("%.2f ", procent);
			}									
			
			result = target/procent;
			resultField = editKwota; 
			//editKwota.setText(resultStr);
		}else if (twoEmpty.contains(editProcent)){
			target = kwota+tip;
			procent = (tip/kwota);
		}
		/*
		if (oneEmpty.equals(procentStr)){
			result = (target/kwota)*100;
			resultStr = String.format("%.2f ", result);
			resultField = editProcent; 
		}
		if (oneEmpty.equals(targetStr)){
			result = kwota*procent;
			resultStr = String.format("%.2f ", result);
			resultField = editTarget; 
		}*/
		
		targetStr = lib.strFormat(target);//String.format("%.2f ", target);
		procentStr = lib.strFormat(procent*100);//String.format("%.2f ", procent*100);
		tipStr =  lib.strFormat(tip);//String.format("%.2f ", tip);
		
		editTarget.setText(targetStr);
		editTip.setText(tipStr);
		editProcent.setText(procentStr);
		
		Drawable bkg = getResources().getDrawable(R.drawable.edit_result);
		for(EditText emptyField: twoEmpty){
			emptyField.setBackgroundDrawable(bkg);	
		}								
		
		
		/*
		double q = 1 + procent / 12;
		double qn = Math.pow(q, okres);
		double rata = kwota * qn * (1 - q) / (1 - qn);
		double totalAmount = rata * okres;
		double podatek = 0.19;
		double suma = 0;
		double kapital = 0;
		*/			
		double principal = kwota; 
		
		//double paymentPerYear = wplata*12;
		//double paymentPerCompundPeriod = paymentPerYear/compoundsPerYear;
		
		
		/*
		for (int m = 1; m < okres*numMonthPeriod; m++){
			procentNetto = (Math.pow(1+(procent/(kapitalizacja*100)*(1-podatek/100)),kapitalizacja*okres)-1);
			suma = suma + wplata + suma*procentNetto;
			kapital = kapital + wplata;
		}
		
		double zysk = suma - kapital;		
		
		double numberOfYears = okres*timeUnitInYears;
		double i = procent/compoundsPerYear;
		double n = numberOfYears*compoundsPerYear;
		//double pmt = paymentPerCompundPeriod;
							
		//double futureValue = principal * Math.pow(1+i,n);		
		//futureValue+=pmt/i*(Math.pow(1+i, n)-1);		
		//double zysk = futureValue - principal - n*pmt;
		
		double FV = target;
		double PV = principal;
		
		double deposit = (FV*i-PV*i*Math.pow(1+i, n))/(Math.pow(1+i, n)-1);
				
		String currency = getString(R.string.currency);
		String pre_currency = getString(R.string.pre_currency);
		
		layResult.setVisibility(View.VISIBLE);
		
		if (mSymbol == null) mSymbol = " ";
		
		textDeposit.setText(pre_currency+String.format("%.2f ", deposit)+mSymbol);
		*/
		//textZysk.setText(pre_currency+String.format("%.2f", zysk)+currency);				
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
		tvSymbol1.setText(mSymbol);
		tvSymbol2.setText(mSymbol);	
		tvSymbol3.setText(mSymbol);
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

	public void setAdVisible(boolean show){
		if (show)
			adView.setVisibility(View.VISIBLE);
		else
			adView.setVisibility(View.GONE);
	}

}
