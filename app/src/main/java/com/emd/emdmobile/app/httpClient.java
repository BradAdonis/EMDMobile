package com.emd.emdmobile.app;

import android.content.Context;

import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;

public class httpClient extends DefaultHttpClient {
    private static Context appContenxt = null;
    private static HttpParams params = null;
    private static SchemeRegistry schmReg = null;
    private static Scheme httpsScheme = null;
    private static Scheme httpScheme = null;
    private static String TAG = "eMD Http Client";

    public httpClient(Context c){
        appContenxt = c;

        if(httpScheme == null || httpsScheme == null){
            httpScheme = new Scheme("http", PlainSocketFactory.getSocketFactory(), 80);
            httpsScheme = new Scheme("https",PlainSocketFactory.getSocketFactory(),443);
        }

        getConnectionManager().getSchemeRegistry().register(httpScheme);
        getConnectionManager().getSchemeRegistry().register(httpsScheme);
    }
}
