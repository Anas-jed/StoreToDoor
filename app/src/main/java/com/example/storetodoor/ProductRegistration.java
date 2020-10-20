package com.example.storetodoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProductRegistration extends AppCompatActivity {
    private EditText editText, editText2;
    private Button insertButton;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_registration);

        editText = findViewById(R.id.activity_productregistration_edittext);
        editText2 = findViewById(R.id.activity_productregistration_edittext2);
        insertButton = findViewById(R.id.activity_productregistration_insertbutton);

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String productName = editText.getText().toString();
                String Price = editText2.getText().toString();

                int productPrice = 0;
                productPrice = Integer.parseInt(Price);

                if (!productName.isEmpty() && productPrice != 0) {

                    ProductPOJO productPOJO = new ProductPOJO(productName, productPrice);
                    firebaseFirestore = FirebaseFirestore.getInstance();
                    firebaseFirestore.collection("product").add(productPOJO).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            hideSoftKeyboard(ProductRegistration.this);
                            editText.getText().clear();
                            editText2.getText().clear();
                            Toast.makeText(getApplicationContext(), "Insertion Successful",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Insertion Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "All Fields Must Be Filled",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
