package com.emd.emdmobile.app;
import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
        String url = webDetails.Protocol + "://" + webDetails.Server + "/" + webDetails.Sys + "/database/patient/" + getDB() + "?mod=" + modDate;
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
    }
}
