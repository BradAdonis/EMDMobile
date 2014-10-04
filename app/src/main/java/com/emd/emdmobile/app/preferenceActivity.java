package com.emd.emdmobile.app;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class preferenceActivity extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        addPreferencesFromResource(R.xml.preferences);
    }
}
