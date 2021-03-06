package com.loko.translate_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.vision.CameraSource;

import java.io.IOException;

public class menu extends AppCompatActivity {
    private Button translate,camera,voice,app,infobut;
    private static final int PERMISSION_REQUEST_STORAGE=1000;
    private static final int MY_CAMERA_REQUEST_CODE = 100;





    private AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        translate=findViewById(R.id.button8);
        camera=findViewById(R.id.button9);
        voice=findViewById(R.id.button10);
        app=findViewById(R.id.button11);

        infobut=findViewById(R.id.button3);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        adView=findViewById(R.id.adView2);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ??ntent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(??ntent);
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ??ntent=new Intent(getApplicationContext(),camera.class);
                startActivity(??ntent);
            }
        });
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ??ntent=new Intent(getApplicationContext(),voice.class);
                startActivity(??ntent);
            }
        });

        app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ??ntent=new Intent(getApplicationContext(),app.class);
                startActivity(??ntent);
            }
        });

        final String mystring = getResources().getString(R.string.info);


        infobut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(menu.this);

                // alert dialog ba??l??????n?? tan??ml??yoruz.
                alertDialogBuilder.setTitle("??NFO");

                // alert dialog ??zelliklerini olu??turuyoruz.
                alertDialogBuilder
                        .setMessage(mystring)
                        .setCancelable(false)
                        .setIcon(R.mipmap.ic_launcher_round)
                        // Evet butonuna t??klan??nca yap??lacak i??lemleri buraya yaz??yoruz.
                        // ??ptal butonuna t??klan??nca yap??lacak i??lemleri buraya yaz??yoruz.
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                // alert dialog nesnesini olu??turuyoruz
                AlertDialog alertDialog = alertDialogBuilder.create();

                // alerti g??steriyoruz
                alertDialog.show();
            }
        });

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!=
                    PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED&&checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},PERMISSION_REQUEST_STORAGE);


            }


        }







    }








    @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if(requestCode==PERMISSION_REQUEST_STORAGE){
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED&&grantResults[2] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(menu.this,"??Z??N VER??LD??",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(menu.this,"??Z??N VER??LMED??",Toast.LENGTH_LONG).show();
                }
            }

    }
}

