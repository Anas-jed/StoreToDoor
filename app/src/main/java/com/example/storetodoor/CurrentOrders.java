package com.example.storetodoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.PendingResults;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;

public class CurrentOrders extends AppCompatActivity {
    private List<CurrentOrdersPOJO> currentOrdersPOJOList;
    private ProgressBar progressBar;
    private List<OrderPOJO> orderPOJOList;
    private FirebaseFirestore firebaseFirestore;
    private String DocId, Area, cname, pname;
    private OrderPOJO orderPOJO;
    private CurrentOrdersPOJO currentOrdersPOJO;
    private int count = -1, count1 = -1, i;
    private RecyclerView recyclerView;
    private List<String> customerName, productName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_orders);

        //Current User Unique ID
        DocId = getIntent().getStringExtra("DocumentId");
        Area = getIntent().getStringExtra("Area");
        Log.w("ReachedCurrentOrders", "Doc Id: " + DocId + "\nArea: " + Area);

        currentOrdersPOJOList = new ArrayList<>();
        customerName = new ArrayList<String>();
        productName = new ArrayList<String>();
        orderPOJOList = new ArrayList<>();
        progressBar = findViewById(R.id.activity_current_order_progressBar);

        recyclerView = findViewById(R.id.activity_current_order_recyclerView);
        firebaseFirestore = FirebaseFirestore.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        firebaseFirestore.collection("order")
                .whereEqualTo("area", Area)
                .whereEqualTo("status", "active")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(final QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            progressBar.setVisibility(View.VISIBLE);
                            for (final QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                count++;
                            }

                            for (final QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {

                                orderPOJO = queryDocumentSnapshot.toObject(OrderPOJO.class);
                                orderPOJOList.add(orderPOJO);

                                count1++;

                                if (count == count1) {
                                    new gettingCustomerName().execute();
                                    //getProductName();
                                }

                            }//endof for loop

                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(CurrentOrders.this, "No Orders in Your Area", Toast.LENGTH_SHORT).show();
                            Log.d("CurrentOrder", "Exception Here");
                        }

                    }

                });
    }//endofOnCreate

    public class gettingCustomerName extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            //Getting Customer Name from ID
            for (i = 0; i <= count; i++) {
                try {
                    Task<DocumentSnapshot> documentSnapshotTask = firebaseFirestore.collection("customer")
                            .document(orderPOJOList.get(i).getC_uid()).get();

                    DocumentSnapshot documentSnapshot = Tasks.await(documentSnapshotTask);

                    if (documentSnapshot.exists()) {
                        cname = documentSnapshot.getString("name");
                        customerName.add(cname);

                    } else {
                        Log.d("Print", "Document Exists failed  ... " + documentSnapshot.exists());
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception handledException) {
                    Log.d("Exception", "Handled Exception" + handledException);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }//end of for loop

            //Product Getting Name
            for (i = 0; i <= count; i++) {
                try {
                    Task<DocumentSnapshot> documentSnapshotTask = firebaseFirestore.collection("product")
                            .document(orderPOJOList.get(i).getP_docid()).get();

                    DocumentSnapshot documentSnapshot = Tasks.await(documentSnapshotTask);
                    if (documentSnapshot.exists()) {
                        pname = documentSnapshot.getString("name");
                        productName.add(pname);
                    }
                } catch (Exception handledException) {
                    Log.d("Exception", "Handled Exception" + handledException);
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            for (int i = 0; i <= count; i++) {
                currentOrdersPOJO = new CurrentOrdersPOJO(customerName.get(i), orderPOJOList.get(i).getComplete_address(),
                        productName.get(i), orderPOJOList.get(i).getQuantity(), orderPOJOList.get(i).getStatus(), orderPOJOList.get(i).getArea(), orderPOJOList.get(i).getO_date());
                currentOrdersPOJOList.add(currentOrdersPOJO);
            }
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setAdapter(new CurrentOrdersAdapter(currentOrdersPOJOList, CurrentOrders.this));

        }
    }//endofclass
}
