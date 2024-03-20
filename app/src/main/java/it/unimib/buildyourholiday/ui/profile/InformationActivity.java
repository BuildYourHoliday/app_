package it.unimib.buildyourholiday.ui.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import it.unimib.buildyourholiday.R;

public class InformationActivity extends AppCompact {

    ImageButton backToSettingButton;
    Button conditionOfUseButton;
    Button privacyPolicyButton;
    Button assistanaceButton;

    @SuppressLint("MissingInflatedId")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        backToSettingButton = findViewById(R.id.back_buttom_to_setting2);
        Intent backToSettingIntent = new Intent(this, ProfileActivity.class);
        backToSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(backToSettingIntent);}
        });


        conditionOfUseButton = findViewById(R.id.condition_of_use_button);
        Intent goToConditionOfUseIntent = new Intent(this, ConditionOfUseActivity.class);
        conditionOfUseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(goToConditionOfUseIntent);}
        });

        privacyPolicyButton = findViewById(R.id.privacy_policy_button);
        Intent goToPrivacyPolicyIntent = new Intent(this, PrivacyPolicyActivity.class);
        privacyPolicyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(goToPrivacyPolicyIntent);}
        });

        assistanaceButton = findViewById(R.id.assistance_button);
        Intent goToAssistanceIntent = new Intent(this, AssistanceActivity.class);
        assistanaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(goToAssistanceIntent);}
        });
    }
}
