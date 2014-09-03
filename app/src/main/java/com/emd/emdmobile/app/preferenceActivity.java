package com.emd.emdmobile.app;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Bradl_000 on 2014-07-23.
 */
public class preferenceActivity extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        addPreferencesFromResource(R.xml.preferences);
    }
}
