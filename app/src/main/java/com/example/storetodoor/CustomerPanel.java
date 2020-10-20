package com.example.storetodoor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class CustomerPanel extends AppCompatActivity {
    String DocId;
    Button placeOrder, viewOrdersLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_panel);


        placeOrder = findViewById(R.id.button);
        //viewOrdersLocation = findViewById(R.id.button2);

        DocId = getIntent().getStringExtra("DocumentId");

        Log.d("Reached", "Reached in Customer Panel Doc Id: " + DocId);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerPanel.this, PlaceOrder.class);
                intent.putExtra("DocumentId", DocId);
                startActivity(intent);
            }
        });

       /* viewOrdersLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerPanel.this, OrdersLocation.class);
                intent.putExtra("DocumentId", DocId);
                startActivity(intent);
            }
        });*/

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CustomerPanel.this,MainActivity.class);
        startActivity(intent);
    }
}
