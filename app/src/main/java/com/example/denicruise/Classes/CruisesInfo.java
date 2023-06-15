package com.example.denicruise.Classes;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;


import static com.example.denicruise.Classes.Constants.*;

import android.content.Intent;
import android.view.View;

import com.example.denicruise.ProfilePage;
import com.example.denicruise.R;

public class CruisesInfo {

    public CruisesInfo() {
        // restrict instantiation
    }

    // Mystical
    public Hashtable<String, String> GetMysticalInfo()
    {
        // creating a info Dictionary
        Hashtable<String, String> info = new Hashtable<String, String>();

        // Insert info
        info.put("name", "Mystical Cruise");
        info.put("startPoint", "Israel");
        info.put("endPoint", "Morocco");
        info.put("destinations", "Israel -> Italy -> Spain -> Morocco");
        info.put("price", "1500");
        info.put("days", "4");
        info.put("description", "A dream cruise that passes through a variety of countries in the world, exposure to many stunning cultures around the world.\nThe cruise has a pool, jacuzzi, water slide, sauna, gym, 5 restaurants and 2 bars.\nThe ship has 7 floors and about 100 rooms, including 4 suites.\nEvery evening on the ship there is a cultural evening in which singers, dancers and stand-up artists will perform.");

        return info;
    }

    // Moon
    public Hashtable<String, String> GetMoonInfo()
    {
        // creating a info Dictionary
        Hashtable<String, String> info = new Hashtable<String, String>();

        // Insert info
        info.put("name", "Moon Cruise");
        info.put("startPoint", "Israel");
        info.put("endPoint", "Argentina");
        info.put("destinations", "Israel -> Portugal -> Brazil -> Argentina");
        info.put("days", "7");
        info.put("price", "2500");
        info.put("description", "A dream cruise that passes through a variety of countries in the world, exposure to many stunning cultures around the world.\nThe cruise has a pool, jacuzzi, water slide, sauna, gym, 5 restaurants and 2 bars.\nThe ship has 7 floors and about 100 rooms, including 4 suites.\nEvery evening on the ship there is a cultural evening in which singers, dancers and stand-up artists will perform.");

        return info;
    }

    // Royal
    public Hashtable<String, String> GetRoyalInfo()
    {
        // creating a info Dictionary
        Hashtable<String, String> info = new Hashtable<String, String>();

        // Insert info
        info.put("name", "Royal Cruise");
        info.put("startPoint", "Spain");
        info.put("endPoint", "France");
        info.put("destinations", "Spain -> Portugal -> France");
        info.put("days", "4");
        info.put("price", "500");
        info.put("description", "A dream cruise that passes through a variety of countries in the world, exposure to many stunning cultures around the world.\nThe cruise has a pool, jacuzzi, water slide, sauna, gym, 5 restaurants and 2 bars.\nThe ship has 7 floors and about 100 rooms, including 4 suites.\nEvery evening on the ship there is a cultural evening in which singers, dancers and stand-up artists will perform.");

        return info;
    }

    // Carnival
    public Hashtable<String, String> GetCarnivalInfo()
    {
        // creating a info Dictionary
        Hashtable<String, String> info = new Hashtable<String, String>();

        // Insert info
        info.put("name", "Carnival Cruise");
        info.put("startPoint", "denmark");
        info.put("endPoint", "Finland");
        info.put("destinations", "Denmark -> Norway -> Sweden -> Finland");
        info.put("days", "4");
        info.put("price", "800");
        info.put("description", "A dream cruise that passes through a variety of countries in the world, exposure to many stunning cultures around the world.\nThe cruise has a pool, jacuzzi, water slide, sauna, gym, 5 restaurants and 2 bars.\nThe ship has 7 floors and about 100 rooms, including 4 suites.\nEvery evening on the ship there is a cultural evening in which singers, dancers and stand-up artists will perform.");

        return info;
    }

    // West
    public Hashtable<String, String> GetWestInfo()
    {
        // creating a info Dictionary
        Hashtable<String, String> info = new Hashtable<String, String>();

        // Insert info
        info.put("name", "West Cruise");
        info.put("startPoint", "Boston");
        info.put("endPoint", "Miami");
        info.put("destinations", "Boston -> New York -> Miami");
        info.put("days", "5");
        info.put("price", "500");
        info.put("description", "A dream cruise that passes through a variety of countries in the world, exposure to many stunning cultures around the world.\nThe cruise has a pool, jacuzzi, water slide, sauna, gym, 5 restaurants and 2 bars.\nThe ship has 7 floors and about 100 rooms, including 4 suites.\nEvery evening on the ship there is a cultural evening in which singers, dancers and stand-up artists will perform.");

        return info;
    }

    // Costa
    public Hashtable<String, String> GetCostaInfo()
    {
        // creating a info Dictionary
        Hashtable<String, String> info = new Hashtable<String, String>();

        // Insert info
        info.put("name", "Costa Cruise");
        info.put("startPoint", "Seattle");
        info.put("endPoint", "San Diego");
        info.put("destinations", "Seattle -> San Fransisco -> Los angeles -> San Diego");
        info.put("days", "5");
        info.put("price", "800");
        info.put("description", "A dream cruise that passes through a variety of countries in the world, exposure to many stunning cultures around the world.\nThe cruise has a pool, jacuzzi, water slide, sauna, gym, 5 restaurants and 2 bars.\nThe ship has 7 floors and about 100 rooms, including 4 suites.\nEvery evening on the ship there is a cultural evening in which singers, dancers and stand-up artists will perform.");

        return info;
    }

}
