package com.loko.translate_app;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;

public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    static int sonkod=0;
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
        Log.i("onItemSelected soncode", String.valueOf(sonkod));
        sonkod=getLanguageCode2(item);
        // Showing selected spinner item
        Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public int getLanguageCode2(String language) {
        Log.i("item2",language);

        switch (language){
            case "English":
                sonkod= FirebaseTranslateLanguage.EN;
                break;
            case "German":
                sonkod= FirebaseTranslateLanguage.DE;
                break;
            case "French":
                sonkod= FirebaseTranslateLanguage.FR;
                break;
            case "Arabic":
                sonkod= FirebaseTranslateLanguage.AR;
                break;
            case "Hindi":
                sonkod= FirebaseTranslateLanguage.HI;
                break;
            case "Japanese":
                sonkod= FirebaseTranslateLanguage.JA;
                break;
            case "Russian":
                sonkod= FirebaseTranslateLanguage.RU;
                break;
            case "Italian":
                sonkod= FirebaseTranslateLanguage.IT;
                break;
            case "Turkish":
                sonkod= FirebaseTranslateLanguage.TR;
                break;
            default:
                sonkod=0;

        }
        Log.i("sonun sonu soncode", String.valueOf(sonkod));
        return sonkod;
    }
}
