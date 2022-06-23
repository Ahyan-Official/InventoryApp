package com.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeleteProductsActivity extends AppCompatActivity {


    public static TextView resultdeleteview;
    private FirebaseAuth firebaseAuth;
    Button scantodelete, deletebtn;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product);
        firebaseAuth = FirebaseAuth.getInstance();


        //Action bar to set text
        ActionBar a = getSupportActionBar();
        a.setTitle("Delete Product");
        a.setDisplayHomeAsUpEnabled(true);

        // database refernece
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // init Views
        resultdeleteview = findViewById(R.id.barcodedelete);
        scantodelete = findViewById(R.id.buttonscandelete);
        deletebtn= findViewById(R.id.deleteItemToTheDatabasebtn);


        // open qr/bar code scan activity
        scantodelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ScanCodeActivityDel.class));
            }
        });

        //delete button
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

    }

    public void delete()
    {
        // get product code
        String deletebarcodevalue = resultdeleteview.getText().toString();

        //get current user email
        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser=users.getEmail();
        String resultemail = finaluser.replace(".","");

        // check code is not null
        if(!TextUtils.isEmpty(deletebarcodevalue)){

            //delete product/item
            databaseReference.child(resultemail).child("Items").child(deletebarcodevalue).removeValue();
            Toast.makeText(DeleteProductsActivity.this,"Item is Deleted",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(DeleteProductsActivity.this,"Please scan Barcode",Toast.LENGTH_SHORT).show();
        }
    }
}
