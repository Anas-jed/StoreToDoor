package com.example.storetodoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.StructuredQuery;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PlaceOrder extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final int MY_PERMISSIONS_REQUEST_INTERNET = 1;
    private EditText productQuantityEditText, completeAddressEditText;
    private Spinner areaSpinner, productSpinner;
    private Button placeOrderButton;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private String area, product, DocId, prodQuantity, compAddress, status = "active";
    Map<String, String> areadoc, productdoc;
    private ArrayList<String> areaName, productName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        DocId = getIntent().getStringExtra("DocumentId");
        Log.d("DocId", "Doc ID :  " + DocId);

        productQuantityEditText = findViewById(R.id.activity_placeOrder_quantity);
        completeAddressEditText = findViewById(R.id.activity_placeOrder_completeAddress);

        areaSpinner = findViewById(R.id.activity_placeOrder_areaSpinner);
        productSpinner = findViewById(R.id.activity_placeOrder_productSpinner);

        placeOrderButton = findViewById(R.id.activity_placeOrder_placeOrderButton);
        areaName = new ArrayList<>();
        productName = new ArrayList<>();
        areadoc = new HashMap<String, String>();
        productdoc = new HashMap<String, String>();
        firebaseFirestore = FirebaseFirestore.getInstance();

        gettingInternet();

        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });


    }

    public void placeOrder() {
        prodQuantity = productQuantityEditText.getText().toString();
        compAddress = completeAddressEditText.getText().toString();
        try {
            if (prodQuantity.isEmpty() || compAddress.isEmpty() || area.isEmpty() || product.isEmpty() || DocId.isEmpty() || status.isEmpty()) {
                Toast.makeText(getApplicationContext(), "All Fields Must Be Filled", Toast.LENGTH_SHORT).show();
                Log.d("Print", "I was in here");
            } else {

                Log.d("Print", "I am in here");
                OrderPOJO orderPOJO = new OrderPOJO(DocId, compAddress, productdoc.get(product), prodQuantity, status, area, new Timestamp(new Date()));
                firebaseFirestore.collection("order").add(orderPOJO).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        hideSoftKeyboard(PlaceOrder.this);
                        productQuantityEditText.getText().clear();
                        completeAddressEditText.getText().clear();
                        Toast.makeText(getApplicationContext(), "Order placed Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PlaceOrder.this,CustomerPanel.class);
                        intent.putExtra("DocumentId",DocId);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("OrderFailed", "Exception : " + e);
                        Toast.makeText(getApplicationContext(), "Order placed Failed", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        } catch (Exception e) {
            Log.d("OrderFailed", "This Exception : " + e);
            Toast.makeText(getApplicationContext(), "Order placed Failed", Toast.LENGTH_SHORT).show();
        }

    }//end of function PlaceOrder

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void getProduct() {

        firebaseFirestore.collection("product").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String string = documentSnapshot.getString("name");
                        String string1 = documentSnapshot.getId();
                        productName.add(string);
                        productdoc.put(string, string1);
                        Log.d("Strings", "String: " + productdoc.get(string));
                    }

                    ArrayAdapter<String> productadapter = new ArrayAdapter<>(PlaceOrder.this,
                            android.R.layout.simple_spinner_item, productName);

                    productadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    productSpinner.setAdapter(productadapter);
                    productSpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) PlaceOrder.this);

                } else {
                    Log.d("Stop", "Product Query Result is Empty");
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Shit", "On Failure ProductName Function : " + e);
            }
        });

    }

    public void getArea() {
        try {
            firebaseFirestore.collection("area")
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String temp = "null";
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                            String string2 = documentSnapshot.getString("uid");
                            if (!temp.equals(string2)) {
                                String string = documentSnapshot.getString("area_name");
                                String string1 = documentSnapshot.getId();
                                areaName.add(string);
                                areadoc.put(string, string1);
                                Log.d("Strings", "String: " + areadoc.get(string));
                            } else {
                                Log.d("NotMatch", "Area was not associated with Uid" + string2);
                            }
                        }

                        ArrayAdapter<String> areaadapter = new ArrayAdapter<>(PlaceOrder.this,
                                android.R.layout.simple_spinner_item, areaName);

                        areaadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        areaSpinner.setAdapter(areaadapter);
                        areaSpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) PlaceOrder.this);
                    } else {
                        Log.d("StopAreaQuery", "Area Query Result is Empty");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Shittyyyy", "On Failure AreaName Function : " + e);
                }
            });

        } catch (Exception e) {
            Log.d("Shitttyyy", "Message: " + e);
        }
    }//end of getArea Function

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getId() == R.id.activity_placeOrder_areaSpinner) {
            area = parent.getItemAtPosition(position).toString();
            Toast.makeText(PlaceOrder.this, "Selected Value is : " + area, Toast.LENGTH_LONG).show();
            Log.d("Selected", "Selected Value is : " + area);
        }
        if (parent.getId() == R.id.activity_placeOrder_productSpinner) {
            product = parent.getItemAtPosition(position).toString();
            Toast.makeText(PlaceOrder.this, "Selected Value is : " + product, Toast.LENGTH_LONG).show();
            Log.d("Selected", "Selected Value is : " + product);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

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

    public class Product extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            getProduct();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public void gettingInternet() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(PlaceOrder.this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(PlaceOrder.this,
                    Manifest.permission.INTERNET)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new AlertDialog.Builder(this)
                        .setTitle("Required Internet Permission")
                        .setMessage("You must Allow Permission To perform this")
                        .setPositiveButton("Ok, Allow", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(PlaceOrder.this,
                                        new String[]{Manifest.permission.INTERNET}, MY_PERMISSIONS_REQUEST_INTERNET);
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
                ActivityCompat.requestPermissions(PlaceOrder.this,
                        new String[]{Manifest.permission.INTERNET},
                        MY_PERMISSIONS_REQUEST_INTERNET);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            new Product().execute();
            new Area().execute();
        }
    }//end of gettinglocationFunction
}
