package com.example.denicruise.Classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppUser implements Serializable {
    String fullname, email, phone, password;
    boolean connected;
    List<Map<String, String>> cruises;

    public AppUser(String fullname, String email, String phone, String password)
    {
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.connected = true;
        this.cruises = new ArrayList<Map<String, String>>();

    }

    public String getFullname()
    {
        return this.fullname;
    }

    public String getEmail()
    {
        return this.email;
    }

    public String getPhone()
    {
        return this.phone;
    }

    public String getPassword()
    {
        return this.password;
    }

    public String getFirstName()
    {
        String[] parts = this.fullname.split(" ");
        return parts[0];

    }

    public void setCruises(List<Map<String, String>> list)
    {
        this.cruises = list;
    }

    public List<Map<String, String>> getCruises()
    {
        return cruises;
    }
}
