package com.zmobile.percentcalc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.ads.AdView;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.widget.Toast;

public class PersonInfo {
	
	private double BMR;
	private double nutriFactor;
	final double LbsRatio = 0.45359237;	
	public String mSex;
	public double mWgtKg;
	public int mWgtLb;
	public int adDestroy;
	
	List<HashMap<String,Double>> mDrinks = new ArrayList<HashMap<String, Double>>();
	
	private static PersonInfo instance = null;
	//public static AdView adView;
	
	Map<String,Double> RDI;
	
   public static PersonInfo getInstance(Context ctx) {
	      if(instance == null) {
	    	  instance = new PersonInfo(ctx);
	      }		    	  		      
	      return instance;
   }
	
	private PersonInfo(Context ctx){
		
		this(ctx, "Female");
	}
	
	private PersonInfo(Context ctx, String sex){
		
		RDI = new HashMap<String,Double>();
		
		SharedPreferences settings = ctx.getApplicationContext().getSharedPreferences("Settings", 0);
		
		mSex = settings.getString("sex", "Male");
		mWgtLb = settings.getInt("lbs", 120);
		mWgtKg = mWgtLb*LbsRatio;
		adDestroy = ctx.getResources().getInteger(R.integer.ad_destroy);
		/*
		int calories;
		if (sex.equals("Male")){
			calories = ctx.getResources().getInteger(R.integer.calories_men);			
		}else{
			calories = ctx.getResources().getInteger(R.integer.calories_women);		
		}
		nutriFactor = (double)BMR/calories;
		
		double fat = ctx.getResources().getInteger(R.integer.total_fat)*nutriFactor;
		double protein = ctx.getResources().getInteger(R.integer.protein);
		double cholesterol = ctx.getResources().getInteger(R.integer.cholesterol)*nutriFactor;
		double saturated_fat = ctx.getResources().getInteger(R.integer.saturated_fat)*nutriFactor;
		double carbo = ctx.getResources().getInteger(R.integer.carbo)*nutriFactor;
		double fiber = ctx.getResources().getInteger(R.integer.fiber)*nutriFactor;
		double sodium = ctx.getResources().getInteger(R.integer.sodium)*nutriFactor;
		double potas = ctx.getResources().getInteger(R.integer.potas)*nutriFactor;
		double calcium = ctx.getResources().getInteger(R.integer.calcium)*nutriFactor;
		double iron = ctx.getResources().getInteger(R.integer.iron)*nutriFactor;
		double sugar = ctx.getResources().getInteger(R.integer.sugar)*nutriFactor;
		double vitamin_a = ctx.getResources().getInteger(R.integer.vitamin_a)*nutriFactor;
		double vitamin_c = ctx.getResources().getInteger(R.integer.vitamin_c)*nutriFactor;		
		
		RDI.put("calories", BMR);
		RDI.put("fat", fat);
		
		RDI.put("protein", protein);
		RDI.put("cholesterol", cholesterol);
		RDI.put("saturated_fat", saturated_fat);
		RDI.put("carbohydrate", carbo);
		RDI.put("fiber", fiber);
		RDI.put("sodium", sodium);
		RDI.put("potassium", potas);
		RDI.put("calcium", calcium);
		RDI.put("iron", iron);
		RDI.put("sugar", sugar);
		RDI.put("vitamin_a", vitamin_a);
		RDI.put("vitamin_c", vitamin_c);
		*/
	}
	
	public double addDrink(int timeMin, double amount_ml, double percent){
		
		HashMap<String,Double> drink = new HashMap<String,Double>();
		
		drink.put("time", (double)timeMin);
		drink.put("amt", amount_ml);
		drink.put("perc", percent);
		
		mDrinks.add(drink);
		
		return 0;
	}	
	
	public double getPercent(String ingredient, double amount){
		double result = 0;
		
		if (RDI.containsKey(ingredient)){
			result = 100*amount/RDI.get(ingredient);
		}		
		return result;
	}
	
	public double getPercent(String ingredient, String amountStr){
		double result = 0;
		
		if (!amountStr.equals("")){
			double amount = Double.valueOf(amountStr);	
			result = getPercent(ingredient, amount);
		}		
		return result;
	}	
	
	//gets ingredient key string and amount as double, returns formatted RDI string
	public String getPercentStr(String ingredient, double amount){		
		
		double rdiRatio = getPercent(ingredient, amount);
		return formatRDI(rdiRatio);
		
	}
	
	//gets ingredient key string and amount as stirng, returns formatted RDI string
	public String getPercentStr(String ingredient, String amountStr){
		//String result = "0 %";		
		double value = getPercent(ingredient, amountStr);				
		return formatRDI(value);
	}
	
	// get double and returns formated as 0.0%
	private String formatRDI(double rdiRatio){
		int intRatio = (int)Math.round(rdiRatio);
		String rdiText = String.valueOf(intRatio)+"%";
		rdiText = String.format("%.1f", rdiRatio)+"%";
		
		return rdiText;
	}
	
