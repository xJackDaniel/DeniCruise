package com.example.denicruise;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.denicruise.Classes.AppUser;
import com.example.denicruise.Classes.CruisesInfo;
import com.example.denicruise.Classes.DeniCruiseMenu;

import java.util.Hashtable;

public class AmericaCruisePage extends AppCompatActivity {
    DeniCruiseMenu MyMenu;
    AppUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_america_cruise_page);
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

    // Dialog
    public void WestCruiseDialog(View view) {
        // Get the data on the cruise
        CruisesInfo InfoManager = new CruisesInfo();
        Hashtable<String, String> info = InfoManager.GetWestInfo();
        int num = 1;
        StartDialog(view, info, num);
    }

    public void CostaCruiseDialog(View view) {
        // Get the data on the cruise
        CruisesInfo InfoManager = new CruisesInfo();
        Hashtable<String, String> info = InfoManager.GetCostaInfo();
        int num = 2;
        StartDialog(view, info, num);
    }


    public void StartDialog(View view, Hashtable<String, String> info, int num)
    {
        // Create data values
        TextView name, dest, description, price, start_end;
        ImageView img;

        // Create dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.info_cruise_dialog);

        // Insert values
        name = dialog.findViewById(R.id.name_info_cruise_dialog);
        dest = dialog.findViewById(R.id.destinations_info_cruise_dialog);
        description = dialog.findViewById(R.id.description_info_cruise_dialog);
        price = dialog.findViewById(R.id.price_info_cruise_dialog);
        start_end = dialog.findViewById(R.id.start_end_cruise_dialog);
        img = dialog.findViewById(R.id.info_cruise_dialog_image);
        // Set values
        name.setText(info.get("name"));
        dest.setText(info.get("destinations"));
        description.setText(info.get("description"));
        price.setText("Starting at " + info.get("price") + "$");
        start_end.setText("From " + info.get("startPoint") + " to " + info.get("endPoint"));
        if (num == 1) {
            img.setImageResource(R.drawable.america_cruise_1);
        } else {
            img.setImageResource(R.drawable.america_cruise_2);
        }
        // Close button
        Button dialogButton = (Button) dialog.findViewById(R.id.close_info_cruise_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Buy button
        Button dialogBuyButton = (Button) dialog.findViewById(R.id.order_info_cruise_dialog);
        dialogBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { BuyPageActivity(v, info); }
        });

        dialog.show();
    }

    public void BuyPageActivity(View view, Hashtable<String, String> info)
    {
        // Intent to Buy page with User details and Cruise
        Intent intent = new Intent(this, BuyPage.class);

        // Pass the user Object
        intent.putExtra("user", user);

        // Pass the cruise Data
        intent.putExtra("cruise", info);

        // Start
        startActivity(intent);
    }
    // End Dialog
}