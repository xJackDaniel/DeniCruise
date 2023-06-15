package com.example.denicruise;

import static com.example.denicruise.Classes.Constants.CRUISE_DATE_FIELD;
import static com.example.denicruise.Classes.Constants.CRUISE_NAME_FIELD;
import static com.example.denicruise.Classes.Constants.EMAIL_FIELD;
import static com.example.denicruise.Classes.Constants.FILENAME;
import static com.example.denicruise.Classes.Constants.FULLNAME_FIELD;
import static com.example.denicruise.Classes.Constants.PASSWORD_FIELD;
import static com.example.denicruise.Classes.Constants.PHONE_FIELD;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.denicruise.Classes.CustomAdapter;
import com.example.denicruise.Classes.DeniCruiseMenu;
import com.example.denicruise.SignUpPage;

import com.example.denicruise.Classes.AppUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ProfilePage extends AppCompatActivity {
    DeniCruiseMenu MyMenu;
    AppUser app_user;
    EditText etFullName, etEmail, etPhone, etPassword;
    ImageView profile_img;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences sharedPreferences;
    // one boolean variable to check whether all the text fields
    // are filled by the user, properly or not.
    boolean isAllFieldsChecked = false;
    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;
    int CAMERA_PIC_REQUEST = 1888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        // Get the user
        app_user = (AppUser) getIntent().getSerializableExtra("user");

        // SharedPreference
        sharedPreferences = getSharedPreferences(FILENAME, Context.MODE_PRIVATE);

        // Get fields
        etFullName = findViewById(R.id.NameProfilePage);
        etEmail = findViewById(R.id.EmailProfilePage);
        etPassword = findViewById(R.id.PswProfilePage);
        etPhone = findViewById(R.id.PhoneProfilePage);

        // Insert data to form
        etFullName.setText(app_user.getFullname());
        etEmail.setText(app_user.getEmail());
        etPassword.setText(app_user.getPassword());
        etPhone.setText(app_user.getPhone());

        // Create Menu Object
        MyMenu = new DeniCruiseMenu(this, app_user);

        // on click on the image profile
        profile_img = (ImageView) findViewById(R.id.ProfileImage);
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { StartDialog(v); }
        });

        // Set image profile
//        setUserProfile(app_user.getPhone(), profile_img);
        loadImageFromStorage(app_user.getPhone());

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


    public void ClearForm(View view) {
        // Insert data to form
        etFullName.setText(app_user.getFullname());
        etEmail.setText(app_user.getEmail());
        etPassword.setText(app_user.getPassword());
        etPhone.setText(app_user.getPhone());
    }

    public void UpdateProfile(View view)
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
                            Context context = getApplicationContext();
                            CharSequence text = "Saved Successfully!";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                            app_user = new AppUser(fullname, email, phone, password);

                            // Update sharedPreference
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(FULLNAME_FIELD, fullname);
                            editor.putString(EMAIL_FIELD, email);
                            editor.putString(PHONE_FIELD, phone);
                            editor.putString(PASSWORD_FIELD, password);
                            editor.commit();

                            // Finish activity
                            HomePageActivity(view);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Context context = getApplicationContext();
                            CharSequence text = "An error has occurred.";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                            finish();
                        }
                    });
        }

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

    public void HomePageActivity(View view)
    {
        // Intent to Home page with User details
        Intent intent = new Intent(this, HomePage.class);

        // Pass the user Object
        intent.putExtra("user", app_user);

        // Start
        startActivity(intent);
    }

    public void StartDialog(View view)
    {
        // Create data values
        TextView name, dest, description, price, start_end;
        ImageView img;

        // Create dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.image_select_dialog);

        // Gallery button
        Button dialogButton = (Button) dialog.findViewById(R.id.gallery_button);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
                dialog.dismiss();
            }
        });
        // Camera button
        Button dialogBuyButton = (Button) dialog.findViewById(R.id.camera_button);
        dialogBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    // this function is triggered when
    // the Select Image Button is clicked
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    profile_img.setImageURI(selectedImageUri);


                    // Save the image uri
                    Uri imageUri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        String path = saveToInternalStorage(bitmap, app_user.getPhone());
                        db.collection("users").document(app_user.getPhone())
                                .update("image_path", path);
                    } catch (IOException e) {
                        Toast toast = Toast.makeText(getApplicationContext(), "An error occurred.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }

            if (requestCode == CAMERA_PIC_REQUEST) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                profile_img.setImageBitmap(image);

                // Save the image uri
                String path = saveToInternalStorage(image, app_user.getPhone());
                db.collection("users").document(app_user.getPhone())
                       .update("image_path", path);

            }

            Toast toast = Toast.makeText(getApplicationContext(), "Profile image successfully updated.", Toast.LENGTH_SHORT);
            toast.show();
        }


    }
    // End Dialog

    private String saveToInternalStorage(Bitmap bitmapImage, String phone){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        String filename = phone + ".jpg";
        File mypath=new File(directory,filename);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            Toast toast = Toast.makeText(getApplicationContext(), "An error occurred.", Toast.LENGTH_SHORT);
            toast.show();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                Toast toast = Toast.makeText(getApplicationContext(), "An error occurred.", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        return directory.getAbsolutePath();
    }

    private void loadImageFromStorage(String phone)
    {
        DocumentReference docRef = db.collection("users").document(phone);
        final String[] profile_path = new String[1];
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String path = (String) document.get("image_path");
                        if (path != null)
                        {
                            profile_path[0] = path;

                        }
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "An error occurred.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "An error occurred.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        // Wait 5 seconds to update user's cruises_images
        Toast toast = Toast.makeText(getApplicationContext(), "Loading image...", Toast.LENGTH_LONG);
        toast.show();

        new Handler().postDelayed(new Runnable() {
            // Using handler with postDelayed called runnable run method
            @Override
            public void run() {
                try {
                    if (profile_path[0] != null) {
                        // Set the image
                        String file_name = phone + ".jpg";
                        File f = new File(profile_path[0], file_name);
                        Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                        profile_img.setImageBitmap(b);
                    }
                }
                catch (FileNotFoundException e)
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "An error occurred.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }, 5*1000); // wait for 5 seconds


    }
}