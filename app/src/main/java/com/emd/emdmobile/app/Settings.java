package com.emd.emdmobile.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

public class Settings extends Activity {

    private emdSession clientSession;
    private final static String ClientSession = "ClientSession";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if(clientSession == null) {
            clientSession = (emdSession) getIntent().getExtras().getSerializable(ClientSession);
            clientSession.initPreferences(this);
        }

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().replace(R.id.container, new preferenceActivity()).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle b = new Bundle();
        b.putSerializable(ClientSession,clientSession);
        Intent i = new Intent(Settings.this,MainActivity.class);
        i.putExtras(b);
        NavUtils.navigateUpTo(this, i);
        return true;
    }
}