	public String getBMIcomment(double bmi){
		String comment = "";
		if (bmi < 15){
			comment = "xx_low";
		}else if (bmi < 16){
			comment = "x_low";
		}else if (bmi < 18.5){
			comment = "low";
		}else if (bmi < 25){
			comment = "normal";
		}else if (bmi < 30){
			comment = "high";
		}else if (bmi < 35){
			comment = "x_high";
		}else if (bmi < 40){
			comment = "xx_high";
		}else{
			comment = "xxx_high";
		}
		return comment;
	}
	
	public double calcBMI(double height, double weight){
		
		double bmi = weight/Math.pow(height/100,2);
		return bmi;
	}
	
	public double calcBMR(String sex, double weight, int height, int age){
		
		double BMR;	
		if (sex.equals("Male")){		
			BMR = 88.362 + (13.397*weight) + (4.799*height) - (5.677 * age);
		}else{		
			BMR = 447.593 + (9.247*weight) + (3.098*height) - (4.330 * age);						
		}
		return BMR;
	}
	
	public String trimDescription(String desc){
		
		desc = desc.replaceAll("[:|]", "");
		desc = desc.replaceAll(".00", "");
		
		return desc;
	}
	
	private double getAmount(String amountStr){
	
		double amount = 1.0; // number of units in serving
		double unitGrams = 100; // size of unit in grams
		int amtEnd = amountStr.length(); //end position of amount in string
		boolean foundUnit = false;
		double result = -1;
	
		int cup = amountStr.indexOf("cup");
		int oz = amountStr.indexOf("oz");
		int floz = amountStr.indexOf("fl oz");
		int bar = amountStr.indexOf("bar");
		int bun = amountStr.indexOf("bun");
		int egg = amountStr.indexOf("egg");
		int serv = amountStr.indexOf("serving");
		int gram = amountStr.indexOf("g ");
		int piece = amountStr.indexOf("piece");
		int can = amountStr.indexOf("can");
		int nugget = amountStr.indexOf("nugget");
		int tbsp = amountStr.indexOf("tbsp");
		int slice = amountStr.indexOf("slice");
		int stick = amountStr.indexOf("stick");
		int bread = amountStr.indexOf("breadstick");
		int bagel = amountStr.indexOf("bagel");
		
	
		int slash = amountStr.indexOf("/");
		if (slash > 0){
			int quart = amountStr.indexOf("1/4");
			int quart3 = amountStr.indexOf("3/4");
			int third = amountStr.indexOf("1/3");
			int third2 = amountStr.indexOf("2/3");
			int half = amountStr.indexOf("1/2");
			
			if (quart > 0) amount = 0.25;
			if (quart3 > 0) amount = 0.75;
			if (third > 0) amount = 0.33;
			if (third2 > 0) amount = 0.66;
			if (half > 0) amount = 0.5;		
		}				
		
		if (gram > 0)  { unitGrams = 1;     amtEnd = gram; 	foundUnit = true; }
		if (cup > 0)   { unitGrams = 259;   amtEnd = cup; 	foundUnit = true; }
		if (oz > 0)    { unitGrams = 28.35; amtEnd = oz; 	foundUnit = true; }
		if (floz > 0)  { unitGrams = 28.35; amtEnd = floz; 	foundUnit = true; }
		if (serv > 0)  { unitGrams = 100;   amtEnd = serv; 	foundUnit = true; }		 
		if (piece > 0) { unitGrams = 100;   amtEnd = piece; foundUnit = true; }
		if (can > 0)   { unitGrams = 333;   amtEnd = can; 	foundUnit = true; }
		if (nugget > 0){ unitGrams = 100;   amtEnd = nugget;foundUnit = true; }
		if (tbsp > 0)  { unitGrams = 15;    amtEnd = tbsp; 	foundUnit = true; }
		if (slice > 0) { unitGrams = 20;    amtEnd = slice; foundUnit = true; }
		if (stick > 0) { unitGrams = 50;    amtEnd = stick; foundUnit = true; }
		if (bread > 0) { unitGrams = 50;    amtEnd = bread; foundUnit = true; }
		if (bagel > 0) { unitGrams = 50;    amtEnd = bagel; foundUnit = true; }
		if (bun > 0)   { unitGrams = 50;    amtEnd = bun; foundUnit = true; }
		if (egg > 0)   { unitGrams = 50;    amtEnd = egg; foundUnit = true; }
		if (bar > 0)   { unitGrams = 40;    amtEnd = bar; foundUnit = true; }
		
		if (foundUnit == true){
			String amt = amountStr.substring(0, amtEnd);
			
			if (amt.equals("") || amt.equals(" ")) 
					amt = "1";
			
			if (slash <= 0)
				amount = strToDbl(amt, 1);
		
			result = unitGrams*amount;
		}
		return result;
	}
	
