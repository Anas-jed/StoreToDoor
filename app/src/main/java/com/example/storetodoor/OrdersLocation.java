package com.example.storetodoor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class OrdersLocation extends AppCompatActivity {
    private String DocId;
    private FirebaseFirestore firebaseFirestore;
    private OrderPOJO orderPOJO;
    private List<OrderPOJO> orderPOJOList;
    private int count = -1, count1 = -1,i;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_location);

        progressBar = findViewById(R.id.activity_orders_location_ProgressBar);

        DocId = getIntent().getStringExtra("DocumentId");
        Log.d("DocId", "Doc ID :  " + DocId);

        firebaseFirestore.collection("order")
                .whereEqualTo("c_uid", DocId)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {

                    for (final QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                        count++;
                    }

                    for (final QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {

                        orderPOJO = queryDocumentSnapshot.toObject(OrderPOJO.class);
                        orderPOJOList.add(orderPOJO);

                        count1++;

                        if (count == count1) {
                            //new gettingMessenger().execute();
                        }
                    }

                } else {

                    Toast.makeText(OrdersLocation.this, "No Orders Completed Yet", Toast.LENGTH_SHORT).show();
                    Log.d("OrderHistory", "Exception Here");
                }
            }
        });

    }//end of Oncreate

   /* public class gettingMessenger extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            //Getting Customer Name from ID
            for (i = 0; i <= count; i++) {
                try {
                    Task<DocumentSnapshot> documentSnapshotTask = firebaseFirestore.collection("messenger")
                            .whereEqualTo("area",orderPOJOList.get(i).getArea()).get();

                    QuerySnapshot querySnapshot = Tasks.await(querySnapshotTask);

                    if (!querySnapshot.isEmpty()) {


                        *//*cname = querySnapshot.getString("name");
                        customerName.add(cname);*//*

                    } else {
                        Log.d("Print", "Document Exists failed  ... " + querySnapshot.isEmpty());
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception handledException) {
                    Log.d("Exception", "Handled Exception" + handledException);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }//end of for loop
            return null;
        }
    }*/
}//end of Main Class
