package com.example.denicruise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.denicruise.Classes.AppUser;
import com.example.denicruise.Classes.DeniCruiseMenu;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.CompositeDateValidator;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;

public class BuyPage extends AppCompatActivity {
    DeniCruiseMenu MyMenu;
    AppUser user;
    HashMap<String, String> cruise;
    private boolean ifSelectedDate = false;
    private Button mPickDateButton;
    private String datePicked;
    private int price_to_pay;
    private boolean isInsurance = false, isSuite = false;
    private EditText mShowSelectedDateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_page);
        // Get the user and cruise data
        user = (AppUser) getIntent().getSerializableExtra("user");
        cruise = (HashMap<String, String>) getIntent().getSerializableExtra("cruise");
        // Create Menu Object
        MyMenu = new DeniCruiseMenu(this, user);

        // Insert cruise data into fields
        // Create data values
        TextView name, dest, price;
        // Addons
        final int[] price_int = {Integer.parseInt(cruise.get("price"))};
        final int suite_price = price_int[0]/5;
        final int insurance_price = price_int[0]/10;
        // Update global price;
        price_to_pay = price_int[0];


        // Insert values
        name = findViewById(R.id.buy_page_name);
        dest = findViewById(R.id.buy_page_destinations);
        price = findViewById(R.id.buy_page_price);
        // Set values
        name.setText(cruise.get("name"));
        dest.setText("Destiniations:" + cruise.get("destinations"));


        // Date picker values
        mPickDateButton = findViewById(R.id.pick_date_button);
        mShowSelectedDateText = findViewById(R.id.show_selected_date);

        // DatePicker Material
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        // Set dates
        CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();

        //....define min and max for example with LocalDateTime and ZonedDateTime or Calendar
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        max.add(Calendar.MONTH, 1);


        CalendarConstraints.DateValidator dateValidatorMin = DateValidatorPointForward.from(min.getTimeInMillis());
        CalendarConstraints.DateValidator dateValidatorMax = DateValidatorPointBackward.before(max.getTimeInMillis());

        ArrayList<CalendarConstraints.DateValidator> listValidators =
                new ArrayList<CalendarConstraints.DateValidator>();
        listValidators.add(dateValidatorMin);
        listValidators.add(dateValidatorMax);
        CalendarConstraints.DateValidator validators = CompositeDateValidator.allOf(listValidators);
        constraintsBuilderRange.setValidator(validators);

        // Set the validator
        materialDateBuilder.setCalendarConstraints(constraintsBuilderRange.build());

        // Set title
        materialDateBuilder.setTitleText("SELECT A DATE");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();





        // handle select date button which opens the
        // material design date picker
        mPickDateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });

        // now handle the positive button click from the
        // material design date picker
        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {

                        // if the user clicks on the positive
                        // button that is ok button update the
                        // selected date
                        mShowSelectedDateText.setText("Selected Date is : " + materialDatePicker.getHeaderText());
                        ifSelectedDate = true;
                        datePicked = materialDatePicker.getHeaderText();
                        // in the above statement, getHeaderText
                        // is the selected date preview from the
                        // dialog
                    }
                });

        // Switch handling
        CompoundButton suite, insurance;
        suite = findViewById(R.id.suite_switch_buy_page);
        insurance = findViewById(R.id.insurance_switch_buy_page);

        suite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    price_int[0] = price_int[0] + suite_price;
                    // set price at the end
                    price.setText("Total to pay:" + price_int[0] + "$");

                } else
                {
                    price_int[0] = price_int[0] - suite_price;
                    // set price at the end
                    price.setText("Total to pay:" + price_int[0] + "$");
                }
                // Update global price;
                isSuite = isChecked;
                price_to_pay = price_int[0];
            }
        });

        insurance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    price_int[0] = price_int[0] + insurance_price;
                    // set price at the end
                    price.setText("Total to pay:" + price_int[0] + "$");

                } else
                {
                    price_int[0] = price_int[0] - insurance_price;
                    // set price at the end
                    price.setText("Total to pay:" + price_int[0] + "$");
                }
                // Update global price;
                isInsurance = isChecked;
                price_to_pay = price_int[0];
            }
        });

        // set price at the end
        price.setText("Total to pay:" + price_int[0] + "$");
    }

    public void BuyCruise(View view) {
        // Check if date selected
        if (!ifSelectedDate)
        {
            mShowSelectedDateText.setError("Select a date first.");
        } else {
            // Intent to HomePage with User details
            Intent intent = new Intent(this, PayPage.class);

            // Pass the user Object
            intent.putExtra("user", user);
            // Pass what to pay - Create hashmap and pass it
            // creating a info Dictionary
            HashMap<String, String> info = new HashMap<String, String>();

            // Insert info
            info.put("name", cruise.get("name"));
            info.put("date", datePicked);
            info.put("price", Integer.toString(price_to_pay));
            info.put("isSuite", String.valueOf(isSuite));
            info.put("isInsurance", String.valueOf(isInsurance));
            // Pass the info to pay
            intent.putExtra("cruise", info);


            // Start
            startActivity(intent);
        }
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