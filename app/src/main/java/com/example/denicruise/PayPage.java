package com.example.denicruise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.denicruise.Classes.AppUser;
import com.example.denicruise.Classes.CardValidator;
import com.example.denicruise.Classes.DeniCruiseMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.WriteResult;

import java.util.HashMap;

public class PayPage extends AppCompatActivity {
    DeniCruiseMenu MyMenu;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    AppUser user;
    EditText cardNumber, cardDate, cardCvv;
    HashMap<String, String> cruise;
    CardValidator validator;
    boolean valid_number, valid_date, valid_cvv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_page);
        // Get the user
        user = (AppUser) getIntent().getSerializableExtra("user");
        cruise = (HashMap<String, String>) getIntent().getSerializableExtra("cruise");
        // Create Menu Object
        MyMenu = new DeniCruiseMenu(this, user);


        // Insert cruise data into fields
        // Create data values
        TextView name, price;
        // Insert values
        name = findViewById(R.id.pay_page_cruise_name);
        price = findViewById(R.id.pay_page_total);
        // Set values
        name.setText(cruise.get("name") + " - " + cruise.get("date"));
        price.setText("Total to pay: " + cruise.get("price") +"$");

        // Reset values
        valid_number = false;
        valid_date = false;
        valid_cvv = false;

        // Create validator
        validator = new CardValidator();

        // Get fields
        cardNumber =findViewById(R.id.pay_page_card_number);
        cardDate = findViewById(R.id.pay_page_card_date);
        cardCvv = findViewById(R.id.pay_page_card_cvv);

        // Card Number validator
        cardNumber.addTextChangedListener(new TextWatcher() {

            private static final int TOTAL_SYMBOLS = 19; // size of pattern 0000-0000-0000-0000
            private static final int TOTAL_DIGITS = 16; // max numbers of digits in pattern: 0000 x 4
            private static final int DIVIDER_MODULO = 5; // means divider position is every 5th symbol beginning with 1
            private static final int DIVIDER_POSITION = DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
            private static final char DIVIDER = '-';

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // noop
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // noop
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isInputCorrectCardNumber(s, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                    s.replace(0, s.length(), buildCorrectString(getDigitArray(s, TOTAL_DIGITS), DIVIDER_POSITION, DIVIDER));
                    valid_number = false;
                } else {
                    valid_number = true;
                }
            }

            private boolean isInputCorrectCardNumber(Editable s, int totalSymbols, int dividerModulo, char divider) {
                boolean isCorrect = s.length() <= totalSymbols; // check size of entered string
                for (int i = 0; i < s.length(); i++) { // check that every element is right
                    if (i > 0 && (i + 1) % dividerModulo == 0) {
                        isCorrect &= divider == s.charAt(i);
                    } else {
                        isCorrect &= Character.isDigit(s.charAt(i));
                    }
                }
                return isCorrect;
            }

            private String buildCorrectString(char[] digits, int dividerPosition, char divider) {
                final StringBuilder formatted = new StringBuilder();

                for (int i = 0; i < digits.length; i++) {
                    if (digits[i] != 0) {
                        formatted.append(digits[i]);
                        if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                            formatted.append(divider);
                        }
                    }
                }

                return formatted.toString();
            }

            private char[] getDigitArray(final Editable s, final int size) {
                char[] digits = new char[size];
                int index = 0;
                for (int i = 0; i < s.length() && index < size; i++) {
                    char current = s.charAt(i);
                    if (Character.isDigit(current)) {
                        digits[index] = current;
                        index++;
                    }
                }
                return digits;
            }
        });

        // Card Date validator
        cardDate.addTextChangedListener(new TextWatcher() {
            int before , after;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                before = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // noop
            }

            @Override
            public void afterTextChanged(Editable s) {
                after = s.length();
                if (after == 2 && before == 1) {
                    s.replace(0, s.length(), s + "/");
                } else if(s.length() == 6)
                {
                    s.replace(0, s.length(), s.toString().substring(0, s.length() - 1));
                }
            }
        });

        // Card CVV validator
        cardCvv.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // noop
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // noop
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 4)
                {
                    s.replace(0, s.length(), s.toString().substring(0, s.length() - 1));
                }
            }
        });

        // add notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("Cruise Notification", "Cruise Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    // Pay function
    public void PayCruise(View view) {
        // Validate form
        String number = cardNumber.getText().toString().replace("-", "");
        if (valid_number || (number.length() == 16 && validator.isNumeric(number))) {
            // check date
            String date = cardDate.getText().toString();
            if (date.length() == 5) {
                String[] dateSplited = date.split("/", 2);
                if (validator.validateExpiryDate(dateSplited[0], dateSplited[1])) {
                    String cvv = cardCvv.getText().toString();
                    if (validator.validateCVV(cvv)) {
                        saveCruise(view, cruise, user.getPhone());
                        // Sent notification
                        addNotification(cruise);
                    } else {
                        cardCvv.setError("Enter a valid CVV.");
                    }
                } else {
                    cardDate.setError("Enter a valid date.");
                }
            } else {
                cardDate.setError("Enter a valid date.");
            }
        } else {
            cardNumber.setError("Enter a valid number");
        }
    }


    // save cruise
    private void saveCruise(View v, HashMap<String, String> cruise, String phone)
    {
        // Add phone field to cruise
        cruise.put("phone", user.getPhone());
        // Enter the data to the database
        // Enter the data to the database
        db.collection("cruises").add(cruise).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                MyCruisesActivity(v);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Make a toast
                Context context = getApplicationContext();
                CharSequence text = "Failed!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }
        });

    }


    // MyCruises Activity
    public void MyCruisesActivity(View view) {
        // Make a toast
        Context context = getApplicationContext();
        CharSequence text = "Completed!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        // Intent to MyCruisesPage page with User details
        Intent intent = new Intent(this, MyCruisesPage.class);

        // Pass the user Object
        intent.putExtra("user", user);

        // Start
        startActivity(intent);
    }


    private void addNotification(HashMap<String, String> cruise) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Cruise Notification");
        builder.setContentTitle("Your cruise was successfully booked!");
        builder.setContentText(cruise.get("name") + " - " + cruise.get("date"));
        builder.setSmallIcon(R.drawable.check_icon);
        builder.setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(1, builder.build());
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
}

