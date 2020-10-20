package com.example.storetodoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessengerRegistration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText editText, editText2, editText3;
    private Spinner areaSpinner;
    private Button registerButton, locationButton;
    private String name, email, password, area;
    private Double latitude = null, longitude = null;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private GeoPoint geoPoint;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private ArrayList<String> areaName;
    private ProgressDialog pDialog;
    private AreaPOJO areaPOJO;


    Map<String,String> areadoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger_registration);

        editText = findViewById(R.id.messenger_registration_editText);
        editText2 = findViewById(R.id.messenger_registration_editText2);
        editText3 = findViewById(R.id.messenger_registration_editText3);

        areaSpinner = findViewById(R.id.messenger_registration_area_spinner);

        locationButton = findViewById(R.id.messenger_registration_location_button);
        registerButton = findViewById(R.id.messenger_registration_register_button);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        areaName = new ArrayList<>();
        areadoc = new HashMap<String,String>();
        new Area().execute();
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettinglocation();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerMessenger();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        area = parent.getItemAtPosition(position).toString();
        Toast.makeText(MessengerRegistration.this, "Selected Value is : " + area, Toast.LENGTH_LONG).show();
        Log.d("Selected", "Selected Value is : " + area);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(MessengerRegistration.this, "Nothing Selected", Toast.LENGTH_LONG).show();
        Log.d("Selected", "Nothing Selected");
    }

    public String coordinatestoAddress(double latitude, double longitude) {
        String TotalAddress = "";
        try {
            Geocoder geocoder = new Geocoder(MessengerRegistration.this);
            List<Address> addressList;
            addressList = geocoder.getFromLocation(latitude, longitude, 1);
            TotalAddress = addressList.get(0).getAddressLine(0);
            return TotalAddress;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Issue in getting Address From Coordinates", Toast.LENGTH_SHORT).show();
            Log.d("Tag", "Issue in getting Address From Coordinates \t" + e);
        }
        return TotalAddress;
    }


    public void registerMessenger() {

        name = editText.getText().toString();
        email = editText2.getText().toString();
        password = editText3.getText().toString();
        Log.d("Tags", "Name: " + name.isEmpty() + "\nEmail: " + email.isEmpty() + "\nPassword: " + password.isEmpty());

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || area.isEmpty() ) {
            Toast.makeText(getApplicationContext(), "All Fields Must Be Filled", Toast.LENGTH_SHORT).show();
        }
        else if( password.length() < 6){
            Toast.makeText(getApplicationContext(), "Password must not be less than 6 Characters", Toast.LENGTH_SHORT).show();
        }
        else if (latitude == null || longitude == null) {
            Toast.makeText(getApplicationContext(), "Get Location First By Clicking 'Get Current Location'", Toast.LENGTH_SHORT).show();
        } else {
            try {
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Tag", "signInWithEmail:success");
                            geoPoint = new GeoPoint(latitude, longitude);
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            String coordinatestoaddress = coordinatestoAddress(latitude, longitude);

                            Log.d("Tags", "UId: " + firebaseUser.getUid() + "\nEmail: " + firebaseUser.getEmail());
                            MessengerPOJO messengerPOJO = new MessengerPOJO(firebaseUser.getUid(), name, email, password, area, coordinatestoaddress, geoPoint);
                            AreaPOJO areaPOJO = new AreaPOJO(firebaseUser.getUid(), area);
                            
                            DocumentReference documentReference = firebaseFirestore.collection("area").document(areadoc.get(area));
                            documentReference.update("uid", firebaseUser.getUid()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {


                                    areaName.clear();
                                    new Area().execute();
                                    editText.setText("");
                                    editText2.setText("");
                                    editText3.setText("");
                                    latitude = null;
                                    longitude = null;

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Not","Listen Failure "+ e);
                                    Toast.makeText(getApplicationContext(), "doc reference Failed",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });

                            firebaseFirestore.collection("messenger")
                                    .add(messengerPOJO)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Registration Successful",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Error", "createUserWithEmail:failure -------------- " + e);
                                    Toast.makeText(getApplicationContext(), "Registration Failed",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Log.w("Error", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Firebase User Creating Issues", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Try Catch Handled Exception", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void gettinglocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(MessengerRegistration.this);
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MessengerRegistration.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MessengerRegistration.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new AlertDialog.Builder(this)
                        .setTitle("Required Location Permission")
                        .setMessage("To Get Location, You must Allow Permission")
                        .setPositiveButton("Ok, Allow", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MessengerRegistration.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create()
                        .show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MessengerRegistration.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                Log.d("langitude", "Latitude: " + latitude + "\nLongitude" + longitude);
                                Toast.makeText(MessengerRegistration.this, "We have got location", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }//end of gettinglocationFunction

    public void getArea() {
        try {
            CollectionReference collectionReference = firebaseFirestore.collection("area");
            final Query areaQuery = collectionReference.whereEqualTo("uid", "null");
            areaQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            String string = documentSnapshot.getString("area_name");
                            String string1 = documentSnapshot.getId();
                            areaName.add(string);
                            areadoc.put(string,string1);
                            Log.d("Strings", "String: " + areadoc.get(string));
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(MessengerRegistration.this,
                                android.R.layout.simple_spinner_item, areaName);

                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        areaSpinner.setAdapter(adapter);
                        areaSpinner.setOnItemSelectedListener(MessengerRegistration.this);

                    } else {
                        Log.d("Stop", "it stops here");
                    }
                }
            });
        } catch (Exception e) {
            Log.d("Shit", "Message: " + e);
        }
    }//end of getArea Function


    public class Area extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            getArea();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
