package com.zmobile.percentcalc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lukasz on 2016-02-26.
 */
public class RemarketHttpCall {

    static String urlToCall = "https://www.googleadservices.com/pagead/conversion/980261141/";
    static public String result;
    static public int status;

    public static String get() {

        Thread netCall = new Thread() {

            StringBuilder response = new StringBuilder();

            public void run() {
                try {

                    URL url = new URL(urlToCall);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    int code = conn.getResponseCode();
                    String line;
                    while ((line = rd.readLine()) != null) {
                        response.append(line);
                    }
                    rd.close();
                    result = response.toString();
                    status = code;
                } catch (Exception e) {
                    String err = e.getMessage();
                }
            }

        };
        netCall.start();
        return result;
    }
}
