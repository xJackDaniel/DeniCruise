package com.example.denicruise.Classes;

import static com.example.denicruise.Classes.Constants.FILENAME;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuInflater;

import androidx.appcompat.app.AppCompatActivity;

import com.example.denicruise.HomePage;
import com.example.denicruise.MainActivity;
import com.example.denicruise.MyCruisesPage;
import com.example.denicruise.ProfilePage;
import com.example.denicruise.R;
import com.example.denicruise.SettingsPage;

public class DeniCruiseMenu {

    AppUser user;
    public Activity activity;
    SharedPreferences sharedPreferences;

    public DeniCruiseMenu(Activity _activity, AppUser user) {
        this.activity = _activity;
        this.user = user;
    }


    public boolean CreateMenu(Menu menu) {
        // Load custom menu
        MenuInflater inflater = this.activity.getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean OptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.profile_button) {
            View menuItemView = this.activity.findViewById(R.id.profile_button); // SAME ID AS MENU ID
            ProfileActivity(menuItemView);
        }
        else if(item.getItemId()==R.id.action_cruises) {
            View menuItemView = this.activity.findViewById(R.id.action_cruises); // SAME ID AS MENU ID
            MyCruisesActivity(menuItemView);
        }
        else if(item.getItemId()==R.id.action_settings) {
            View menuItemView = this.activity.findViewById(R.id.action_settings); // SAME ID AS MENU ID
            SettingsActivity(menuItemView);
        }
        else if(item.getItemId()==R.id.action_home) {
            View menuItemView = this.activity.findViewById(R.id.action_home); // SAME ID AS MENU ID
            HomePageActivity(menuItemView);
        }
        else if(item.getItemId()==R.id.action_logout)
        {
            View menuItemView = this.activity.findViewById(R.id.action_logout); // SAME ID AS MENU ID
            // Logout the user
            sharedPreferences = this.activity.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            LogInActivity(menuItemView);
        }
        return true;
    }


    public void ProfileActivity(View view)
    {
        // Intent to Profile page with User details
        Intent intent = new Intent(this.activity, ProfilePage.class);

        // Pass the user Object
        intent.putExtra("user", user);

        // Start
        this.activity.startActivity(intent);
    }

    public void MyCruisesActivity(View view)
    {
        // Intent to MyCruises page with User details
        Intent intent = new Intent(this.activity, MyCruisesPage.class);

        // Pass the user Object
        intent.putExtra("user", user);

        // Start
        this.activity.startActivity(intent);
    }

    public void SettingsActivity(View view)
    {
        // Intent to Settings page with User details
        Intent intent = new Intent(this.activity, SettingsPage.class);

        // Pass the user Object
        intent.putExtra("user", user);

        // Start
        this.activity.startActivity(intent);
    }

    public void HomePageActivity(View view)
    {
        // Intent to Home page with User details
        Intent intent = new Intent(this.activity, HomePage.class);

        // Pass the user Object
        intent.putExtra("user", user);

        // Start
        this.activity.startActivity(intent);
    }

    public void LogInActivity(View view)
    {
        // Intent to Settings page without User details
        Intent intent = new Intent(this.activity, MainActivity.class);

        // Start
        this.activity.startActivity(intent);
    }

}
