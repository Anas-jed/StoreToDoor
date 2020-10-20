package com.example.storetodoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MessengersLocation extends AppCompatActivity {
    private List<MessengerPOJO> messengerPOJOList;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messengers_location);
        final RecyclerView recyclerView = findViewById(R.id.activity_messengers_location_recyclerView);

        firebaseFirestore = FirebaseFirestore.getInstance();
        messengerPOJOList = new ArrayList<>();
        //setting layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        firebaseFirestore.collection("messenger").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        MessengerPOJO messengerPOJO = documentSnapshot.toObject(MessengerPOJO.class);
                        messengerPOJOList.add(messengerPOJO);
                    }
                    recyclerView.setAdapter(new MessengersLocationAdapter(MessengersLocation.this,messengerPOJOList));
                } else {
                    Log.d("MessengersLocation","Exception Here");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("MessengersLocation","Exception Here 1");
            }
        });


    }//Oncreate end
}//Main end
