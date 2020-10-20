package com.example.storetodoor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CustomerRegistration extends AppCompatActivity {
    private EditText editText, editText2, editText3;
    private Button register_button;
    String name, email, password;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_registration);

        editText = findViewById(R.id.customer_registration_editText);
        editText2 = findViewById(R.id.customer_registration_editText2);
        editText3 = findViewById(R.id.customer_registration_editText3);
        register_button = findViewById(R.id.customer_registration_register_button);
        mAuth = FirebaseAuth.getInstance();

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    public void registerUser(){

        name = editText.getText().toString();
        email = editText2.getText().toString();
        password = editText3.getText().toString();

        if (name.isEmpty() && email.isEmpty() && password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "All Fields Must Be Filled", Toast.LENGTH_SHORT).show();
        } else {
            try {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {


                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                    CustomerPOJO customerPOJO = new CustomerPOJO(firebaseUser.getUid(), name, email, password);

                                    firebaseFirestore = FirebaseFirestore.getInstance();

                                    firebaseFirestore.collection("customer").add(customerPOJO).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(), "Registration Successful",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "Registration Failed",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                } else {

                                    Log.w("log", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }

                                // ...
                            }
                        });
            }catch (Exception e)
            {
                Log.d("log","Handled Exception"+ e );
            }
        }
    }
}
