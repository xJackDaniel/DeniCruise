package com.example.denicruise;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.example.denicruise.Classes.AppUser;
import com.example.denicruise.Classes.DeniCruiseMenu;

public class SettingsPage extends AppCompatActivity {

    AppUser user;
    DeniCruiseMenu MyMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // Get the user
        user = (AppUser) getIntent().getSerializableExtra("user");

        // Create Menu Object
        MyMenu = new DeniCruiseMenu(this, user);
    }

    // Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return MyMenu.CreateMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return MyMenu.OptionsItemSelected(item);
    }
    // End Menu

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}