	public double getEnergy(String desc){
				
		int foundCup = -1;
		double energy = -1;
		
		int foundGram = desc.indexOf("- Calories:");
		if (foundGram > 0){
			int kcalPos = desc.indexOf("kcal");
			String kcalStr = desc.substring(foundGram+12, kcalPos);
			String amountStr = desc.substring(4, foundGram);
			String gramStr = desc.substring(4, foundGram);
			
			double grams = getAmount(amountStr);
			
			/*
			foundCup = amountStr.indexOf("cup");															
			int grams = strToInt(gramStr, 100);*/
			if (grams > 0){
				int kcalValue = strToInt(kcalStr, 100);
				
				double gramFactor = grams/100;
				energy = kcalValue/gramFactor;
			}
		}		
		return energy;
	}
	
	public String get(Map<String, String> map, String key){
		
		if (map.containsKey(key))
			return map.get(key);
		else
			return "n/a";
		
	}
	
	public int strToInt(String str, int def){
		int res = def;
		try{			
			res = Integer.valueOf(str);
		}catch(Exception e){
			res = def;
		}
		return res;
	}
	
	public long strToLong(String str, long def){
		long res = def;
		try{			
			res = Integer.valueOf(str);
		}catch(Exception e){
			res = def;
		}
		return res;
	}
	
	public double strToDbl(String str, double def){
		double res = def;
		str = str.replaceAll(",", ".");
		try{			
			res = Double.valueOf(str);
		}catch(Exception e){
			res = def;
		}
		return res;
	}
	
	public double lbsToKg(int lbs){
		
		double weightKg = (double)Math.round(lbs*LbsRatio*2)/2;
		return weightKg;
	}
	
	public double kgToLbs(double kg){
		
		double weightLbs = (double)Math.round(kg/LbsRatio);
		return weightLbs;
	}
	
	GradientDrawable getGradient(int color, int param, int factor){
		
		int colors[] = new int[2];
		colors[0] = color;
		colors[1] = ModifyColor(color, param, factor);
		GradientDrawable backColor = new GradientDrawable(Orientation.TOP_BOTTOM, colors);
		return backColor;
	}
	
	int getColor(double bmi) {
		float[] hsv = new float[3];
		int color = Color.GRAY;		
		Color.colorToHSV(color, hsv);
		//hsv[parameter] = hsv[parameter]/factor;
		//hsv[0] = (float)(-bmi/10 + 7.5)/92*360+360;
		//hsv[0] = (float)(300/((bmi-0.5)/7)-23);
		hsv[0] = (float)(360-bmi*12);
		hsv[1] = 1;
		hsv[2] = 0.85f;
		int newCol = Color.HSVToColor(hsv);
		return newCol;
	}
	
	int getColorBar(double percent) {
		float[] hsv = new float[3];
		int color = Color.GRAY;		
		Color.colorToHSV(color, hsv);
		//hsv[parameter] = hsv[parameter]/factor;
		//hsv[0] = (float)(-bmi/10 + 7.5)/92*360+360;
		//hsv[0] = (float)(300/((bmi-0.5)/7)-23);
		//percent = Math.min(percent,100);
		hsv[0] = (float)(300-percent*1.8);
		hsv[1] = 1;
		hsv[2] = 0.75f;
		int newCol = Color.HSVToColor(hsv);
		return newCol;
	}	
	
	// Modifies given color parameter in HSV by factor
	int ModifyColor(int color, int parameter, float factor) {
		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);
		hsv[parameter] = hsv[parameter]/factor;
		int newCol = Color.HSVToColor(hsv);
		return newCol;
	}	
	
	// Changes string to make all first letters capital
	public String firstCapital(String s){
		final StringBuilder result = new StringBuilder(s.length());
		String[] words = s.split("\\s");
		for(int i=0,l=words.length;i<l;++i) {
		  if(i>0) result.append(" ");      
		  result.append(Character.toUpperCase(words[i].charAt(0)))
		        .append(words[i].substring(1));

		}
		return result.toString();
	}
	
	// Gets last day of previous to given month
    public static long getLastMonthLastDate(Calendar calendar) {
        //Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);

        int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, max);

        return calendar.getTimeInMillis();
    }
    
	// Gets last day of previous to given month
    public static long getLastMonthFirstDate(Calendar calendar) {
        //Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);

        int min = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, min);

        return calendar.getTimeInMillis();
    }    
	
	public void toastMsg(Context ctx, int stringId){
		
		String msg = ctx.getResources().getString(stringId);
		Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
	}		
	
	String strFormat(double number){
		String str;
		if (Math.abs(number - (int)number) >= 0.01)
			str = String.format("%.2f", number);
		else
			str = String.format("%.0f", number);
		str.replace(".00", "");
		str.replace(",00", "");
		return str;
	}	

}
