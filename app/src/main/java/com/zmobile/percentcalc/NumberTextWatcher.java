package com.zmobile.percentcalc;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.ParseException;

public class NumberTextWatcher implements TextWatcher {

    private DecimalFormat df;
    private DecimalFormat dfnd;
    private boolean hasFractionalPart;

    private EditText et;

    public NumberTextWatcher(EditText et)
    {
        df = new DecimalFormat("#,###.##");
        df.setDecimalSeparatorAlwaysShown(true);
        dfnd = new DecimalFormat("#,###");
        this.et = et;
        hasFractionalPart = false;
    }

    /*
    @Override
    public void onTextChanged(CharSequence s, int start, int before,
                              int count) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterTextChanged(Editable s) {
        et.removeTextChangedListener(this);

        try {
            String givenstring = s.toString();
            Long longval;

            if (givenstring.contains(",")) {
                givenstring = givenstring.replaceAll(",", "");
            }
            longval = Long.parseLong(givenstring);
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            String formattedString = formatter.format(longval);
            et.setText(formattedString);
            et.setSelection(et.getText().length());
            // to place the cursor at the end of text
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        }

        et.addTextChangedListener(this);

    }
    */
    @SuppressWarnings("unused")
    private static final String TAG = "NumberTextWatcher";

    @Override
    public void afterTextChanged(Editable s)
    {
        et.removeTextChangedListener(this);
        float textSizeOrg = et.getTextSize();
        float textSizeNew;
        float text_1 = et.getContext().getResources().getDimension(R.dimen.text_xs);
        float text_2 = et.getContext().getResources().getDimension(R.dimen.text_s);
        float text_3 = et.getContext().getResources().getDimension(R.dimen.text_m);
        String valueStr = extractValue(et);
        double value = 0;
        try{
            value = Double.parseDouble(valueStr);
        }catch (Exception e){
            value = 0;
        }
        if (value>99999999) {
            //et.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            et.setTextSize(TypedValue.COMPLEX_UNIT_DIP, text_1);
        }else if (value>999999) {
            //textSizeNew = textSizeOrg * 0.45f;
            //et.setTextSize(R.dimen.text_m);
            //TypedValue.COMPLEX_UNIT_SP, R.dimen.text_m);
            //et.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            et.setTextSize(TypedValue.COMPLEX_UNIT_DIP, text_2);
        }else {
            //textSizeNew = textSizeOrg * 0.5f;
            //et.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            et.setTextSize(TypedValue.COMPLEX_UNIT_DIP, text_3);
            //et.setTextSize(TypedValue.COMPLEX_UNIT_SP, R.dimen.text_s);
            //et.setTextSize(R.dimen.text_s);
        }

        try {
            int inilen, endlen;
            inilen = et.getText().length();

            String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "");
            Number n = df.parse(v);
            int cp = et.getSelectionStart();
            if (hasFractionalPart) {
                et.setText(df.format(n));
            } else {
                et.setText(dfnd.format(n));
            }
            endlen = et.getText().length();
            int sel = (cp + (endlen - inilen));
            if (sel > 0 && sel <= et.getText().length()) {
                et.setSelection(sel);
            } else {
                // place cursor at the end?
                et.setSelection(et.getText().length() - 1);
            }
        } catch (NumberFormatException nfe) {
            // do nothing?
        } catch (ParseException e) {
            // do nothing?
        }

        et.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        if (s.toString().contains(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator())))
        {
            hasFractionalPart = true;
        } else {
            hasFractionalPart = false;
        }
    }

    String extractValue(EditText et){
        return et.getText().toString().replaceAll("\\s", "").replaceAll(",", "");
    }

}
