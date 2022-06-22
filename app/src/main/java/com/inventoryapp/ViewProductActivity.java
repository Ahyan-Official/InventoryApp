package com.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class ViewProductActivity extends AppCompatActivity {

    ImageView image;
    TextView  name,price,stock,barcode,description,category;
    String item_barcode,item_category,item_name,item_price,item_stock,item_image;
    Button edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);


        //Top action bar

        ActionBar a = getSupportActionBar();
        a.setTitle("Product");
        a.setDisplayHomeAsUpEnabled(true);


        //init Views
        edit =findViewById(R.id.edit);
        image =findViewById(R.id.image);
        name =findViewById(R.id.name);
        price =findViewById(R.id.price);
        stock =findViewById(R.id.stock);
        barcode =findViewById(R.id.barcode);
        description =findViewById(R.id.description);
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


        //edit button
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Intent to move to next activity
                Intent intent = new Intent(getApplicationContext(),EditProductActivity.class);
                intent.putExtra("item_barcode",item_barcode);
                intent.putExtra("item_category",item_category);
                intent.putExtra("item_name",item_name);
                intent.putExtra("item_price",item_price);
                intent.putExtra("item_stock",item_stock);
                intent.putExtra("item_image",item_image);
                startActivity(intent);
            }
        });



    }
}