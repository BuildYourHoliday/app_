package it.unimib.buildyourholiday;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import it.unimib.buildyourholiday.util.AppCompact;
import it.unimib.buildyourholiday.util.LanguageManager;

public class LanguageActivity extends AppCompact {

    ImageButton backToSetting;
    Button italianLanguageButton;
    Button englishLanguageButton;

    @SuppressLint("MissingInflatedId")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        backToSetting = findViewById(R.id.back_buttom_to_setting);
        italianLanguageButton = findViewById(R.id.italianButton);
        englishLanguageButton = findViewById(R.id.englishButton);
        LanguageManager language = new LanguageManager(this);

        italianLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language.updateResource("it");
                recreate();
            }
        });

        englishLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                language.updateResource("en");
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
}
