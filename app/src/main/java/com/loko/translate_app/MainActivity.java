package com.loko.translate_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.CameraSource;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.loko.translate_app.CustomOnItemSelectedListener.sonkod;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private TextView msourclang;
    private EditText msourcetet;
    private Button çevir,save;
    private TextView mtranslatedtext;
    private String sourcetext;
    int langcode;

    String filename,myText,item;
    Spinner sp1,sp2;
    public static String kytt;

    SurfaceView cameraView;
    TextView textView;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;



   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            break;
        }
    }

*/

   private InterstitialAd mInterstitialAd;
    private AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);

        final String mystring = getResources().getString(R.string.gecismain);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(mystring);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mInterstitialAd.show();
            }
        });



        adView=findViewById(R.id.adView);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);



        msourcetet=findViewById(R.id.sourcetext);
        çevir=findViewById(R.id.translate);
        mtranslatedtext=findViewById(R.id.tranlatelATEDTEXT);
        mtranslatedtext.setMovementMethod(new ScrollingMovementMethod());

        sp1=findViewById(R.id.sp1);
        sp1.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        save=findViewById(R.id.button4);




        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Diller, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        sp1.setAdapter(adapter);
        addListenerOnSpinnerItemSelection();

        çevir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translatetext(msourcetet.getText());
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePdf(kytt);

            }
        });





    }


    public void getLanguageCode(String language) {

        switch (language){
            case "English":
                langcode= FirebaseTranslateLanguage.EN;
                break;

            case "German":
                langcode=FirebaseTranslateLanguage.DE;
                break;
            case "French":
                langcode=FirebaseTranslateLanguage.FR;
                break;
            case "Arabic":
                langcode=FirebaseTranslateLanguage.AR;
                break;
            case "Hindi":
                langcode=FirebaseTranslateLanguage.HI;
                break;
            case "Japanese":
                langcode=FirebaseTranslateLanguage.JA;
                break;
            case "Russian":
                langcode=FirebaseTranslateLanguage.RU;
                break;
            case "Italian":
                langcode=FirebaseTranslateLanguage.IT;
                break;
            case "Turkish":
                langcode=FirebaseTranslateLanguage.TR;
                break;
            default:
                langcode=0;
        }


    }


    private void translatetext( final CharSequence sourcetext) {
        FirebaseTranslatorOptions options=new FirebaseTranslatorOptions.Builder()

                .setSourceLanguage(langcode)
                .setTargetLanguage(sonkod)
                .build();
        Log.i("Langcode", String.valueOf(langcode));
        Log.i("translatetext soncode", String.valueOf(sonkod));
        final FirebaseTranslator translator=FirebaseNaturalLanguage.getInstance()
                .getTranslator(options);
        FirebaseModelDownloadConditions conditions=new FirebaseModelDownloadConditions.Builder()
                .build();
        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                translator.translate(String.valueOf(sourcetext)).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        kytt=s;
                        mtranslatedtext.setText(s);
                    }
                });

            }
        });

    }

    private void savePdf(String text) {
        //create object of Document class
        Document mDoc = new Document();
        //pdf file name
        String mFileName = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(System.currentTimeMillis());


        //pdf file path

        String mFilePath = Environment.getExternalStorageDirectory() + "/" + mFileName + sonkod +".pdf";

        try {
            //create instance of PdfWriter class
            PdfWriter.getInstance(mDoc, new FileOutputStream(mFilePath));
            //open the document for writing
            mDoc.open();
            //get text from EditText i.e. mTextEt
            String mText = text;

            //add author of the document (optional)
            mDoc.addAuthor("YEB");

            //add paragraph to the document
            mDoc.add(new Paragraph(mText));

            //close the document
            mDoc.close();
            //show message that file is saved, it will show file name and file path too
            Toast.makeText(this, mFileName +".pdf\nis saved to\n"+ mFilePath, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            //if any thing goes wrong causing exception, get and show exception message
            Toast.makeText(this, "ERROR "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }







    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        item = parent.getItemAtPosition(position).toString();
        getLanguageCode(item);
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    public void addListenerOnSpinnerItemSelection() {
        sp2 = (Spinner) findViewById(R.id.sp2);
        sp2.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Diller, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        sp2.setAdapter(adapter);
    }
}