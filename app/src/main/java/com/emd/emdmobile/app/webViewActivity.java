package com.emd.emdmobile.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class webViewActivity extends Activity {

    private emdSession clientSession;
    private final static String ClientSession = "ClientSession";
    private final static String ActionbarColour = "#012968";
    private final static String Title = "Details";

    private class eMDWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String URL){
            view.loadUrl(URL);
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clientSession = (emdSession)getIntent().getExtras().getSerializable(ClientSession);
        clientSession.initPreferences(this);
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(Title);
        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setColor(Color.parseColor(ActionbarColour));
        actionBar.setBackgroundDrawable(colorDrawable);
        setContentView(R.layout.activity_web_view);
        createWebView();
    }

    private void createWebView(){
        WebView wView = (WebView)findViewById(R.id.webPageViewer);
        wView.setWebViewClient(new eMDWebViewClient());
        wView.getSettings().setJavaScriptEnabled(true);
        wView.loadUrl(clientSession.currentURL);
        wView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }
        });
        wView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home:
                    Bundle b = new Bundle();
                    b.putSerializable(ClientSession,clientSession);
                    Intent i = new Intent(webViewActivity.this,MainActivity.class);
                    i.putExtras(b);
                    NavUtils.navigateUpTo(this, i);
            default:
                super.onOptionsItemSelected(item);
        }

        return true;
    }

}
