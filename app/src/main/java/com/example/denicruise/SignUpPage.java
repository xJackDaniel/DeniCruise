package com.example.denicruise;

import static com.example.denicruise.Classes.Constants.EMAIL_FIELD;
import static com.example.denicruise.Classes.Constants.FILENAME;
import static com.example.denicruise.Classes.Constants.FULLNAME_FIELD;
import static com.example.denicruise.Classes.Constants.PASSWORD_FIELD;
import static com.example.denicruise.Classes.Constants.PHONE_FIELD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.example.denicruise.Classes.AppUser;
import com.example.denicruise.Classes.DeniCruiseMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SignUpPage extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText etFullName, etEmail, etPhone, etPassword;

    // one boolean variable to check whether all the text fields
    // are filled by the user, properly or not.
    boolean isAllFieldsChecked = false;

    boolean phoneValid = true;

    AppUser user_app;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        etFullName = findViewById(R.id.register_name_input);
        etEmail = findViewById(R.id.register_email_input);
        etPhone = findViewById(R.id.register_phone_input);
        etPassword = findViewById(R.id.register_password_input);

        // Get sharedPreference
        sharedPreferences = getSharedPreferences(FILENAME, Context.MODE_PRIVATE);

    }

    public void RegisterUser(View view)
    {

        // store the returned value of the dedicated function which checks
        // whether the entered data is valid or if any fields are left blank.
        isAllFieldsChecked = CheckAllFields();

        // the boolean variable turns to be true then
        // only the user must be proceed to the activity2
        if (isAllFieldsChecked) {


            // Create the fields
            String fullname = etFullName.getText().toString();
            String email = etEmail.getText().toString();
            String phone = etPhone.getText().toString();
            String password = etPassword.getText().toString();

            Map<String, String> user = new HashMap<>();
            user.put(FULLNAME_FIELD, fullname);
            user.put(EMAIL_FIELD, email);
            user.put(PHONE_FIELD, phone);
            user.put(PASSWORD_FIELD, password);

            // Enter the data to the database
            db.collection("users").document(phone)
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            user_app = new AppUser(fullname, email, phone, password);
                            // Insert data to sharedPreference
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(FULLNAME_FIELD, fullname);
                            editor.putString(EMAIL_FIELD, email);
                            editor.putString(PHONE_FIELD, phone);
                            editor.putString(PASSWORD_FIELD, password);
                            editor.commit();
                            // Redirect to HomePage
                            HomeActivity(view);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            SignUpActivity(view);
                        }
                    });
        }


    }

    public void HomeActivity(View view) {
        Intent intent = new Intent(this, HomePage.class);

        // Pass the user Object
        intent.putExtra("user", user_app);

        startActivity(intent);
    }

    public void SignUpActivity(View view) {
        Intent intent = new Intent(this, SignUpPage.class);
        startActivity(intent);
    }


    // function which checks all the text fields
    // are filled or not by the user.
    // when user clicks on the PROCEED button
    // this function is triggered.
    private boolean CheckAllFields() {
        if (etFullName.length() == 0) {
            etFullName.setError("Fullname is required");
            return false;
        }
        else if (!etFullName.getText().toString().contains(" ")) {
            etFullName.setError("You must enter your full name");
            return false;
        }
        if (etEmail.length() == 0) {
            etEmail.setError("Email is required");
            return false;
        }

        // Validate the email
        String regexPattern = "^(.+)@(\\S+)$";
        if(!Pattern.compile(regexPattern).matcher(etEmail.getText().toString()).matches()) {
            etEmail.setError("Enter a valid email");
            return false;
        }

        // Check that the phone doesn't exist on db
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String phoneForm = etPhone.getText().toString();
                    String phoneData;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        phoneData = document.getId();
                        if (phoneData.equals(phoneForm))
                        {
                            phoneValid = false;
                            break;
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"An error has occurred, try again later.",Toast.LENGTH_LONG).show();
                }
            }
        });

        // Check phone
        if (!phoneValid) {
            etPhone.setError("This phone number is already exist");
            return false;
        }
        else if (etPhone.length() == 0) {
            etPhone.setError("Phone is required");
            return false;
        }
        else if(!(isNumeric(etPhone.getText().toString()) && (etPhone.length() == 9 || etPhone.length() == 10)))
        {
            etPhone.setError("Enter a valid phone");
            return false;
        }

        if (etPassword.length() == 0) {
            etPassword.setError("Password is required");
            return false;
        }
        else if (etPassword.length() < 8) {
            etPassword.setError("Password must be minimum 8 characters");
            return false;
        }

        // after all validation return true.
        return true;
    }


    // Check if string is build from numbers
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


}