package com.example.denicruise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.denicruise.Classes.AppUser;
import static com.example.denicruise.Classes.Constants.*;

import com.example.denicruise.Classes.AppUser;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText etEmail, etPassword;
    SharedPreferences sharedPreferences;
    AppUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Check if user connected
        sharedPreferences = getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(EMAIL_FIELD))
        {
            user = new AppUser(sharedPreferences.getString(FULLNAME_FIELD, ""), sharedPreferences.getString(EMAIL_FIELD, ""),
                    sharedPreferences.getString(PHONE_FIELD, ""), sharedPreferences.getString(PASSWORD_FIELD, ""));
            // redirect to home
            // Intent to HomePage with User details
            Intent intent = new Intent(this, HomePage.class);

            // Pass the user Object
            intent.putExtra("user", user);

            // Start
            startActivity(intent);
        }

        etEmail = findViewById(R.id.log_in_email_input);
        etPassword = findViewById(R.id.log_in_password_input);

        // Hide the menu
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    public void LogInActivity(View view)
    {

        // Get the data from the form
        String FormEmail = etEmail.getText().toString();
        String FormPassword = etPassword.getText().toString();

        // Make sure that the fields are not empty
        if (FormEmail.length() == 0)
        {
            etEmail.setError("Email is required.");
            return;
        } else if (FormPassword.length() == 0)
        {
            etPassword.setError("Password is required.");
            return;
        }


        // Get all users
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
           @Override
           public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if (task.isSuccessful()) {
                   // Check value
                   boolean userExist = false;
                   Map<String, Object> user_data = null;

                   for (QueryDocumentSnapshot document : task.getResult()) {
                       // Check if email exist on db
                       if(FormEmail.equals(document.getData().get(EMAIL_FIELD)))
                       {
                           userExist = true;
                           user_data = document.getData();
                           break;
                       }
                   }

                   if (userExist)
                   {
                       // Check if the password is right
                       if (FormPassword.equals(user_data.get(PASSWORD_FIELD)))
                       {
                           // Create the user Object and create intent to HomePage
                           String fullname = (String) user_data.get(FULLNAME_FIELD);
                           String email = (String) user_data.get(EMAIL_FIELD);
                           String phone = (String) user_data.get(PHONE_FIELD);
                           String password = (String) user_data.get(PASSWORD_FIELD);
                           // Insert to sharedPreference
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(FULLNAME_FIELD, fullname);
                            editor.putString(EMAIL_FIELD, email);
                            editor.putString(PHONE_FIELD, phone);
                            editor.putString(PASSWORD_FIELD, password);
                            editor.commit();
                            user = new AppUser(fullname, email, phone, password);

                           // Start the intent
                           HomePageActivity(view);

                       } else {
                           etPassword.setError("The password doesn't match.");
                       }

                   } else {
                       etEmail.setError("The email doesn't exist.");
                   }
               } else {
                   Toast.makeText(getApplicationContext(),"An error has occurred, try again later.",Toast.LENGTH_LONG).show();
               }
           }
        });
    }

    public void HomePageActivity(View view) {
        // Intent to HomePage with User details
        Intent intent = new Intent(this, HomePage.class);

        // Pass the user Object
        intent.putExtra("user", user);

        // Start
        startActivity(intent);
    }

    public void SignUpActivity(View view) {
        Intent intent = new Intent(this, SignUpPage.class);
        startActivity(intent);
    }
}