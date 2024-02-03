package it.unimib.buildyourholiday.util;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AppCompact extends AppCompatActivity {
    protected void onCreate(Bundle savedIstanceState) {

        super.onCreate(savedIstanceState);
        LanguageManager languageManager = new LanguageManager(this);
        languageManager.updateResource(languageManager.getLanguage());
    }
}
