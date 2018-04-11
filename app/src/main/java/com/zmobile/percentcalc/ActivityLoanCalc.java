package com.zmobile.percentcalc;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.device.ads.AdLayout;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.NativeExpressAdView;
import com.zmobile.ads.AdRequestBuilder;

//import com.google.analytics.tracking.android.EasyTracker;
//import com.google.android.gms.ads.*;
//import com.zmobile.saveplan.DepositCalc.SymbolListner;


public class ActivityLoanCalc extends ActivityTemplate implements OnClickListener, OnFocusChangeListener,
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

	Button button1;
	Button bClear;
	Button bDel;
	View focusedView;
	EditText editKwota, editProcent, editAPR, editTotalToRepay;
	EditText editOkres;	
	EditText editComm;
	EditText editFees;
	EditText editPayAmount;
	EditText editTotalCost;
	EditText editCostPercent;
	ArrayList<EditText> editList = new ArrayList<EditText>();// = {editWplata, editKwota, editFuture, editProcent};
	ArrayList<EditText> resultList = new ArrayList<EditText>();
	ArrayList<TextView> symbols = new ArrayList<TextView>();
	String mSymbol = " ";
	Spinner ddSymbol;
	//SymbolListner symbolListner = new SymbolListner();
	TextView tvPayAmount;
	TextView tvTotalCost;
	TextView tvAPR;
	TextView tvCostPercent;
	TextView tvSymbol1, tvSymbol2, tvSymbol3, tvSymbol4, tvSymbol5, tvSymbol6;		
	//LinearLayout layResult;
	DialogSymbol dialogSymbol;
	AlertDialog dialog;
	//private InterstitialAd interstitial;
	AdRequest adRequest;
	//static lib.adView lib.adView;
	PersonInfo lib;
	SharedPreferences settings;
	MenuHelper menuHelp;
	int ads;
	//HandlerPurchase handlerIAP;
	
	//View MainView = (View) findViewById(R.layout.activity_main);
    int clBackground = Color.rgb(155, 155, 0);
    int clInputField = Color.rgb(255, 255, 255);
    
    public void onStart() {
	    super.onStart();
	    // The rest of your onStart() code.
	    //EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	    //lib.adView = (AdView) findViewById(R.id.loan_adView);
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
	    for(TextView tv: symbols) tv.setText(mSymbol);	
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
		setContentView(R.layout.activity_loan_cost);
		/*
		amazonAd = (AdLayout) findViewById(R.id.amazonAdView);
		//adView = (AdView) findViewById(R.id.loan_adView);
		adView = (com.google.android.gms.ads.AdView) findViewById(R.id.adView);
		nativeAdContainer = (FrameLayout) findViewById(R.id.native_ad_container_end);
		adViewExpress = (NativeExpressAdView) findViewById(R.id.adViewExpress);
		fbBannerContainer = (FrameLayout) findViewById(R.id.fb_banner_ad);
		*/
		setUpAdLayouts();
		super.onCreate(savedInstanceState);
		//AdRequestBuilder AdBuilder = new AdRequestBuilder();	
		AdRequestBuilder AdBuilder = AdRequestBuilder.getInstance();
		adRequest = AdBuilder.build();

		//layResult = (LinearLayout) findViewById(R.id.layResult);
		button1 = (Button) findViewById(R.id.button1);
		
		editKwota = (EditText) findViewById(R.id.editLoanAmount);
		editProcent = (EditText) findViewById(R.id.editPercent);		
		editOkres = (EditText) findViewById(R.id.editNumPayments);
		editComm = (EditText) findViewById(R.id.editCommission);
		editFees = (EditText) findViewById(R.id.editOtherFees);
		editPayAmount = (EditText) findViewById(R.id.editPayAmount);
		editTotalToRepay = (EditText) findViewById(R.id.editTotalToRepay);
		editTotalCost = (EditText) findViewById(R.id.editTotalCost);
		editCostPercent = (EditText) findViewById(R.id.editCostPercent);		
		editAPR = (EditText) findViewById(R.id.editAPR);
		tvAPR = (TextView) findViewById(R.id.tvAPR);
		tvPayAmount = (TextView) findViewById(R.id.tvPayAmount);
		tvTotalCost = (TextView) findViewById(R.id.tvTotalCost);
		tvCostPercent = (TextView) findViewById(R.id.tvCostPercent);
		tvSymbol1 = (TextView) findViewById(R.id.tvSymbol1);
		tvSymbol2 = (TextView) findViewById(R.id.tvSymbol2);
		tvSymbol3 = (TextView) findViewById(R.id.tvSymbol3);
		tvSymbol4 = (TextView) findViewById(R.id.tvSymbol4);
		tvSymbol5 = (TextView) findViewById(R.id.tvSymbol5);
		//tvSymbol6 = (TextView) findViewById(R.id.tvSymbol6);
		symbols.add(tvSymbol1);
		symbols.add(tvSymbol2);
		symbols.add(tvSymbol3);
		symbols.add(tvSymbol4);
		symbols.add(tvSymbol5);
		//symbols.add(tvSymbol6);
		bClear = (Button) findViewById(R.id.bClear);
		bClear.setOnClickListener(this);
		bDel = (Button) findViewById(R.id.bDel);
		bDel.setOnClickListener(this);
		editList.add(editOkres);
		editList.add(editKwota);		
		editList.add(editProcent);
		editList.add(editComm);
		editList.add(editFees);
		editList.add(editPayAmount);
		resultList.add(editTotalToRepay);
		resultList.add(editTotalCost);
		resultList.add(editCostPercent);
		resultList.add(editAPR);
		
		for (EditText view : editList){
			   view.setOnFocusChangeListener(this);
			if (view!=editProcent && view!=editOkres && view!=editComm)
				view.addTextChangedListener(new NumberTextWatcher(view));
		}		
		
		/*
		editText1.setBackgroundColor(clInputField);
		editText2.setBackgroundColor(clInputField);
		editText3.setBackgroundColor(clInputField);		
		MainView.setBackgroundColor(clBackground);
		*/
		button1.setOnClickListener(this);
		
		settings = getApplicationContext().getSharedPreferences("Settings", 0);		
		
		//lib = new PersonInfo(this);
		lib = PersonInfo.getInstance(this);
		
		/*
		// spinner symbol
		ddSymbol = (Spinner) findViewById(R.id.ddSymbol);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapterSymbol = ArrayAdapter.createFromResource(this,
		        R.array.symbols, R.layout.spinner);
		// Specify the layout to use when the list of choices appears
		adapterSymbol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		ddSymbol.setAdapter(adapterSymbol);		
		ddSymbol.setOnItemSelectedListener(symbolListner);
		ddSymbol.setSelection(1);		
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
		if (arg0.getId() == R.id.bDel && focusedView != null){
			((EditText)focusedView).setText("");
	        if (imm != null) {
	            imm.showSoftInput(focusedView,0);
	        }
	        ((EditText)focusedView).setBackgroundDrawable(bkg);
	        //layResult.setVisibility(View.GONE);
		}		
		if (arg0.getId() == R.id.bClear){
			//editKwota.setText("");
			//editProcent.setText("");			
			//editOkres.setText("");
			//editKwota.setSelected(true);
			for(EditText edit: editList){
				edit.setText("");
				edit.setBackgroundDrawable(bkg);
			}
			for(EditText result: resultList){
				result.setText("");
			}			
			editKwota.requestFocus();
	        if (imm != null) {
	            imm.showSoftInput(editKwota,0);
	        }	
	        //layResult.setVisibility(View.GONE);
		}	
		
		if (arg0.getId() == R.id.button1){
			Calculate();
		}
	}
	
	private void Calculate(){
		//String currency = getString(R.string.currency);
		//String pre_currency = getString(R.string.pre_currency);
		// compulsory
		String kwotaStr = editKwota.getText().toString();
		String okresStr = editOkres.getText().toString();
		// optional
		String commStr = editComm.getText().toString();
		String feesStr = editFees.getText().toString();
		// exactly one must be empty
		String procentStr = editProcent.getText().toString();				
		String paymentStr = editPayAmount.getText().toString();
		// results
		String totalCostStr = editTotalCost.getText().toString();
		String costPercentStr = editOkres.getText().toString();
		String totalToPayStr = editOkres.getText().toString();								
		
		if (kwotaStr.equals("") || okresStr.equals("") ){
			String msg = getResources().getString(R.string.fill_in_compulsory);
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
			return;
		}
		if (!(procentStr.equals("") ^ paymentStr.equals(""))){
			String msg = getResources().getString(R.string.leave_one_of_two);
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (paymentStr.equals("")){
			Drawable bkg = getResources().getDrawable(R.drawable.edit_result);
			editPayAmount.setBackgroundDrawable(bkg);
		}
		
		//layResult.setVisibility(View.VISIBLE);
		double price = lib.strToDbl(kwotaStr,0);//Double.parseDouble(kwotaStr);
		double commPercent = lib.strToDbl(commStr, 0)/100;//Double.parseDouble(commStr) / 100;
		double commision = price*commPercent;
		double fees = lib.strToDbl(feesStr,0);
		double kwota = price+commision+fees;//Double.parseDouble(kwotaStr);
		double procent = lib.strToDbl(procentStr,0.00001)/100;//Double.parseDouble(procentStr) / 100;
		double payment = lib.strToDbl(paymentStr,100);//Double.parseDouble(paymentStr);
		double okres = lib.strToDbl(okresStr,12);//Double.parseDouble(okresStr);
		double rata = payment;
		double APR = 0;
		if (!procentStr.equals("")){
			if (procent == 0) procent = 0.00001;
			double q = 1 + procent / 12;
			double qn = Math.pow(q, okres);
			rata = kwota * qn * (1 - q) / (1 - qn);
			//editPayAmount.setText(pre_currency+String.format("%.2f ", rata)+mSymbol);
		}
		double totalRepay = rata * okres;
		double totalCost = totalRepay - price;
		double cost_percent = 100*totalCost/price;
		//if (!paymentStr.equals("")){
			APR = CalculateAPR(okres, totalRepay, price)*100;
			//editProcent.setText(pre_currency+String.format("%.2f ", procent)+"%");
			tvAPR.setText(String.format("%.2f", APR));
			editAPR.setText(String.format("%.2f", APR));
		//}

		if (mSymbol == null) mSymbol = "";
		
		editTotalToRepay.setText(String.format("%.2f", totalRepay));
		editPayAmount.setText(String.format("%.2f", rata));
		editTotalCost.setText(String.format("%.2f", totalCost));
		editCostPercent.setText(String.format("%.2f", cost_percent));
		
		tvPayAmount.setText(String.format("%.2f", rata)+mSymbol);
		tvTotalCost.setText(String.format("%.2f", totalCost)+mSymbol);
		tvCostPercent.setText(String.format("%.2f", cost_percent));
	}
	
	double CalculateAPR(double periods, double repayTotal, double loanAmount){
		double result = 10000.0;
		double singlePayment = repayTotal/periods;
		for (double i=0; i<10000; i+=0.0005){
			double rightSide = 0;
			for (int k=1; k<=periods; k+=1){
				double dk = (double)k; 
				double base = 1+i;
				double mianownik = Math.pow(base,(double)(dk)/12);
				rightSide += singlePayment/mianownik;
			}
			if (rightSide - loanAmount < 0.01){
				return i;				
			}else if (rightSide - loanAmount < 0){
				result = i;
				break;
			}
		}			
		return result;
	}
	
	@Override
    public void onFocusChange(View v, boolean hasFocus){
          if(hasFocus){
          focusedView = v;
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
		//tvSymbol1.setText(mSymbol);
		//tvSymbol2.setText(mSymbol);
		//tvSymbol3.setText(mSymbol);
		for(TextView tv: symbols) tv.setText(mSymbol);	
		
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
			
			mSymbol = ddSymbol.getItemAtPosition(pos).toString();
			tvSymbol1.setText(mSymbol);
			//tvSymbol2.setText(mSymbol);
			// TODO Auto-generated method stub
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub			
		}		
	}	
	*/

	public void setAdVisible(boolean show){
		if (show)
			adView.setVisibility(View.VISIBLE);
		else
			adView.setVisibility(View.GONE);
	}
}