package com.inventoryapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {


    private EditText itemname,itemcategory,itemprice,editstock;
    private TextView itembarcode;
    private FirebaseAuth firebaseAuth;
    public static TextView resulttextview;
    Button scanbutton, additemtodatabase;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferencecat;

    ProgressDialog pd;
    public Uri ImageUri;
    Context context;
    FirebaseStorage storage;
    private StorageReference storageReference;
    FirebaseAuth auth;
    boolean imagePicked = false;
    ImageView pickImage;
    String image_link = "default";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        firebaseAuth = FirebaseAuth.getInstance();


        //Top action bar
        ActionBar a = getSupportActionBar();
        a.setTitle("Add Product");
        a.setDisplayHomeAsUpEnabled(true);

        //Firebase Storage referenece initialization
        FirebaseApp.initializeApp(context);
        storage = FirebaseStorage.getInstance();
        storageReference=storage.getReference();


        //Firebase authentication
        auth = FirebaseAuth.getInstance();
        //Progress bar
        pd = new ProgressDialog(this);
        pd.setTitle("Saving Data");
        pd.setMessage("Please Wait...");

        //Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReferencecat = FirebaseDatabase.getInstance().getReference("Users");

        //init views
        pickImage = findViewById(R.id.pickImage);
        resulttextview = findViewById(R.id.barcodeview);
        additemtodatabase = findViewById(R.id.additembuttontodatabase);
        scanbutton = findViewById(R.id.buttonscan);
        itemname = findViewById(R.id.edititemname);
        itemcategory= findViewById(R.id.editcategory);
        itemprice = findViewById(R.id.editprice);
        itembarcode= findViewById(R.id.barcodeview);
        editstock= findViewById(R.id.editstock);





        //Scan button to scan the code
        scanbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ScanCodeActivity.class));
            }
        });

        //add product to database
        additemtodatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });


        //pick image from gallery
        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePic();
            }
        });


    }

    // open gallery
    private void choosePic()
    {
        Intent intent;
        intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,1);
    }

    //on result gallery
    @Override
    protected void onActivityResult(int requestcode, int resultcode, @Nullable Intent data)
    {
        super.onActivityResult(requestcode,resultcode,data);

        if (requestcode==1 && resultcode==RESULT_OK && data!=null && data.getData()!=null)
        {
            //get image URI and upload to database
            ImageUri= data.getData();
            pickImage.setImageURI(ImageUri);
            uploadPic();

        }
    }
    private void uploadPic() {

        //show loading
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading picture...");
        pd.show();

        //get random id
        final String randomkey = UUID.randomUUID().toString();
        //firebase storeage path(where to save image)
        final StorageReference ref = storageReference.child("images/" + randomkey);

        //upload image
        ref.putFile(ImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        //get image URL
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUrl) {
                                pd.dismiss();
                                imagePicked = true;
                                //save URL in profile_link
                                image_link = downloadUrl.toString();
                                Toast.makeText(AddProductActivity.this, "Picture uploaded !", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(AddProductActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                        //show progress
                        double progressPercent = (100.00 * snapshot.getBytesTransferred()
                                / snapshot.getTotalByteCount());

                        pd.setMessage("Percentage: " + (int) progressPercent + "%");

                    }
                });
    }


    // adding item to database
    public  void addProduct(){

        //getring values from fields
        String itemnameValue = itemname.getText().toString();
        String itemcategoryValue = itemcategory.getText().toString();
        String itempriceValue = itemprice.getText().toString();
        String itembarcodeValue = itembarcode.getText().toString();
        String itemstock = editstock.getText().toString();

        final FirebaseUser users = firebaseAuth.getCurrentUser();


        //get user email
        String finaluser=users.getEmail();
        String resultemail = finaluser.replace(".","");


        if (itembarcodeValue.isEmpty()) {
            itembarcode.setError("It's Empty");
            itembarcode.requestFocus();
            return;
        }


        // all fields must be filled
        if(!TextUtils.isEmpty(itemnameValue)&&!TextUtils.isEmpty(itemcategoryValue)&&!TextUtils.isEmpty(itempriceValue)){


            //upload product to database
            Products items = new Products(itemnameValue,itemcategoryValue,itempriceValue,itembarcodeValue,itemstock, image_link);
            databaseReference.child(resultemail).child("Items").child(itembarcodeValue).setValue(items);
            databaseReferencecat.child(resultemail).child("ItemByCategory").child(itemcategoryValue).child(itembarcodeValue).setValue(items);
            itemname.setText("");
            itembarcode.setText("");
            itemprice.setText("");
            itembarcode.setText("");
            editstock.setText("");

            Toast.makeText(AddProductActivity.this,itemnameValue+" Added",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(AddProductActivity.this,"Please Fill all the fields",Toast.LENGTH_SHORT).show();
        }
    }
















    // logout
    private void Logout()
    {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(AddProductActivity.this, LoginActivity.class));
        Toast.makeText(AddProductActivity.this,"LOGOUT SUCCESSFUL", Toast.LENGTH_SHORT).show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // menu
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // on clicking logout. Logout the user
        switch (item.getItemId()){
            case  R.id.logoutMenu:{
                Logout();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
