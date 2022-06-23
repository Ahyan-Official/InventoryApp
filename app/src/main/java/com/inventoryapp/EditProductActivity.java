package com.inventoryapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class EditProductActivity extends AppCompatActivity {


    ImageView image;
    Button save;

    EditText name,price,stock,barcode,category;
    String item_barcode,item_category,item_name,item_price,item_stock,item_image;

    DatabaseReference databaseReference;
    DatabaseReference databaseReferencecat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        //Top action bar
        ActionBar a = getSupportActionBar();
        a.setTitle("Edit Product");
        a.setDisplayHomeAsUpEnabled(true);


        //init Views
        save =findViewById(R.id.save);
        image =findViewById(R.id.image);
        name =findViewById(R.id.name);
        price =findViewById(R.id.price);
        stock =findViewById(R.id.stock);
        barcode =findViewById(R.id.barcode);
        category =findViewById(R.id.category);

        //getting data from intent

        item_barcode = getIntent().getStringExtra("item_barcode");
        item_category = getIntent().getStringExtra("item_category");
        item_name = getIntent().getStringExtra("item_name");
        item_price = getIntent().getStringExtra("item_price");
        item_stock = getIntent().getStringExtra("item_stock");
        item_image = getIntent().getStringExtra("item_image");


        //load product image
        Picasso.get().load(item_image).into(image);

        //set data in fields
        name.setText(item_name);
        price.setText(item_price);
        stock.setText(item_stock);
        category.setText(item_category);
        barcode.setText(item_barcode);


        //Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReferencecat = FirebaseDatabase.getInstance().getReference("Users");

        //get current user email
        String finaluser= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String resultemail = finaluser.replace(".","");


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //check fields and add products
                if(!TextUtils.isEmpty(name.getText().toString())&&!TextUtils.isEmpty(price.getText().toString())&&!TextUtils.isEmpty(stock.getText().toString())){

                    //add product to database
                    Products items = new Products(name.getText().toString(),category.getText().toString(),price.getText().toString(),barcode.getText().toString(),stock.getText().toString(),item_image);
                    databaseReference.child(resultemail).child("Items").child(item_barcode).setValue(items);
                    databaseReferencecat.child(resultemail).child("ItemByCategory").child(item_category).child(item_barcode).setValue(items);

                    Toast.makeText(EditProductActivity.this,name.getText().toString()+" Added",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(EditProductActivity.this,"Please Fill all the fields",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}