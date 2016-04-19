package com.paxdron.neurodynaussiesport;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Antonio on 27/03/2016.
 */
public class settingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(android.R.id.content, new SettingsFragment());
        ft.commit();
    }

}
