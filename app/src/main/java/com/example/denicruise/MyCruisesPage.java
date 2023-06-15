package com.example.denicruise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.denicruise.Classes.Constants.*;

import static java.util.Map.entry;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.denicruise.Classes.AppUser;
import com.example.denicruise.Classes.CustomAdapter;
import com.example.denicruise.Classes.DeniCruiseMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCruisesPage extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DeniCruiseMenu MyMenu;
    AppUser user;
    ListView simpleList;
    Map<String, Integer> drawble_cruises_images = Map.ofEntries(entry("royal cruise", R.drawable.europe_cruise_1), entry("carnival cruise", R.drawable.europe_cruise_1),
            entry("mystical cruise", R.drawable.global_cruise_1), entry("moon cruise", R.drawable.global_cruise_2), entry("west cruise", R.drawable.america_cruise_2), entry("costa cruise", R.drawable.america_cruise_2));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the user
        user = (AppUser) getIntent().getSerializableExtra("user");

        // Create Menu Object
        MyMenu = new DeniCruiseMenu(MyCruisesPage.this, user);

        // Get user cruises_images
        GetCruises(user.getPhone());

        // Create animation
        setContentView(R.layout.activity_splash_screen);

        ImageView img = findViewById(R.id.SplashScreenImage);
        Animation slideAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_slide);

        img.startAnimation(slideAnimation);


        // Wait 5 seconds to update user's cruises_images
        new Handler().postDelayed(new Runnable() {
            // Using handler with postDelayed called runnable run method
            @Override
            public void run() {
                setContentView(R.layout.activity_my_cruises_page);
                // Get user's cruises
                List<Map<String, String>> cruises = user.getCruises();
                // Create cruise_names list
                List<String> cruises_names = new ArrayList<String>();
                List<Integer> cruises_images = new ArrayList<Integer>();
                List<String> cruises_dates = new ArrayList<String>();
                for (Map<String, String> name : cruises) {
                    cruises_names.add(name.get(CRUISE_NAME_FIELD));
                    cruises_images.add(drawble_cruises_images.get(name.get(CRUISE_NAME_FIELD).toString().toLowerCase()));
                    cruises_dates.add(name.get(CRUISE_DATE_FIELD));
                }
                System.out.println(cruises_names + " " + cruises_images);
                // Create view list
                simpleList = (ListView) findViewById(R.id.simpleListView);
                CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), cruises_names, cruises_images, cruises_dates);
                simpleList.setAdapter(customAdapter);

            }

        }, 5*1000); // wait for 5 seconds


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

    // Get user's cruises_images
    public void GetCruises(String phone)
    {
        // Count of cruises_images
        final int[] count = {0};
        final List<Map<String, String>> cruises = new ArrayList<Map<String, String>>();
        db.collection("cruises")
                .whereEqualTo("phone", phone)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HashMap<String, Object> data = (HashMap<String, Object>) document.getData();
                                Map<String, String> cruise = new HashMap<String, String>();
                                cruise.put(CRUISE_DATE_FIELD, data.get(CRUISE_DATE_FIELD).toString());
                                cruise.put(CRUISE_INSURANCE_FIELD, data.get(CRUISE_INSURANCE_FIELD).toString());
                                cruise.put(CRUISE_SUITE_FIELD, data.get(CRUISE_SUITE_FIELD).toString());
                                cruise.put(CRUISE_NAME_FIELD, data.get(CRUISE_NAME_FIELD).toString());
                                cruise.put(CRUISE_PHONE_FIELD, data.get(CRUISE_PHONE_FIELD).toString());
                                cruise.put(CRUISE_PRICE_FIELD, data.get(CRUISE_PRICE_FIELD).toString());
                                // Add to list
                                cruises.add(count[0], cruise);
                                // Add 1 to count
                                count[0] += 1;
                            }
                            // Set cruises
                            user.setCruises(cruises);

                        } else {
                            Toast.makeText(getApplicationContext(),"An error has occurred, try again later.",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}