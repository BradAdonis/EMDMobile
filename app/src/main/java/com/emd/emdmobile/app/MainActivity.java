package com.emd.emdmobile.app;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;

import org.apache.http.impl.cookie.DateParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, webView.OnFragmentInteractionListener, patientlistFragment.OnFragmentInteractionListener, asyncTaskCompleteListner {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private String mTitle;
    private int mPosition = 0;
    private static final String NAV_POSITION = "navigationPosition";
    private static final String NAV_TITLE = "navigationTitle";
    private emdSession clientSession;
    private final static String ClientSession = "ClientSession";
    private final static String ActionbarColour = "#012968";
    private final static String JsonPatient = "patient";
    private final static String MessagePatient = "Getting Database Patients";
    private final static String JsonErrorPatient = "Error getting patient information";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get the session from the bundle
        if(clientSession == null) {
            clientSession = (emdSession) getIntent().getExtras().getSerializable(ClientSession);
            clientSession.initPreferences(this);
        }

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        //mTitle = getTitle().toString();

        if(savedInstanceState != null)
        {
            mTitle = savedInstanceState.getString(NAV_TITLE);
            mPosition = savedInstanceState.getInt(NAV_POSITION);
        }

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //Colour the action bar
        ActionBar actionBar = getActionBar();
        ColorDrawable colorDrawable = new ColorDrawable();
        colorDrawable.setColor(Color.parseColor(ActionbarColour));
        actionBar.setBackgroundDrawable(colorDrawable);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putInt(NAV_POSITION, mPosition);
        savedInstanceState.putString(NAV_TITLE, mTitle);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null)
        {
            mTitle = savedInstanceState.getString(NAV_TITLE);
            mPosition = savedInstanceState.getInt(NAV_POSITION);
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    public void onSectionAttached(int number) {
    }

    private void CreateNavigation(int position){
        switch (position + 1) {
            case 1:
                mTitle = getString(R.string.title_section1);
                GetSQLDBPatients(null);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                BuildWebFragmentView("mReportsTotalsMonthly");
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                BuildWebFragmentView("mReportsTotalsMonthly");
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                BuildWebFragmentView("mReportsTotalsMonthly");
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                BuildWebFragmentView("mReportsPatientLiable");
                break;
            case 6:
                mTitle = getString(R.string.title_section6);
                BuildWebFragmentView("mReportsMaVsPat");
                break;
            case 7:
                mTitle = getString(R.string.title_section7);
                BuildWebFragmentView("mRctMAByMnth");
                break;
            case 8:
                mTitle = getString(R.string.title_section8);
                BuildWebFragmentView("mRctPvtByMnth");
                break;
            case 9:
                mTitle = getString(R.string.title_section9);
                BuildWebFragmentView("mRctAccRepByMnth");
                break;
            case 10:
                mTitle = getString(R.string.title_section10);
                BuildBlankFragment(10);
                break;
            case 11:
                mTitle = getString(R.string.title_section11);
                BuildBlankFragment(11);
                break;
            case 12:
                mTitle = getString(R.string.title_section12);
                BuildBlankFragment(12);
                break;
            case 13:
                mTitle = getString(R.string.title_section13);
                BuildBlankFragment(13);
                break;
            case 14:
                mTitle = getString(R.string.title_section14);
                BuildBlankFragment(14);
                break;
            case 15:
                mTitle = getString(R.string.title_section15);
                BuildBlankFragment(15);
                break;
        }
    }

    private void BuildBlankFragment(int number){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(number + 1))
                .commit();
    }

    private void BuildWebFragmentDocument(String Form, String Page){
        FragmentManager fragmentManager = getFragmentManager();
        String URL = clientSession.getDBUrlDocument(Form, Page);
        fragmentManager.beginTransaction().replace(R.id.container, webView.newInstance(URL)).commit();
    }

    private void BuildWebFragmentView(String Page){
        FragmentManager fragmentManager = getFragmentManager();
        String URL = clientSession.getDBUrlView(Page);
        fragmentManager.beginTransaction().replace(R.id.container, webView.newInstance(URL)).commit();
    }

    private void GetSQLDBPatients(String Keyword){
        sqlObject o = new sqlObject();
        o.setType(JsonPatient);
        if(Keyword != null){
            o.setKeywords(Keyword);
        }
        asyncTask a = new asyncTask(this);
        a.setClientSession(clientSession);
        a.setMessage("Retrieving Patient Information");
        a.setSqlMethod(asyncTask.asyncSQLMethod.Select);
        a.setAsyncMethod("selectpatients");
        a.setAsyncSystem(asyncTask.asyncSystem.SqlMethod);
        a.setSqlObject(o);
        a.execute("");
    }

    private void GetPracticePatients(){
        asyncTask aTask = new asyncTask(this);
        aTask.setClientSession(clientSession);
        aTask.setAsyncSystem(asyncTask.asyncSystem.WebMethod);
        aTask.setAsyncMethod(JsonPatient);
        aTask.setMessage(MessagePatient);
        aTask.execute(clientSession.getPatientUrl(clientSession.patientModifiedDate));
    }

    private void CreateSearchDialog(String HeaderLabel, final String SearchType){
        final Dialog searchDialog = new Dialog(this);
        searchDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        searchDialog.setContentView(R.layout.search_dialog);
        TextView txtHeader = (TextView)searchDialog.findViewById(R.id.lblSearchLabel);
        final EditText txtSearch = (EditText)searchDialog.findViewById(R.id.txtSearch);
        txtSearch.setInputType(InputType.TYPE_CLASS_TEXT);
        Button btnSearch = (Button)searchDialog.findViewById(R.id.btnSearch);
        Button btnCancel = (Button)searchDialog.findViewById(R.id.btnCancel);
        txtSearch.requestFocus();
        txtHeader.setText(HeaderLabel);
        searchDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Keyword = txtSearch.getText().toString();

                if(SearchType.contentEquals(JsonPatient)){
                    if(Keyword.length() > 0){
                        GetSQLDBPatients(Keyword);
                    }
                    else{
                        GetSQLDBPatients(null);
                    }
                }
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

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public void onWebTaskComplete(String Method, String result){
        if(Method == JsonPatient){
            try
            {
                JSONArray userArray = new JSONArray(result);
                List<sqlObject> objList = new ArrayList<sqlObject>();

                /*String[] patientsID = new String[userArray.length()];
                ArrayList<patientDetails> Patients = new ArrayList<patientDetails>(userArray.length());*/

                for(int i = 0; i < userArray.length(); i++){
                    JSONObject userObject = new JSONObject(userArray.get(i).toString());
                    Gson g = new Gson();
                    patientDetails p = g.fromJson(userObject.toString(),patientDetails.class);
                    if(p != null){
                        sqlObject s = new sqlObject();
                        s.setJson(userObject.toString());
                        s.setType(JsonPatient);
                        try {
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            s.setModDate(df.parse(p.LastModified));
                        }catch(ParseException d){}
                        s.setUnid(p.Identifier);
                        s.setKeywords("[" + p.FirstName.toLowerCase() + ";" + p.LastName.toLowerCase() + ";" + p.AccountNumber.toLowerCase() + "]");
                        objList.add(s);

                        /*Patients.add(p);
                        patientsID[i] = p.Identifier;*/
                    }
                }

                if(objList.size() > 0){
                    sqlObject o = new sqlObject();
                    o.setType(JsonPatient);
                    asyncTask a = new asyncTask(this);
                    a.setClientSession(clientSession);
                    a.setAsyncSystem(asyncTask.asyncSystem.SqlMethod);
                    a.setMessage("Updating Patients Database");
                    a.setAsyncMethod("insertpatient");
                    a.setSqlMethod(asyncTask.asyncSQLMethod.Refresh);
                    a.setSqlObject(o);
                    a.setSqlObjects(objList);
                    a.execute("");
                }

                Date today = Calendar.getInstance().getTime();
                DateFormat sdt = new SimpleDateFormat("yyyy-MM-dd");
                clientSession.setPatientModifiedDate(this, sdt.format(today));

                if(objList.size() > 0) {
                    clientSession.ShowNotification(this, "Patients Modified/Added", objList.size() + " patients have been modified / added to the database.");
                }
                else
                {
                    clientSession.ShowNotification(this, "Patients Modified/Added", "No patient modifications have been found.");
                }


               /* FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, patientlistFragment.newInstance(Patients,patientsID)).commit();*/
            }
            catch(JSONException e){
                clientSession.showToast(MainActivity.this,JsonErrorPatient);
                /*Toast t = Toast.makeText(getApplicationContext(),JsonErrorPatient,Toast.LENGTH_LONG);
                t.show();*/
            }
        }
    }

    @Override
    public void onDominoTaskComplete(boolean result){

    }

    @Override
    public void onSqlSelectTaskComplete(String Method, List<sqlObject> objs){
        if(Method.contentEquals("selectpatients")){

            String[] patientsID = new String[objs.size()];
            ArrayList<patientDetails> Patients = new ArrayList<patientDetails>(objs.size());

            for(int i = 0; i < objs.size(); i++){
                Gson g = new Gson();
                sqlObject o = objs.get(i);
                patientDetails p = g.fromJson(o.getJson(),patientDetails.class);

                if(p != null){
                    Patients.add(p);
                    patientsID[i] = p.Identifier;
                }
            }

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, patientlistFragment.newInstance(Patients,patientsID)).commit();
        }
    }

    @Override
    public void onSqlNonSelectTaskComplete(String Method, long i){
        if(Method.contentEquals("insertpatient")){
            GetSQLDBPatients(null);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri){
    }

    @Override
    public void onFragmentInteraction(String Function,String id){

        if(Function.contentEquals(JsonPatient)){
            String Url = clientSession.getDBUrlDocument("mPatients",id);
            clientSession.currentURL = Url;
            Bundle b = new Bundle();
            b.putSerializable(ClientSession,clientSession);
            Intent i = new Intent(this,webViewActivity.class);
            i.putExtras(b);
            startActivity(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        clientSession.initPreferences(this);

        switch(id){
            case R.id.action_settings:
                ActionBar actionBar = getActionBar();
                actionBar.setTitle(R.string.appSettingsTitle);
                getFragmentManager().beginTransaction().replace(R.id.container, new preferenceActivity()).commit();
                break;
            case R.id.action_password:
                Bundle b = new Bundle();
                b.putSerializable(ClientSession,clientSession);
                Intent i = new Intent(MainActivity.this, userRegistration.class);
                i.putExtras(b);
                startActivity(i);
                break;
            case R.id.action_refresh:
                GetPracticePatients();
                break;
            case R.id.action_bar_search:
                if(mTitle.contentEquals("Patients")){
                    CreateSearchDialog("Patient Search",JsonPatient);
                }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if(mPosition != 0) {
            position = mPosition;
        }

        if(clientSession == null){
            clientSession = (emdSession)getIntent().getExtras().getSerializable(ClientSession);
        }

        CreateNavigation(position);
    }

    public static class PlaceholderFragment extends Fragment{
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}