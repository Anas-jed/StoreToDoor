package com.example.storetodoor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ManagerPanel extends AppCompatActivity {
    private Button addProduct, addMessenger, messengersLocation,orderHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_panel);

        addMessenger = findViewById(R.id.activity_managerpanel_addmessenger);
        addProduct = findViewById(R.id.activity_managerpanel_addProduct);
        messengersLocation = findViewById(R.id.activity_managerpanel_messengerslocation);
        orderHistory = findViewById(R.id.activity_managerpanel_orderhistory);

        addMessenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerPanel.this, MessengerRegistration.class);
                startActivity(intent);
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerPanel.this, ProductRegistration.class);
                startActivity(intent);
            }
        });

        messengersLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerPanel.this, MessengersLocation.class);
                startActivity(intent);
            }
        });

        orderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerPanel.this, OrderHistory.class);
                startActivity(intent);
            }
        });
    }
}
