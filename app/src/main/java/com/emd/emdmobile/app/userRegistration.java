package com.emd.emdmobile.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.emd.emdmobile.app.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class userRegistration extends Activity implements asyncTaskCompleteListner{

    private emdSession clientSession;
    private final static String ClientSession = "ClientSession";
    private final static String Unknown = "Unknown";
    private final static String ActionbarColour = "#012968";
    private final static String EnterValidPassword = "Please enter a valid password";
    private final static String LoadingData = "Loading Data";
    private final static String Authentication = "Authenticating User";
    private final static String JsonUserDetails = "UserDetails";
    private final static String JsonPracticeNumber = "PracticeNumber";
    private final static String JsonPrefMethod = "PrefMethod";
    private final static String JsonUserID = "UserID";
    private final static String JsonOTP = "OTP";
    private final static String JsonPassword = "Password";
    private final static String InvalidPractice = "Invalid practice information";
    private final static String JsonSearchMethod = "Search";
    private final static String JsonOTPMethod = "OTP";
    private final static String JsonConfirmMethod = "Confirm";
    private final static String OTP = "Sending OTP";
    private final static String Confirm = "Confirming Password";
    private final static String OTPEnter = "Please enter your OTP";
    private final static String PasswordMatch = "Passwords do not match";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        ActionBar actionBar = getActionBar();
        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setColor(Color.parseColor(ActionbarColour));
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle(R.string.registrationRegistrationPractice);

        //get the session from the bundle
        if(clientSession == null) {
            clientSession = (emdSession) getIntent().getExtras().getSerializable(ClientSession);
            clientSession.initPreferences(this);
        }

        if(clientSession != null) {
            if (clientSession.uDetails != null) {
                showPracticeDetails(false);
            } else {
                showPracticeDetails(true);
            }
        }
        else{
            showPracticeDetails(true);
        }

        Button btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAuthenticationWebMethod();
            }
        });

        Button btnReset = (Button)findViewById(R.id.btnSetPassword);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createForgotPasswordDialog();
            }
        });
    }

    private void showPracticeDetails(boolean clearValues){
        //get all of the view widgets
        TextView txtPracticeNumber = (TextView)findViewById(R.id.txtPracticeNumber);
        TextView txtPracticeName = (TextView)findViewById(R.id.txtPracticeName);
        TextView txtUsername = (TextView)findViewById(R.id.txtUsername);
        TextView txtEmail = (TextView)findViewById(R.id.txtEmail);
        TextView txtMobile = (TextView)findViewById(R.id.txtMobile);
        EditText txtPassword = (EditText)findViewById(R.id.txtPassword);
        txtPassword.clearFocus();
        txtPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            }
        });

        if(clearValues){
            txtPracticeName.setText(Unknown);
            txtPracticeNumber.setText(Unknown);
            txtUsername.setText(Unknown);
            txtEmail.setText(Unknown);
            txtMobile.setText(Unknown);
            txtPassword.setText("");
        }
        else{
            if(clientSession.uDetails != null) {
                txtUsername.setText(clientSession.uDetails.UserName);

                if (clientSession.uDetails.PracticeNumber != null && clientSession.uDetails.PracticeNumber.length() == 0) {
                    txtPracticeNumber.setText(Unknown);
                } else {
                    txtPracticeNumber.setText(clientSession.uDetails.PracticeNumber);
                }

                if (clientSession.uDetails.PracticeName != null && clientSession.uDetails.PracticeName.length() == 0) {
                    txtPracticeName.setText(Unknown);
                } else {
                    txtPracticeName.setText(clientSession.uDetails.PracticeName);
                }

                if (clientSession.uDetails.Email != null && clientSession.uDetails.Email.length() == 0) {
                    txtEmail.setText(Unknown);
                } else {
                    txtEmail.setText(clientSession.uDetails.Email);
                }

                if (clientSession.uDetails.Cell != null && clientSession.uDetails.Cell.length() == 0) {
                    txtMobile.setText(Unknown);
                } else {
                    txtMobile.setText(clientSession.uDetails.Cell);
                }

                if (clientSession.uDetails.Password != null && clientSession.uDetails.Password.length() == 0) {

                } else {
                    if (clientSession.validPassword) {
                        txtPassword.setText(clientSession.uDetails.Password);
                    }
                }
            }
        }
    }

    private void createSearchDialog(){
        final Dialog searchDialog = new Dialog(this);
        searchDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        searchDialog.setContentView(R.layout.search_dialog);
        TextView txtHeader = (TextView)searchDialog.findViewById(R.id.lblSearchLabel);
        final EditText txtSearch = (EditText)searchDialog.findViewById(R.id.txtSearch);
        Button btnSearch = (Button)searchDialog.findViewById(R.id.btnSearch);
        Button btnCancel = (Button)searchDialog.findViewById(R.id.btnCancel);
        txtSearch.requestFocus();
        txtHeader.setText("Practice Number");
        searchDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPracticeDetails(true);
                callWebMethod(txtSearch.getText().toString());
                searchDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDialog.dismiss();
            }
        });

        searchDialog.show();
    }

    private void createForgotPasswordDialog(){
        if(clientSession.uDetails != null) {
            final Dialog forgotDialog = new Dialog(this);
            forgotDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            forgotDialog.setContentView(R.layout.reset_password_dialog);

            Button btnSetPassword = (Button)forgotDialog.findViewById(R.id.btnConfirmPassword);
            Button btnCancel = (Button)forgotDialog.findViewById(R.id.btnForgotCancel);
            Button btnSendOTP = (Button)forgotDialog.findViewById(R.id.btnVerificationSend);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    forgotDialog.dismiss();
                }
            });

            btnSendOTP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Spinner pref = (Spinner)forgotDialog.findViewById(R.id.forgotVerificationSpinner);
                    String selectedMethod = pref.getSelectedItem().toString();
                    callWebMethodOTP(clientSession.uDetails.UserID,selectedMethod);
                }
            });

            btnSetPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText edtPassword = (EditText)forgotDialog.findViewById(R.id.txtPasswordSet);
                    EditText edtConfirmPassword = (EditText)forgotDialog.findViewById(R.id.txtPasswordConfirm);
                    EditText edtOPT = (EditText)forgotDialog.findViewById(R.id.txtOTP);

                    if(edtOPT.getText().length() > 0){
                        if(edtPassword.getText().length() > 0 && edtConfirmPassword.getText().length() > 0){
                            if(edtConfirmPassword.getText() == edtConfirmPassword.getText()){
                                callWebMethodVerify(clientSession.uDetails.UserID, edtPassword.getText().toString(),edtOPT.getText().toString());
                                forgotDialog.dismiss();
                            }
                            else{
                                edtPassword.setText("");
                                edtConfirmPassword.setText("");
                                clientSession.showToast(userRegistration.this,PasswordMatch.toString());
                                //Toast t = Toast.makeText(getApplicationContext(), PasswordMatch, Toast.LENGTH_LONG);
                                //t.show();
                            }
                        }
                        else{
                            clientSession.showToast(userRegistration.this, EnterValidPassword);
                           /* Toast t = Toast.makeText(getApplicationContext(), EnterValidPassword, Toast.LENGTH_LONG);
                            t.show();*/
                        }
                    }
                    else{
                        clientSession.showToast(userRegistration.this, OTPEnter);
                        /*Toast t = Toast.makeText(getApplicationContext(), OTPEnter, Toast.LENGTH_LONG);
                        t.show();*/
                    }
                }
            });

            forgotDialog.show();
        }
        else{
            clientSession.showToast(userRegistration.this, InvalidPractice);
            /*Toast t = Toast.makeText(getApplicationContext(), InvalidPractice, Toast.LENGTH_LONG);
            t.show();*/
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home:
                if(clientSession.validPassword == true){
                    Bundle b = new Bundle();
                    b.putSerializable(ClientSession,clientSession);
                    Intent i = new Intent(userRegistration.this,MainActivity.class);
                    i.putExtras(b);
                    NavUtils.navigateUpTo(this,i);
                }
                else{
                    clientSession.showToast(userRegistration.this,EnterValidPassword.toString());
                   /* Toast t = Toast.makeText(getApplicationContext(),EnterValidPassword,Toast.LENGTH_LONG);
                    t.show();*/
                }
                break;
            case R.id.action_settings:
                break;
            case R.id.action_search:
                createSearchDialog();
                return true;
            default:
                super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void callWebMethod(String practicenumber){
        asyncTask aTask;
        try {
            JSONObject json = new JSONObject();
            json.put(JsonPracticeNumber,practicenumber);
            aTask = new asyncTask(this);
            aTask.setAsyncSystem(asyncTask.asyncSystem.WebMethod);
            aTask.setClientSession(clientSession);
            aTask.setJsonObject(json);
            aTask.setMessage(LoadingData);
            aTask.setAsyncMethod(JsonSearchMethod);
            aTask.execute(clientSession.getPracticeUserUrl(practicenumber));
        }
        catch(JSONException e){
        }
    }

    private void callWebMethodOTP(String UserID, String PrefMethod){
        asyncTask aTask;
        try{
            JSONObject json = new JSONObject();
            json.put(JsonPrefMethod,PrefMethod);
            json.put(JsonUserID, UserID);
            aTask = new asyncTask(this);
            aTask.setAsyncSystem(asyncTask.asyncSystem.WebMethod);
            aTask.setAsyncMethod(JsonOTPMethod);
            aTask.setClientSession(clientSession);
            aTask.setMessage(OTP);
            aTask.setJsonObject(json);
            aTask.execute(clientSession.getPracticeOtpUrl());
        }
        catch(JSONException e){

        }
    }

    private void callWebMethodVerify(String UserID, String Password, String OTP){
        asyncTask aTask;
        try{
            JSONObject json = new JSONObject();
            json.put(JsonOTP,OTP);
            json.put(JsonPassword, Password);
            json.put(JsonUserID, UserID);
            aTask = new asyncTask(this);
            aTask.setAsyncSystem(asyncTask.asyncSystem.WebMethod);
            aTask.setAsyncMethod(JsonConfirmMethod);
            aTask.setClientSession(clientSession);
            aTask.setMessage(Confirm);
            aTask.setJsonObject(json);
            aTask.execute(clientSession.getPasswordConfirmUrl());
        }
        catch(JSONException e){

        }
    }

    private void callAuthenticationWebMethod(){
        EditText edit = (EditText)findViewById(R.id.txtPassword);

        Gson gSon = new Gson();
        SharedPreferences sp = this.getSharedPreferences(JsonUserDetails, Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = sp.edit();
        spe.putString("json", gSon.toJson(clientSession));
        spe.commit();

        if(clientSession.uDetails != null) {

            if (edit.getText().length() > 0) {
                asyncTask aTask;
                clientSession.uDetails.Password = edit.getText().toString();
                aTask = new asyncTask(this);
                aTask.setClientSession(clientSession);
                aTask.setMessage(Authentication);
                aTask.setAsyncSystem(asyncTask.asyncSystem.DominoAuth);
                aTask.setJsonObject(null);
                aTask.execute(clientSession.getAuthUrl());
            } else {
                clientSession.showToast(userRegistration.this,EnterValidPassword);
                /*Toast t = Toast.makeText(getApplicationContext(), EnterValidPassword, Toast.LENGTH_LONG);
                t.show();*/
            }
        }
        else{
            clientSession.showToast(userRegistration.this, InvalidPractice);
            /*Toast t = Toast.makeText(getApplicationContext(), InvalidPractice, Toast.LENGTH_LONG);
            t.show();*/
        }
    }

    @Override
    public void onWebTaskComplete(String Method, String Result){

        if(Method == JsonSearchMethod){
            try {
                Gson gSon = new Gson();
                userDetails ud = gSon.fromJson(Result,userDetails.class);
                clientSession.uDetails = ud;
                clientSession.validPassword = false;

                if(clientSession.uDetails != null){
                    clientSession.uDetails.Password = "";
                }

                showPracticeDetails(false);
            }
            catch (Exception e){
                Log.e("Json Error", e.getMessage());
            }
        }
        else if(Method == JsonOTPMethod){
            try{
                Result = "{" + Result + "}";
                Result = Result.replace("null","\"null\"");
                Result = Result.replace(":\"{",":{");
                Result = Result.replace("}\"}","}}");
                JSONObject jOTP = new JSONObject(Result);
                JSONObject jOTPchildA = jOTP.getJSONObject("UserVerifyResult");
                String Status = jOTPchildA.getString("Status");
                Toast t = Toast.makeText(getApplicationContext(),Status,Toast.LENGTH_LONG);
                t.show();
                Log.i("json",jOTP.toString());
            }
            catch(JSONException e){
                Log.e("json",e.toString());
            }
        }
        else if(Method == JsonConfirmMethod){
            try{
                Result = "{" + Result + "}";
                Result = Result.replace("null","\"null\"");
                Result = Result.replace(":\"{",":{");
                Result = Result.replace("}\"}","}}");
                JSONObject jOTP = new JSONObject(Result);
                JSONObject jOTPchildA = jOTP.getJSONObject("UserVerifyResult");
                String Status = jOTPchildA.getString("Status");
                Toast t = Toast.makeText(getApplicationContext(),Status,Toast.LENGTH_LONG);
                t.show();
                Log.i("json",jOTP.toString());
            }
            catch(JSONException e){
                Log.e("json",e.toString());
            }
        }
    }

    @Override
    public void onDominoTaskComplete(boolean Result){
        clientSession.validPassword = Result;
        Gson gSon = new Gson();
        SharedPreferences sp = this.getSharedPreferences(JsonUserDetails, Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = sp.edit();
        spe.putString("json", gSon.toJson(clientSession));
        spe.commit();

        if(Result == true){
            Bundle b = new Bundle();
            b.putSerializable(ClientSession,clientSession);
            Intent i = new Intent(userRegistration.this,MainActivity.class);
            i.putExtras(b);
            startActivity(i);
        }
        else{
            clientSession.showToast(userRegistration.this,EnterValidPassword);
            /*Toast t = Toast.makeText(getApplicationContext(),EnterValidPassword,Toast.LENGTH_LONG);
            t.show();*/
        }
    }

    @Override
    public void onSqlNonSelectTaskComplete(String Method, long i){}

    @Override
    public void onSqlSelectTaskComplete(String Method, List<sqlObject> objs){}
}
