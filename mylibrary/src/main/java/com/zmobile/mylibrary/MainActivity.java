package com.zmobile.mylibrary;

//import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv1 = (TextView) findViewById(R.id.textView);
        tv1.setText("To jest tekst na test :)");
    }
}
