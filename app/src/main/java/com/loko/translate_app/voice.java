package com.loko.translate_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import static com.loko.translate_app.CustomOnItemSelectedListener.sonkod;

public class voice extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button button;
    public static String kyt;

    String filename,myText,item;
    int langcode ;

    private Button button2,button3;

 Spinner sp1,sp2;

    private TextView textView;
    private TextView textView2;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        textView = (TextView) this.findViewById(R.id.textView);textView.setMovementMethod(new ScrollingMovementMethod());
        textView2 = (TextView) this.findViewById(R.id.textView2);textView2.setMovementMethod(new ScrollingMovementMethod());
sp1=findViewById(R.id.sp1);


        final String mystring = getResources().getString(R.string.gecisvoice);

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

        button = (Button) this.findViewById(R.id.button);
        button2 = (Button) this.findViewById(R.id.button2);
        button3 = (Button) this.findViewById(R.id.button3);


        sp1.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Diller, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        sp1.setAdapter(adapter);
        addListenerOnSpinnerItemSelection();





        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
                try{
                    startActivityForResult(intent,200);
                }catch (ActivityNotFoundException a){
                    Toast.makeText(getApplicationContext(),"Intent problem", Toast.LENGTH_SHORT).show();
                }
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               translatetext(textView.getText());
            }
        });


        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePdf(kyt);

            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200){
            if(resultCode == RESULT_OK && data != null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                textView.setText(result.get(0));


            }
        }
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
                        kyt=s;
                        textView2.setText(s);
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
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();

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