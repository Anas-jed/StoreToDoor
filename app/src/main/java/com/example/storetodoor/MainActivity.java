package com.example.storetodoor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    String email, pass;
    TextView textView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private EditText editText1, editText2;
    private Button button;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    ProgressBar progressBar;
    CustomerPOJO customerPOJO;
    MessengerPOJO messengerPOJO;
    Intent intent;
    FirebaseUser firebaseUser;


    /*public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressIcon);
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = firebaseFirestore.getInstance();
        editText1 = findViewById(R.id.email);
        editText2 = findViewById(R.id.password);
        button = findViewById(R.id.activity_main_loginButton);
        textView = findViewById(R.id.activity_main_signUp);
        radioGroup = findViewById(R.id.activity_main_radioGroup);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(MainActivity.this);
                progressBar.setVisibility(ProgressBar.VISIBLE);
                email = editText1.getText().toString();
                pass = editText2.getText().toString();
                int selectedId = radioGroup.getCheckedRadioButtonId();

                if (!email.isEmpty() && !pass.isEmpty() && selectedId != -1) {

                    radioButton = findViewById(selectedId);
                    final String selected = radioButton.getText().toString();
                    Log.d("Log", "a value selected" + selected);

                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                Log.d("Tag", "signInWithEmail:success");

                                firebaseUser = mAuth.getCurrentUser();

                                if (selected.equals("Manager")) {

                                    CollectionReference collectionReference = firebaseFirestore.collection("manager");
                                    Query managerQuery = collectionReference.whereEqualTo("uid", firebaseUser.getUid());

                                    managerQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot querySnapshot) {
                                            if (!querySnapshot.isEmpty()) {
                                                intent = new Intent(MainActivity.this, ManagerPanel.class);
                                                for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshot) {
                                                    intent.putExtra("DocumentId", queryDocumentSnapshot.getId());
                                                }
                                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                                                editText1.getText().clear();
                                                editText2.getText().clear();
                                                radioGroup.clearCheck();
                                                startActivity(intent);
                                            } else {
                                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                                                Toast.makeText(getApplicationContext(), "Id not Found", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });

                                } else if (selected.equals("Customer")) {

                                    CollectionReference collectionReference = firebaseFirestore.collection("customer");
                                    Query customerQuery = collectionReference.whereEqualTo("uid", firebaseUser.getUid());

                                    customerQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot querySnapshot) {
                                            if (!querySnapshot.isEmpty()) {
                                                intent = new Intent(MainActivity.this, CustomerPanel.class);
                                                for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshot) {
                                                    customerPOJO = queryDocumentSnapshot.toObject(CustomerPOJO.class);
                                                    intent.putExtra("DocumentId", queryDocumentSnapshot.getId());
                                                }
                                                System.out.println("Customer UID: " + customerPOJO.getUid());
                                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                                                editText1.getText().clear();
                                                editText2.getText().clear();
                                                radioGroup.clearCheck();
                                                startActivity(intent);
                                            } else {
                                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                                                Toast.makeText(getApplicationContext(), "Id not Found", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("QueryFailed", "Exception : " + e);
                                        }
                                    });

                                } else if (selected.equals("Messenger")) {

                                    CollectionReference collectionReference = firebaseFirestore.collection("messenger");
                                    Query messengerQuery = collectionReference.whereEqualTo("uid", firebaseUser.getUid());

                                    messengerQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot querySnapshot) {
                                            if (!querySnapshot.isEmpty()) {
                                                intent = new Intent(MainActivity.this, MessengerPanel.class);
                                                for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                                                    messengerPOJO = documentSnapshot.toObject(MessengerPOJO.class);
                                                    intent.putExtra("DocumentId", documentSnapshot.getId());
                                                    intent.putExtra("Area", messengerPOJO.getArea());
                                                }

                                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                                                editText1.getText().clear();
                                                editText2.getText().clear();
                                                radioGroup.clearCheck();
                                                startActivity(intent);
                                            } else {
                                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                                                Log.d("Tag", "signInWithMessenger:data:Failed");
                                                Toast.makeText(getApplicationContext(), "Id not Found",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                                }
                            } else {
                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                                Log.w("Tag", "signInWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "All Fields Must Be Filled",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup_intent = new Intent(MainActivity.this, CustomerRegistration.class);
                startActivity(signup_intent);
            }
        });

    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }
        catch (NullPointerException nullexception)
        {
            Log.d("Exception", "Exception Handled" + nullexception);
        }
    }
}



