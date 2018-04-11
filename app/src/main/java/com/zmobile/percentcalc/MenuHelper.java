package com.zmobile.percentcalc;

//import com.suredigit.inappfeedback.FeedbackDialog;
//import com.suredigit.inappfeedback.FeedbackSettings;
//import com.yasesprox.android.transcommusdk.TransCommuActivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;

import com.zmobile.ads.HandlerPurchase;

class MenuHelper {
	
	DialogSymbol dialogSymbol;
	//private FeedbackDialog feedBack;
	
	private static MenuHelper instance = null;
	
	public static MenuHelper getInstance(Context ctx){
	      if(instance == null) {
	    	  instance = new MenuHelper(ctx);
	      }		    	  		      
	      return instance;
	}
		
	private MenuHelper(Context ctx) {
		// TODO Auto-generated constructor stub	
	
		//dialogSymbol = new DialogSymbol(ctx);
		dialogSymbol = DialogSymbol.getInstance(ctx);
		
		//FeedbackSettings feedbackSettings = new FeedbackSettings();
		String feedback = ctx.getString(R.string.feedback);
		String feedbackPrompt = ctx.getString(R.string.feedback_prompt);
		String thanks = ctx.getString(R.string.thanks);
		String msgDev = ctx.getString(R.string.msg_from_dev);
		String opinion = ctx.getString(R.string.opinion);
		String bug = ctx.getString(R.string.bug);
		String question = ctx.getString(R.string.question);
		String cancel = ctx.getString(R.string.cancel);
		String send = ctx.getString(R.string.send);


		/*DIALOG TEXT
		feedbackSettings.setYourComments("Type your question here...");
		feedbackSettings.setTitle(feedback);

		//TOAST MESSAGE
		feedbackSettings.setToast(thanks);
		//feedbackSettings.setToastDuration(Toast.LENGTH_SHORT);  // Default
		feedbackSettings.setToastDuration(Toast.LENGTH_LONG);

		//RADIO BUTTONS
		//feedbackSettings.setRadioButtons(false); // Disables radio buttons
		feedbackSettings.setBugLabel(bug);
		feedbackSettings.setIdeaLabel(opinion);
		feedbackSettings.setQuestionLabel(question);

		/*RADIO BUTTONS ORIENTATION AND GRAVITY
		feedbackSettings.setOrientation(LinearLayout.HORIZONTAL); // Default
		feedbackSettings.setOrientation(LinearLayout.VERTICAL);
		feedbackSettings.setGravity(Gravity.RIGHT); // Default
		feedbackSettings.setGravity(Gravity.LEFT);
		feedbackSettings.setGravity(Gravity.CENTER);

		//SET DIALOG MODAL
		feedbackSettings.setModal(true); //Default is false

		//DEVELOPER REPLIES
		feedbackSettings.setReplyTitle(msgDev);
		feedbackSettings.setReplyCloseButtonText("Close");
		feedbackSettings.setReplyRateButtonText("RATE!");
		
		feedbackSettings.setText(feedbackPrompt);
		feedbackSettings.setIdeaLabel(opinion);
		
		feedBack = new FeedbackDialog((Activity) ctx, "AF-12E0A5BE15C5-AB", feedbackSettings);
		*/
	}
	

	   public boolean handleOnItemSelected(Activity ctx, MenuItem item) {
	          // do something..
		   Intent i;
	        switch (item.getItemId()) {
			case android.R.id.home:
				ctx.finish();
				return true;
	        case R.id.action_regular:
	            i = new Intent(ctx, ActivityRegular.class);
	            ctx.startActivity(i);
	            return true;        
	        case R.id.action_tip:
	            i = new Intent(ctx, ActivityTip.class);
	            ctx.startActivity(i);
	            return true;        
	        case R.id.action_discount:
	            i = new Intent(ctx, ActivityDiscount.class);
	            ctx.startActivity(i);
	            return true;        
	        case R.id.action_price_unit:
	            i = new Intent(ctx, ActivityUnitPrice.class);
	            ctx.startActivity(i);
	            return true;        
	        case R.id.action_loan:
	            i = new Intent(ctx, ActivityLoanCalc.class);
	            ctx.startActivity(i);
	            return true;               
	        case R.id.action_currency:
	          //i = new Intent(this, AddDrinkActivity.class);
	          //startActivity(i);
	          //dialogSymbol.show(this);
	        
	          dialogSymbol.setAct((Updatable)ctx);
	          //dialog = dialogSymbol.getDialog();
	          //dialog.show();
	          //mSymbol = dialogSymbol.getSymbol();
	          //UpdateSymbols();
	          return true;
	        case R.id.action_rate_app:
	        	Uri uri = Uri.parse("market://details?id=" + ctx.getPackageName());
	        	Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
	        	try {
	        		ctx.startActivity(goToMarket);
	        	} catch (ActivityNotFoundException e) {
	        		ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + ctx.getPackageName())));
	        	}
	        	return true;
	        case R.id.action_feedback:
	        	//feedBack.show();
				final Intent emailIntent = new Intent( android.content.Intent.ACTION_SEND);

				emailIntent.setType("plain/text");

				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
						new String[]{"zmobapp@gmail.com"});

				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, ctx.getString(R.string.app_name2));

				String prompt = ctx.getString(R.string.feedback_prompt)+"\n\n";
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,prompt);

				ctx.startActivity(Intent.createChooser(
						emailIntent, prompt));
	        	return true;
			case R.id.action_remove_ads:
				//feedBack.show();
					HandlerPurchase.getInstance(ctx).onRemoveAdsButtonClicked(new View(ctx));
				return true;
	        	/*
	        case R.id.action_translate:
	        	Intent intent = new Intent(ctx, TransCommuActivity.class);
	        	intent.putExtra(TransCommuActivity.APPLICATION_CODE_EXTRA, "VlTywdDdVG");
	        	ctx.startActivity(intent);	          	          
	        case R.id.action_info:
	          i = new Intent(this, InfoActivity.class);
	          startActivity(i);
	          return true;
	          */
	        default:
	          //return super.onOptionsItemSelected(item);
	          return false;
	        } 
	   }

	void dispose(){
		instance = null;
		if (dialogSymbol!=null) {
			dialogSymbol.dispose();
			dialogSymbol.dismiss();
		}
	}

	}