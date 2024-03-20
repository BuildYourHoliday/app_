package it.unimib.buildyourholiday.ui.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;

import it.unimib.buildyourholiday.R;

public class LanguageActivity extends AppCompact {

    ImageButton backToSetting;
    Button italianLanguageButton;
    Button englishLanguageButton;
    RadioButton italianLanguageRadioButton;
    RadioButton englishLanguageRadioButton;
    SharedPreferences italianLanguageSelected;
    SharedPreferences.Editor italianEditor;
    SharedPreferences englishLanguageSelected;
    SharedPreferences.Editor englishEditor;
    boolean italianRadioSelected;
    boolean englishRadioSelected;

    @SuppressLint("MissingInflatedId")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        backToSetting = findViewById(R.id.back_buttom_to_setting);

        italianLanguageRadioButton = findViewById(R.id.radio_italian);
        englishLanguageRadioButton = findViewById(R.id.radio_english);

        LanguageManager language = new LanguageManager(this);

        italianLanguageSelected = getSharedPreferences("Italian",MODE_PRIVATE);
        englishLanguageSelected = getSharedPreferences("English",MODE_PRIVATE);

        italianRadioSelected = italianLanguageSelected.getBoolean("italianRadioButtonSelectedd",false);
        englishRadioSelected = englishLanguageSelected.getBoolean("englishRadioButtonSelectedd",false);

        italianLanguageRadioButton.setChecked(italianRadioSelected);
        englishLanguageRadioButton.setChecked(englishRadioSelected);

        italianLanguageRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language.updateResource("it");
                updateSharedPrefences(true,false);
                recreate();
            }
        });

        englishLanguageRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language.updateResource("en");
                updateSharedPrefences(false,true);
                recreate();
            }
        });

        Intent intent = new Intent(this, ProfileActivity.class);
        backToSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }

    public void updateSharedPrefences(boolean italian, boolean english){
        italianLanguageSelected = getSharedPreferences("Italian",MODE_PRIVATE);
        italianEditor = italianLanguageSelected.edit();
        italianEditor.putBoolean("italianRadioButtonSelectedd",italian);

        englishLanguageSelected = getSharedPreferences("English",MODE_PRIVATE);
        englishEditor = englishLanguageSelected.edit();
        englishEditor.putBoolean("englishRadioButtonSelectedd",english);

        englishEditor.apply();
        italianEditor.apply();
    }
}
