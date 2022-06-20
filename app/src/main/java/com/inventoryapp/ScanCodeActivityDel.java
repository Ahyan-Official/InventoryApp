package com.inventoryapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanCodeActivityDel extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    int MY_PERMISSIONS_REQUEST_CAMERA=0;
    ZXingScannerView scannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //open qr/bar code camera
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
    }

    @Override
    public void handleResult(Result result) {

        // on code scanned. set qr/bar code result on DeleteProductsActivity

        DeleteProductsActivity.resultdeleteview.setText(result.getText());
        //go back to DeleteProductsActivity

        onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();

        // check if permission is granted or not. if not ask for camera permission
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        }
        //add handler and open camera
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}
