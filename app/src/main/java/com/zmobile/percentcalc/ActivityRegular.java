package com.zmobile.percentcalc;

//import com.example.tutorial1.R;

//import com.zmobile.payplan.R;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
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

//import com.facebook.ads.AdView;
import com.amazon.device.ads.AdLayout;
import com.google.android.gms.ads.*; //InterstitialAd
import com.zmobile.ads.AdRequestBuilder;
//import com.google.analytics.tracking.android.EasyTracker;


public class ActivityRegular extends ActivityTemplate implements OnClickListener, OnItemSelectedListener, OnFocusChangeListener,
	Updatable {
	
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
	EditText editTarget;
	EditText editKwota;
	EditText editOkres;
	//EditText editKapital;
	EditText editProcent;
	ArrayList<EditText> editList = new ArrayList<EditText>();// = {editWplata, editKwota, editFuture, editProcent};
	TextView textDeposit;
	TextView textZysk;	
	TextView tvDepPeriod;
	TextView tvSymbol1;
	TextView tvSymbol2;
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
	private com.google.android.gms.ads.InterstitialAd interstitial;
	AdRequest adRequest;		
	MenuHelper menuHelp;
	//static AdView lib.adView;
	private com.facebook.ads.AdView faceAdView;
	PersonInfo lib;
	SharedPreferences settings;
	//int ads;
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
	  public void onStart() {
	    super.onStart();
	    // The rest of your onStart() code.
	    //EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	    //lib.adView = (AdView) findViewById(R.id.reg_adView);
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
		//faceAdView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		TAG = this.getClass().getSimpleName();
		setContentView(R.layout.activity_regular);

		setUpAdLayouts();
		super.onCreate(savedInstanceState);

		//layResult = (LinearLayout) findViewById(R.id.layResult);
		bOblicz = (Button) findViewById(R.id.bOblicz);
		editTarget = (EditText) findViewById(R.id.editTarget);
		editKwota = (EditText) findViewById(R.id.editKwota);
		//editOkres = (EditText) findViewById(R.id.editPeriod);
		//editKapital = (EditText) findViewById(R.id.editKapital);
		editProcent = (EditText) findViewById(R.id.editProcent);
		tvSymbol1 = (TextView) findViewById(R.id.tvSymbol1);
		tvSymbol2 = (TextView) findViewById(R.id.tvSymbol2);
		bOblicz.setOnClickListener(this);
		bClear = (Button) findViewById(R.id.bClear);
		bClear.setOnClickListener(this);
		bDel = (Button) findViewById(R.id.bDel);
		bDel.setOnClickListener(this);
		//editList.add(editOkres);
		editList.add(editKwota);
		editList.add(editTarget);
		editList.add(editProcent);
		
		for (EditText view : editList){
			   view.setOnFocusChangeListener(this);
			if (view!=editProcent && view!=editOkres)
				view.addTextChangedListener(new NumberTextWatcher(view));
		}	
		
		//lib = new PersonInfo(this);
		lib = PersonInfo.getInstance(this);
		settings = getApplicationContext().getSharedPreferences("Settings", 0);		

		//ddlistOkres.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listArray));
		/*
		// spinner capital
		ddownKapital = (Spinner) findViewById(R.id.ddownKapital);
		// Create an ArrayAdapter using the string array and a default spinner layout
		//ArrayAdapter<CharSequence> adapterKapital = ArrayAdapter.createFromResource(this,
		        //R.array.kapitalizacja, android.R.layout.simple_spinner_item);
		ArrayAdapter<CharSequence> adapterKapital = ArrayAdapter.createFromResource(this,
		        R.array.kapitalizacja, R.layout.spinner);
		// Specify the layout to use when the list of choices appears
		adapterKapital.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);		
		//adapterKapital.setDropDownViewResource(R.layout.spinner);
		// Apply the adapter to the spinner
		ddownKapital.setAdapter(adapterKapital);
		ddownKapital.setOnItemSelectedListener(this);
		ddownKapital.setSelection(2);
		
		// spinner period
		ddownOkres = (Spinner) findViewById(R.id.ddownOkres);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapterOkres = ArrayAdapter.createFromResource(this,
		        R.array.okres, R.layout.spinner);
		// Specify the layout to use when the list of choices appears
		adapterOkres.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		ddownOkres.setAdapter(adapterOkres);		
		ddownOkres.setOnItemSelectedListener(periodListner);
		ddownOkres.setSelection(1);			
				*/
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
	
	private void Calculate(){
		
		String kwotaStr = editKwota.getText().toString();
		String procentStr = editProcent.getText().toString();
		//String okresStr = editOkres.getText().toString();
		String targetStr = editTarget.getText().toString();		
		String msg = getResources().getString(R.string.fill_in_xor);
		
		Drawable bkgInput = getResources().getDrawable(R.drawable.input);
		for(EditText emptyField: editList){
			emptyField.setBackgroundDrawable(bkgInput);	
		}	
		/*
		boolean kwotaNull = kwotaStr.equals(""); 
		boolean procentNull =  procentStr.equals("");
		boolean targetNull = targetStr.equals("");
		*/
		String[] strs = {kwotaStr, procentStr, targetStr};
		String oneEmpty = oneEmpty(strs);
		
		if (oneEmpty == null){
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
			return;
		}
		double result = 0;
		String resultStr = "";
		EditText resultField = null;
								
		double target = lib.strToDbl(targetStr,0);//Double.parseDouble(targetStr);
		double kwota = lib.strToDbl(kwotaStr,0);//Double.parseDouble(kwotaStr);
		double procent = lib.strToDbl(procentStr,0)/100;//Double.parseDouble(procentStr) / 100;
		//double okres = lib.strToDbl(targetStr);//Double.parseDouble(okresStr);
		
		if (oneEmpty.equals(kwotaStr)){
			result = target/procent;
			//resultStr = String.format("%.2f ", result);
			resultField = editKwota; 
			//editKwota.setText(resultStr);
		}
		if (oneEmpty.equals(procentStr)){
			result = (target/kwota)*100;
			//resultStr = String.format("%.2f ", result);
			resultField = editProcent; 
		}
		if (oneEmpty.equals(targetStr)){
			result = kwota*procent;
			//resultStr = String.format("%.2f ", result);
			resultField = editTarget; 
		}	
		resultStr = lib.strFormat(result);
		resultField.setText(resultStr);
		Drawable bkg = getResources().getDrawable(R.drawable.edit_result);
		resultField.setBackgroundDrawable(bkg);
		
		
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


}
