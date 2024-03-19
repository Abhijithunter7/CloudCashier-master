package com.appsnipp.cloudcashier;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class transport_form extends AppCompatActivity {

    private ProgressBar progressBar;
    private DatabaseReference monthlyTargetRef;

    private EditText editTitle;
    private Spinner optionsSpinner;
    private EditText editPrice;
    private EditText editNote;
    private Button saveButton;
    private Button showDatePickerButton,graph,pastexpense;
    private DatePicker fooddatePicker;
    private String noteId;
    private ImageView backgroundImageView;

    private static final int PICK_IMAGE_REQUEST = 1;
    private DatabaseReference backgroundRef;

    private FirebaseAuth auth;
    private FirebaseDatabase mdatabase;
    private DatabaseReference transportRef;

    // Create an instance of MyFirebaseMessagingService
    private MyFirebaseMessagingService messagingService = new MyFirebaseMessagingService();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_form);

        // Start the service to handle FCM messages
        startService(new Intent(this, MyFirebaseMessagingService.class));
        // Initialize Firebase Messaging Serv

        // Call sendNotification method



        // Initialize Firebase Database reference
        auth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance();
        FirebaseUser ccurrentUser = auth.getCurrentUser();
        if (ccurrentUser != null) {
            String userid = ccurrentUser.getUid();
            transportRef = mdatabase.getReference().child(userid).child("transport");
            backgroundRef = mdatabase.getReference().child(userid).child("backgroundURL");
            monthlyTargetRef = mdatabase.getReference().child(userid).child("monthlyTarget");



            backgroundRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String backgroundImageURL = snapshot.getValue(String.class);
                        if (backgroundImageURL != null && !backgroundImageURL.isEmpty()) {
                            // Set the background image using Glide or any other image loading library
                            Glide.with(transport_form.this)
                                    .load(backgroundImageURL)
                                    .into(backgroundImageView);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("FirebaseError", "Failed to read background URL.", error.toException());
                }
            });

            // Set OnClickListener for custom TextView
            TextView customTextView = findViewById(R.id.transportcustom);
            customTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Open image selection intent
                    openImageChooser();
                }
            });

            // Initialize progress bar
            progressBar = findViewById(R.id.progressBar);
            progressBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSetTargetDialog();
                }
            });

            // Fetch and display total price spent
            fetchTotalPriceSpent();



        }

        // Initialize UI elements
        editTitle = findViewById(R.id.transporttitleEditText);
        optionsSpinner = findViewById(R.id.transportoptionsSpinner);
        editPrice = findViewById(R.id.transportpriceEditText);
        editNote = findViewById(R.id.transportnoteEditText);
        saveButton = findViewById(R.id.transportsaveButton);
        showDatePickerButton = findViewById(R.id.showDatePickerButton);
        fooddatePicker = findViewById(R.id.transportdatePicker);
        graph = findViewById(R.id.graphtransport);
        pastexpense = findViewById(R.id.transportpasterxpence);
        backgroundImageView = findViewById(R.id.backgroundImageView);



        // Populate the Spinner with food options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.options_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionsSpinner.setAdapter(adapter);

        // Retrieve noteId from intent
        Intent intent = getIntent();
        if (intent != null) {
            noteId = intent.getStringExtra("noteId");
            if (noteId != null) {
                // Fetch data for the given noteId from Firebase and populate the form fields
                fetchDataFromFirebase(noteId);
            }
        }

        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(transport_form.this, transport_graph.class));
            }
        });

        pastexpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(transport_form.this,transportrecy.class));

            }
        });

        // Set an OnClickListener for the Save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values entered by the user
                String title = editTitle.getText().toString();
                String option = optionsSpinner.getSelectedItem().toString();
                String priceStr = editPrice.getText().toString();
                String note = editNote.getText().toString();
                String selectedDate = getSelectedDate();

                // Check if any field is empty
                if (title.isEmpty() || priceStr.isEmpty() || note.isEmpty() || selectedDate.isEmpty()) {
                    Toast.makeText(transport_form.this, "All fields are compulsory", Toast.LENGTH_SHORT).show();
                    return;
                }

                double price;
                if (!priceStr.trim().isEmpty()) { // Check if priceStr is not empty or contains only whitespace
                    try {
                        price = Double.parseDouble(priceStr);
                    } catch (NumberFormatException e) {
                        Toast.makeText(transport_form.this, "Invalid price format", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(transport_form.this, "Price cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save the data to Firebase
                if (noteId != null) {
                    // Update the existing data in Firebase
                    updateDataInFirebase(title, option, price, note, selectedDate);
                } else {
                    // Generate a unique ID for the entry
                    String entryId = transportRef.push().getKey();

                    FirebaseUser user = auth.getCurrentUser();
                    String userName = user != null ? user.getDisplayName() : "Unknown User";

                    // Create a new object to represent the data
                    TransportData foodda = new TransportData(entryId, title, option, price, note, selectedDate);

                    // Save the data to Firebase
                    transportRef.child(entryId).setValue(foodda);

                    // Clear the form fields
                    clearForm();

                    // Show a message or perform other actions if needed
                    String message = "Data saved to Firebase!";
                    Toast.makeText(transport_form.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set an OnClickListener for the Show Date Picker button
        showDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the DatePicker
                fooddatePicker.setVisibility(View.VISIBLE);
                showDatePicker();
            }
        });
    }

    private void fetchDataFromFirebase(String noteId) {
        DatabaseReference noteRef = transportRef.child(noteId);
        noteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    TransportData transportData = snapshot.getValue(TransportData.class);
                    if (transportData != null) {
                        // Populate the form fields with existing data
                        editTitle.setText(transportData.getTitle());
                        // Set the selected item in the Spinner
                        ArrayAdapter adapter = (ArrayAdapter) optionsSpinner.getAdapter();
                        int position = adapter.getPosition(transportData.getselectedOption());
                        optionsSpinner.setSelection(position);
                        editPrice.setText(String.valueOf(transportData.getPrice()));
                        editNote.setText(transportData.getNote());
                        // Parse and set the date in the DatePicker
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        try {
                            Date date = sdf.parse(transportData.getselectedDate());
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            fooddatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to read value.", error.toException());
            }
        });
    }

    private void showSetTargetDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_set_target, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextTarget = dialogView.findViewById(R.id.editTextTarget);

        dialogBuilder.setTitle("Set Monthly Target");
        dialogBuilder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String targetAmount = editTextTarget.getText().toString();
                if (!TextUtils.isEmpty(targetAmount)) {
                    saveMonthlyTarget(Double.parseDouble(targetAmount));
                } else {
                    Toast.makeText(transport_form.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancelled.
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void saveMonthlyTarget(double targetAmount) {
        monthlyTargetRef.setValue(targetAmount);
        Toast.makeText(this, "Monthly target set successfully", Toast.LENGTH_SHORT).show();
    }

    private void fetchTotalPriceSpent() {
        transportRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double totalPrice = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TransportData transportData = snapshot.getValue(TransportData.class);
                    if (transportData != null) {
                        totalPrice += transportData.getPrice();
                    }
                }
                updateProgressBar(totalPrice);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled
            }
        });
    }

    private void updateProgressBar(double totalPrice) {
        TextView textViewProgress = findViewById(R.id.textViewProgress);
        final double[] monthlyTarget = {0}; // Default value

        monthlyTargetRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    monthlyTarget[0] = snapshot.getValue(Double.class);
                    if (monthlyTarget[0] == 0) {
                        textViewProgress.setText("Set Monthly Target");
                    } else {
                        double progress = (totalPrice / monthlyTarget[0]) * 100;
                        progressBar.setProgress((int) progress);
                        textViewProgress.setText(String.format(Locale.getDefault(), "%.2f%%", progress));

                        // Check if progress is 50% or above
                        if (progress >= 50) {
                            // Send a notification
                            sendProgressNotification();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    private void sendProgressNotification() {
        String title = "Expense Reached 50%";
        String message = "You have spent 50% or more of your monthly target.";
        // Send notification using FirebaseMessagingService

    }


    private void clearForm() {
        editTitle.setText("");
        optionsSpinner.setSelection(0); // Reset spinner selection to the first item
        editPrice.setText("");
        editNote.setText("");
        // Clear the DatePicker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        fooddatePicker.init(year, month, day, null);
    }
    // Method to open image chooser intent
    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    // Handle the result of image selection intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the request code is the same as defined above and if the result is OK
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the Uri of the selected image
            Uri filePath = data.getData();

            // Upload the image to Firebase Storage and set it as background
            uploadImageToFirebaseAndSetBackground(filePath);
        }
    }

    private void uploadImageToFirebaseAndSetBackground(Uri filePath) {
        if (filePath != null) {
            // Get reference to Firebase Storage
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();

            // Create a reference to "backgrounds" folder and specify the file name
            StorageReference backgroundRef = storageRef.child("backgrounds/background.jpg");

            // Upload the file to Firebase Storage
            backgroundRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get the download URL of the uploaded file
                            backgroundRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Save the image URL to Realtime Database for persistence
                                    saveImageURLToRealtimeDatabase(uri.toString());
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                    Toast.makeText(transport_form.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle any errors
                            Toast.makeText(transport_form.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    // Save image URL to Firebase Realtime Database
    private void saveImageURLToRealtimeDatabase(String imageURL) {
        // Set the background image URL in the Realtime Database
        backgroundRef.setValue(imageURL)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Background image URL saved successfully
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                        Toast.makeText(transport_form.this, "Failed to save background URL", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void updateDataInFirebase(String title, String option, double price, String note, String selectedDate) {

        // Create a new object to represent the updated data
        TransportData transportData = new TransportData(noteId, title, option, price, note, selectedDate);

        // Update the data in Firebase
        transportRef.child(noteId).setValue(transportData);

        // Show a message or perform other actions if needed
        String message = "Data updated in Firebase!";
        Toast.makeText(transport_form.this, message, Toast.LENGTH_SHORT).show();
    }


    private void showDatePicker() {
        // Get the current date
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog with future dates disabled
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Set the selected date in the DatePicker (only if it's not in the future)
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, monthOfYear, dayOfMonth);
                        if (!selectedDate.after(Calendar.getInstance())) {
                            fooddatePicker.init(year, monthOfYear, dayOfMonth, null);
                        } else {
                            Toast.makeText(transport_form.this, "Please select a date today or before", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, year, month, day);

        // Disable future dates
        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());

        datePickerDialog.show();
    }

    private String getSelectedDate() {
        // Get the selected date from the DatePicker
        int day = fooddatePicker.getDayOfMonth();
        int month = fooddatePicker.getMonth();
        int year = fooddatePicker.getYear();

        // Create a formatted date string
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return sdf.format(calendar.getTime());
    }
}
