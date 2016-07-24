package com.sanath.movies.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sanath.movies.fragments.SettingFragment;

/**
 * Created by sanathnandasiri on 7/21/16.
 */

public class SettingActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, new SettingFragment())
                    .commit();
        }
    }
}
