package com.example.storetodoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MessengerPanel extends AppCompatActivity {
    private Button updateLocation, viewCurrentOrders, deliverOrders;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    Double Latitude = null, Longitude = null;
    private String DocId, Area;
    ProgressBar progressBar;
    private GeoPoint geoPoint;
    private Boolean aBoolean = false;
    private FirebaseFirestore firebaseFirestore;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger_panel);

        updateLocation = findViewById(R.id.activity_messengerpanel_updateLocation);
        viewCurrentOrders = findViewById(R.id.activity_messengerpanel_viewCurrentOrder);
        deliverOrders = findViewById(R.id.activity_messengerpanel_deliverOrder);
        progressBar = findViewById(R.id.activity_messengerpanel_progressIcon);
        firebaseFirestore = FirebaseFirestore.getInstance();


        //Current User Unique ID
        DocId = getIntent().getStringExtra("DocumentId");
        Area = getIntent().getStringExtra("Area");

        Log.w("ReachedMessengerPanel", "Doc Id: " + DocId + "\nArea: " + Area);

        updateLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aBoolean = true;
                fetchLocation();
            }
        });

        viewCurrentOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessengerPanel.this, CurrentOrders.class);
                intent.putExtra("DocumentId", DocId);
                intent.putExtra("Area", Area);
                startActivity(intent);
            }
        });

        deliverOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchLocation();
            }
        });
    }

    public void updatingDoc() {
        if (Latitude != null && Longitude != null) {
            geoPoint = new GeoPoint(Latitude, Longitude);

            DocumentReference documentReference = firebaseFirestore.collection("messenger").document(DocId);
            documentReference.update("geoPoint", geoPoint).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    Toast.makeText(MessengerPanel.this, "Location is Updated", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    Toast.makeText(MessengerPanel.this, "Location Updation Failed", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(MessengerPanel.this, "Latitude & Longitude is null", Toast.LENGTH_SHORT).show();
        }
        aBoolean = false;
    }

    public void deliverOrder(double latitude, double longitude) {
        String Sublocality;
        Sublocality = coordinatestoAddress(latitude, longitude);
        Log.w("Print", "Reached Here 71");
        if (Sublocality.equals(Area)) {
            Log.w("Print", "Reached Here 72");

            firebaseFirestore.collection("order")
                    .whereEqualTo("status", "active")
                    .whereEqualTo("area", Area)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                    String document = queryDocumentSnapshot.getId();
                                    DocumentReference documentReference = firebaseFirestore.collection("order").document(document);

                                    documentReference
                                            .update("status", "completed")
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                                                     Toast.makeText(getApplicationContext(), "All Orders are Delivered in this Area", Toast.LENGTH_SHORT).show();
                                                    Log.d("DeliverOrder", "Order status updated");
                                                }
                                            });

                                }
                            } else {
                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "No Order To Deliver", Toast.LENGTH_SHORT).show();
                                Log.d("MessengerPanel", "Exception Here 1");
                            }
                        }
                    });
        } else {
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            Log.w("Print", "Reached Here 73");
            Toast.makeText(getApplicationContext(), "Not in a Deliver Zone", Toast.LENGTH_SHORT).show();
            Log.d("MessengerPanel", "Exception Here 12");
        }
        aBoolean = false;
    }


    public String coordinatestoAddress(double latitude, double longitude) {
        String SubLocality = "";
        try {
            Geocoder geocoder = new Geocoder(MessengerPanel.this);
            List<Address> addressList;
            addressList = geocoder.getFromLocation(latitude, longitude, 1);
            SubLocality = addressList.get(0).getSubLocality();

            return SubLocality;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Issue in getting Address From Coordinates", Toast.LENGTH_SHORT).show();
            Log.d("Tag", "Issue in getting Address From Coordinates \t" + e);
        }

        return SubLocality;
    }

    public void fetchLocation() {
        try {
            progressBar.setVisibility(ProgressBar.VISIBLE);
            Log.w("Print", "Reached Here 22");
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(MessengerPanel.this);
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(MessengerPanel.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(MessengerPanel.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                    new AlertDialog.Builder(this)
                            .setTitle("Required Location Permission")
                            .setMessage("To Use this Feature, You must have to Allow Permission")
                            .setPositiveButton("Ok, Allow", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    ActivityCompat.requestPermissions(MessengerPanel.this,
                                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressBar.setVisibility(ProgressBar.INVISIBLE);
                            dialog.dismiss();
                        }
                    }).create()
                            .show();
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(MessengerPanel.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                Log.w("Print", "Reached Here 63");

                // Permission has already been granted
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                Log.w("Print", "Reached Here ");

                                if (location != null) {

                                    // Logic to handle location object
                                    Latitude = location.getLatitude();
                                    Longitude = location.getLongitude();
                                    Log.w("Print", "Reached Here 25");
                                    Log.w("langitude", "Latitude: " + Latitude + "\nLongitude" + Longitude);
                                    //Toast.makeText(MessengerPanel.this, "We have got location", Toast.LENGTH_SHORT).show();
                                    if (aBoolean) {
                                        Log.w("Print", "Reached Here 26");
                                        updatingDoc();
                                    } else {
                                        Log.w("Print", "Reached Here 27");
                                        deliverOrder(Latitude, Longitude);
                                    }
                                } else {
                                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                                    Toast.makeText(MessengerPanel.this, "Location Problem", Toast.LENGTH_SHORT).show();
                                    Log.w("Print", "The Location Is: " + location);

                                    locationRequest = LocationRequest.create();
                                    locationRequest.setNumUpdates(1);
                                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                    locationRequest.setInterval(0);
                                    Log.w("Print", "Reached Here 64");
                                    locationCallback = new LocationCallback(){
                                        @Override
                                        public void onLocationResult(LocationResult locationResult) {
                                            Log.w("Print", "Reached Here 65");
                                            if(locationResult != null)
                                            {
                                                Log.w("Print", "Reached Here 66");
                                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                                                Location location = locationResult.getLastLocation();
                                                Log.d("GettingLocation","Latitude: "+location.getLatitude()+
                                                        "Longitude"+location.getLongitude());
                                                Toast.makeText(MessengerPanel.this, "Got Location", Toast.LENGTH_SHORT).show();

                                            }
                                            else
                                            {
                                                Log.w("Print", "Reached Here 67");
                                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                                                Toast.makeText(MessengerPanel.this, "Location Problem", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onLocationAvailability(LocationAvailability locationAvailability) {
                                            if (locationAvailability.isLocationAvailable()) {
                                                Log.w("Print", "Reached Here 68");
                                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                                                Toast.makeText(MessengerPanel.this, "New Location is available", Toast.LENGTH_SHORT).show();

                                            } else
                                            {
                                                Log.w("Print", "Reached Here 69");
                                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                                                Toast.makeText(MessengerPanel.this, "Location not available right now", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    };
                                }
                            }
                        });
            }
        } catch (Exception any) {
            Toast.makeText(MessengerPanel.this, "Exception Problem", Toast.LENGTH_SHORT).show();
            Log.w("Print", "Exception Here " + any);
        }
    }//end of fetchLocation function

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MessengerPanel.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MessengerPanel.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MessengerPanel.this,MainActivity.class);
        startActivity(intent);
    }
}
