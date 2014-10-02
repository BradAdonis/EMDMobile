package com.emd.emdmobile.app;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

public class emdSession implements Serializable {

    public com.emd.emdmobile.app.userDetails uDetails;
    public webFunctionDetails webDetails = new webFunctionDetails();
    public boolean validPassword = false;
    public String currentURL = "";
    public String patientModifiedDate = "";
    public String claimModifiedDate = "";
    public int refreshFrequency = 0;

    public emdSession(){}

    public String getDB(){
        return uDetails.Database.replace('~','/');
    }

    public String getUsername(){
        return uDetails.UserName;
    }

    public String getPassword(){
        return uDetails.Password;
    }

    public String getDBUrlView(String Page){

        String URL = uDetails.Protocol + "://";
        URL += uDetails.WebServer + "/";
        URL += uDetails.AddressBook + "/?";
        URL += "login&username=" + uDetails.UserName + "&";
        URL += "password=" + uDetails.Password + "&";
        URL += "redirectto=" + uDetails.Database.replace('~','/') + "/";
        URL += Page + "?OpenView";

        return URL;
    }

    public String getDBUrlDocument(String Form,String Page){

        String URL = uDetails.Protocol + "://";
        URL += uDetails.WebServer + "/";
        URL += uDetails.AddressBook + "/?";
        URL += "login&username=" + uDetails.UserName + "&";
        URL += "password=" + uDetails.Password + "&";
        URL += "redirectto=" + uDetails.Database.replace('~','/') + "/";
        URL += Form + "/" + Page + "?OpenDocument";

        return URL;
    }

    public String getAuthUrl(){

        String URL = uDetails.Protocol + "://";
        URL += uDetails.WebServer + "/";
        URL += uDetails.AddressBook + "/?";
        URL += "login";

        return URL;
    }

    public String getPracticeUserUrl(String practiceNumber){
        String url = webDetails.Protocol + "://" + webDetails.Server + "/" + webDetails.Sys + "/practice/user/" + practiceNumber;
        return url;
    }

    public String getPracticeOtpUrl(){
        String url = webDetails.Protocol + "://" + webDetails.Server + "/" + webDetails.Sys + "/" + webDetails.Verify;
        return url;
    }

    public String getPasswordConfirmUrl(){
        String url = webDetails.Protocol + "://" + webDetails.Server + "/" + webDetails.Sys + "/" + webDetails.Confirm;
        return url;
    }

    public String getPatientUrl(String modDate){
        String url = webDetails.Protocol + "://" + webDetails.Server + "/" + webDetails.Sys + "/database/patient/" + getDB() + "?moddate=" + modDate;
        return url;
    }

    public String getClaimURL(String modDate){
        String url = webDetails.Protocol + "://" + webDetails.Server + "/" + webDetails.Sys + "/database/claim/" + getDB() + "?moddate=" + modDate;
        return url;
    }

    public void showToast(Activity a,String Message){
        LayoutInflater inflator = a.getLayoutInflater();
        View layout = inflator.inflate(R.layout.custom_toast, (ViewGroup) a.findViewById(R.id.toast_layout_root));

        TextView tv = (TextView)layout.findViewById(R.id.toastText);
        tv.setText(Message);

        Toast t = new Toast(a.getApplicationContext());
        t.setGravity(Gravity.CENTER_VERTICAL,0,0);
        t.setDuration(Toast.LENGTH_LONG);
        t.setView(layout);
        t.show();
    }

    public void initPreferences(Activity a){
        SharedPreferences pm = PreferenceManager.getDefaultSharedPreferences(a);
        webDetails.Protocol = pm.getString("pref_key_comm_protocol",null);
        webDetails.Server = pm.getString("pref_key_comm_server",null);
        webDetails.Sys = pm.getString("pref_key_comm_method",null);
        patientModifiedDate = pm.getString("pref_key_ui_patmod",null);
        claimModifiedDate = pm.getString("pref_key_ui_claimmod",null);
        String value = pm.getString("pref_key_ui_freq",null);
        refreshFrequency = Integer.valueOf(value);
    }

    public void setPatientModifiedDate(Activity a, String ModDate){
        SharedPreferences pm = PreferenceManager.getDefaultSharedPreferences(a);
        SharedPreferences.Editor edt = pm.edit();
        edt.putString("pref_key_ui_patmod",ModDate);
        edt.commit();
    }

    public void setClaimModifiedDate(Activity a, String ModDate){
        SharedPreferences pm = PreferenceManager.getDefaultSharedPreferences(a);
        SharedPreferences.Editor edt = pm.edit();
        edt.putString("pref_key_ui_claimmod",ModDate);
        edt.commit();
    }

    public void ShowNotification(Activity a, String Title, String Message){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(a.getApplicationContext())
                                            .setSmallIcon(R.drawable.ic_launcher)
                                            .setContentTitle(Title)
                                            .setContentText(Message);

        Intent resultIntent = new Intent(a,splashScreen.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(a);
        stackBuilder.addParentStack(splashScreen.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager)a.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }
}
