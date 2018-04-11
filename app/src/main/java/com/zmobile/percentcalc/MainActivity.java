package com.zmobile.percentcalc;

//import com.example.tutorial1.R;

//import com.zmobile.payplan.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
//import com.google.analytics.tracking.android.EasyTracker;

public class MainActivity extends Activity implements OnClickListener, OnItemSelectedListener  {
	
	Button bOblicz;
	EditText editWplata;
	EditText editKwota;
	EditText editOkres;
	//EditText editKapital;
	EditText editProcent;
	TextView textKwota;
	TextView textZysk;	
	Spinner ddownKapital;
	Spinner ddownOkres;
	double kapitalizacja;
	double compoundsPerYear = 1;
	double numMonthPeriod;
	double procentNetto;
	double okres;
	double timeUnitInYears = 1;
	public String listArray[] = { "Example1", "Example2", "Example3", "Example4", "Example5"};
	Listner periodListner = new Listner();
	
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
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.saving_calc);
		bOblicz = (Button) findViewById(R.id.bOblicz);
		editWplata = (EditText) findViewById(R.id.editTarget);
		editKwota = (EditText) findViewById(R.id.editKwota);
		//editOkres = (EditText) findViewById(R.id.editPeriod);
		//editKapital = (EditText) findViewById(R.id.editKapital);
		editProcent = (EditText) findViewById(R.id.editProcent);
		//textKwota = (TextView) findViewById(R.id.textDeposit);
		//textZysk = (TextView) findViewById(R.id.textMonths);
		bOblicz.setOnClickListener(this);
		//ddlistOkres.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listArray));
		
		//ddownKapital = (Spinner) findViewById(R.id.ddownKapital);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapterKapital = ArrayAdapter.createFromResource(this,
		        R.array.kapitalizacja, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapterKapital.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		ddownKapital.setAdapter(adapterKapital);
		
		//ddownOkres = (Spinner) findViewById(R.id.ddownOkres);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapterOkres = ArrayAdapter.createFromResource(this,
		        R.array.okres, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapterOkres.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		ddownOkres.setAdapter(adapterOkres);
		
		ddownKapital.setOnItemSelectedListener(this);
		ddownKapital.setSelection(1);
		ddownOkres.setOnItemSelectedListener(periodListner);
		ddownOkres.setSelection(1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0.getId() == R.id.bOblicz){
			String kwotaStr = editKwota.getText().toString();
			String procentStr = editProcent.getText().toString();
			String okresStr = editOkres.getText().toString();
			String wplataStr = editWplata.getText().toString();
			
			double wplata = Double.parseDouble(wplataStr);
			double kwota = Double.parseDouble(kwotaStr);
			double procent = Double.parseDouble(procentStr) / 100;
			double okres = Double.parseDouble(okresStr);
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
			
			double paymentPerYear = wplata*12;
			double paymentPerCompundPeriod = paymentPerYear/compoundsPerYear;
			
			
			/*
			for (int m = 1; m < okres*numMonthPeriod; m++){
				procentNetto = (Math.pow(1+(procent/(kapitalizacja*100)*(1-podatek/100)),kapitalizacja*okres)-1);
				suma = suma + wplata + suma*procentNetto;
				kapital = kapital + wplata;
			}
			
			double zysk = suma - kapital;
			*/
			
			double numberOfYears = okres*timeUnitInYears;
			double i = procent/compoundsPerYear;
			double n = numberOfYears*compoundsPerYear;
			double pmt = paymentPerCompundPeriod;
						
			
			double futureValue = principal * Math.pow(1+i,n);
			
			futureValue+=pmt/i*(Math.pow(1+i, n)-1);
			
			double zysk = futureValue - principal - n*pmt;
			
			String currency = getString(R.string.currency);
			//String pre_currency = getString(R.string.pre_currency);
			
			textKwota.setText(String.format("%.2f", futureValue)+currency);
			textZysk.setText(String.format("%.2f", zysk)+currency);
			
		}
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
				kapitalizacja = 30;
				compoundsPerYear = 365;
				break;
			case 1:
				kapitalizacja = 1;
				compoundsPerYear = 12;
				break;
			case 2:
				kapitalizacja = 1;
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
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
