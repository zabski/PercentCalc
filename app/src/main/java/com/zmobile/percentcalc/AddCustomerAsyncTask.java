package com.zmobile.percentcalc;

//import com.zmobile.backend.myApiLukasz.*;
//import com.zmobile.backend.myApiLukasz.model.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Pair;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.zmobile.foodendpoint.customerApi.CustomerApi;
import com.zmobile.foodendpoint.customerApi.model.OfyCustomer;

import java.io.IOException;

//import com.zmobile.myapplication.customerApi.CustomerApi;
//import com.zmobile.myapplication.customerApi.model.OfyCustomer;

//import com.zmobile.myapplication.myApiLukasz.MyApiLukasz;

//import com.zmobile.myapplication.myApiLukasz.model.*;
//import javafx.util.Pair;

/**
 * Created by lukasz on 16.02.2017.
 */

class AddCustomerAsyncTask extends AsyncTask<Pair<Context, OfyCustomer>, Void, String> {
    private static CustomerApi myApiService = null;
    private Context context;

    @Override
    protected String doInBackground(Pair<Context, OfyCustomer>... params) {
        if(myApiService == null) {  // Only do this once
            CustomerApi.Builder builder = new CustomerApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    //.setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    //.setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    //.setRootUrl("http://192.168.0.18:8080/_ah/api/")
                    //.setRootUrl("https://innate-star-87415.appspot.com/_ah/api/")
                    //.setRootUrl("https://hazel-strand-87611.appspot.com/_ah/api/")
                    .setRootUrl("https://centering-force-847.appspot.com/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }

        context = params[0].first;
        OfyCustomer customer = params[0].second;

        try {
            OfyCustomer bean = myApiService.insertCustomer(customer).execute();
            return bean.getAddr();
        } catch (IOException e) {
            String result = e.getMessage();
            //Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            //((MainActivity)context).text.setText(result);
            return result;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        //Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        //((MainActivity)context).text.setText(result);
        SharedPreferences settings = context.getApplicationContext().getSharedPreferences("Settings", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("userDataSaved", true);
        editor.commit();
    }
}
