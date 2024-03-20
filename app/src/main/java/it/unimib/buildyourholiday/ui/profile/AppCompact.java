package it.unimib.buildyourholiday.ui.profile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AppCompact extends AppCompatActivity {
    protected  void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        LanguageManager languageManager = new LanguageManager((this));
        languageManager.updateResource(languageManager.getLang());
    }
}
