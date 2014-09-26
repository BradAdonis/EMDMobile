package com.emd.emdmobile.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import com.google.gson.Gson;

import java.util.List;


public class splashScreen extends Activity implements asyncTaskCompleteListner{

    private static int SPLASH_TIME_OUT = 1000;
    private emdSession clientSession;
    private asyncTask aTask;
    private final static String ClientSession = "ClientSession";
    private final static String JsonUserDetails = "UserDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        //Old method that would start a timer and get the user details
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                clientSession = new emdSession();
                getUserDetails();
            }
        }, SPLASH_TIME_OUT);*/

        PreferenceManager.setDefaultValues(this,R.xml.preferences,false);
        getUserDetails();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getUserDetails(){
        SharedPreferences sp = this.getSharedPreferences(JsonUserDetails, Context.MODE_PRIVATE);
        if(sp != null){
            try {
                String jsonString = sp.getString("json", null);
                if (jsonString != null) {

                    Gson gSon = new Gson();
                    clientSession = gSon.fromJson(jsonString, emdSession.class);

                    if(clientSession == null){
                       startRegistration();
                    }
                    else{

                        if(clientSession.uDetails != null) {
                            authenticateUser();
                        }
                        else{
                            startRegistration();
                        }
                    }
                }
                else {
                    startRegistration();
                }
            }
            catch(Exception e){
                startRegistration();
            }
        }
        else{
            startRegistration();
        }
    }

    //open up the registration form to obtain the practice information
    private void startRegistration(){

        if(clientSession == null){
            initializeClientSession();
        }

        Intent i = new Intent(splashScreen.this, userRegistration.class);
        Bundle b = new Bundle();
        b.putSerializable(ClientSession,clientSession);
        i.putExtras(b);
        startActivity(i);
    }

    private void initializeClientSession(){

        if(clientSession == null){
            clientSession = new emdSession();
        }

        clientSession.initPreferences(this);
    }

    private void authenticateUser(){
        initializeClientSession();

        aTask = new asyncTask(this);
        aTask.setAsyncSystem(asyncTask.asyncSystem.DominoAuth);
        aTask.setJsonObject(null);
        aTask.setClientSession(clientSession);
        aTask.setMessage(null);
        aTask.execute(clientSession.getAuthUrl());
    }

    @Override
    public void onWebTaskComplete(String method,String result){

    }

    @Override
    public void onDominoTaskComplete(boolean Result){
        if(Result == true){
            clientSession.validPassword = true;
            Bundle b = new Bundle();
            b.putSerializable(ClientSession, clientSession);
            Intent i = new Intent(splashScreen.this, MainActivity.class);
            i.putExtras(b);
            startActivity(i);
        }
        else{
            startRegistration();
        }
    }

    @Override
    public void onSqlSelectTaskComplete(String Method, List<sqlObject> objs){

    }

    @Override
    public void onSqlNonSelectTaskComplete(String Method, long i){

    }
}
