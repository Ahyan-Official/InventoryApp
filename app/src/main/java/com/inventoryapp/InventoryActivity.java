package com.inventoryapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class InventoryActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    RecyclerView mrecyclerview;
    DatabaseReference mdatabaseReference;
    private TextView totalnoofitem, totalnoofsum;
    private int counttotalnoofitem =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        //Top action bar
        ActionBar a = getSupportActionBar();
        a.setTitle("Inventory");
        a.setDisplayHomeAsUpEnabled(true);


        //Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finaluser=users.getEmail();
        String resultemail = finaluser.replace(".","");

        //database reference
        mdatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(resultemail).child("Items");


        //init Views
        totalnoofitem= findViewById(R.id.totalnoitem);
        totalnoofsum = findViewById(R.id.totalsum);
        mrecyclerview = findViewById(R.id.recyclerViews);
        firebaseAuth = FirebaseAuth.getInstance();




        //add layout manager to recyclerview
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mrecyclerview.setLayoutManager(manager);
        mrecyclerview.setHasFixedSize(true);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(this));


        //get total number of item in inventory
        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    counttotalnoofitem = (int) dataSnapshot.getChildrenCount();
                    totalnoofitem.setText(Integer.toString(counttotalnoofitem));
                }else{
                    totalnoofitem.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //get total sum of all products
        mdatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int sum=0;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Map<String,Object> map = (Map<String, Object>) ds.getValue();
                    Object price = map.get("itemprice");
                    int pValue = Integer.parseInt(String.valueOf(price));
                    sum += pValue;
                    totalnoofsum.setText(String.valueOf(sum));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected  void  onStart() {
        super.onStart();

        //query
        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(mdatabaseReference, Products.class).build();

        //firebase recyclerview
        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Products, UsersViewHolder>(options) {
            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // attach layout to RecyclerView
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);

                return new UsersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(UsersViewHolder viewHolder, int position, Products model) {

                // set data in views
                viewHolder.setDetails(getApplicationContext(),model.getItembarcode(),model.getItemcategory(),model.getItemname(),model.getItemprice(),model.getItemimage());
                // add click listesner
                viewHolder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), ViewProductActivity.class);
                        intent.putExtra("item_barcode",model.getItembarcode());
                        intent.putExtra("item_category",model.getItemcategory());
                        intent.putExtra("item_name",model.getItemname());
                        intent.putExtra("item_price",model.getItemprice());
                        intent.putExtra("item_stock",model.getItemstock());
                        intent.putExtra("item_image",model.getItemimage());

                        startActivity(intent);

                    }
                });

            }
        };


        firebaseRecyclerAdapter.startListening();
        // add adapter to recyclerview
        mrecyclerview.setAdapter(firebaseRecyclerAdapter);







    }

    //View Holder class
    public  static  class UsersViewHolder extends RecyclerView.ViewHolder{
        View mView;
        CardView card;

        public UsersViewHolder(View itemView){
            super(itemView);
            mView =itemView;
        }

        public void setDetails(Context ctx,String itembarcode, String itemcategory, String itemname, String itemprice,String itemimage){

            // init Views of the layout that is attached to recyclerview
            TextView item_barcode = (TextView) mView.findViewById(R.id.viewitembarcode);
            TextView item_name = (TextView) mView.findViewById(R.id.viewitemname);
            TextView item_category = (TextView) mView.findViewById(R.id.viewitemcategory);
            TextView item_price = (TextView) mView.findViewById(R.id.viewitemprice);
            RoundedImageView item_image = (RoundedImageView) mView.findViewById(R.id.itemimage);
            card =  mView.findViewById(R.id.card);

            // set data
            item_barcode.setText(itembarcode);
            item_category.setText("Category: "+itemcategory);
            item_name.setText(itemname);
            item_price.setText("Price: "+itemprice);

            //load image
            Picasso.get().load(itemimage).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(item_image);
        }

    }

}
