package com.example.denicruise;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.denicruise.Classes.AppUser;
import com.example.denicruise.Classes.DeniCruiseMenu;

import static com.example.denicruise.Classes.Constants.*;

public class HomePage extends AppCompatActivity {

    AppUser user;
    DeniCruiseMenu MyMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Get the user
        user = (AppUser) getIntent().getSerializableExtra("user");

        // Create Menu Object
        MyMenu = new DeniCruiseMenu(this, user);
    }

    // Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { return MyMenu.CreateMenu(menu); }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return MyMenu.OptionsItemSelected(item);
    }
    // End Menu




    public void GlobalActivity(View view) {
        // Intent to Global page with User details
        Intent intent = new Intent(this, GlobalCruisePage.class);

        // Pass the user Object
        intent.putExtra("user", user);

        // Start
        startActivity(intent);
    }


    public void AmericaActivity(View view) {
        // Intent to America page with User details
        Intent intent = new Intent(this, AmericaCruisePage.class);

        // Pass the user Object
        intent.putExtra("user", user);

        // Start
        startActivity(intent);
    }

    public void EuropeActivity(View view) {
        // Intent to Europe page with User details
        Intent intent = new Intent(this, EuropeCruisePage.class);

        // Pass the user Object
        intent.putExtra("user", user);

        // Start
        startActivity(intent);
    }
}