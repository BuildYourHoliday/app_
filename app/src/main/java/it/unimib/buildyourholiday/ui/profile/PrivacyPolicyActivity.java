package it.unimib.buildyourholiday.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import it.unimib.buildyourholiday.R;
import it.unimib.buildyourholiday.ui.profile.AppCompact;
import it.unimib.buildyourholiday.ui.profile.InformationActivity;

public class PrivacyPolicyActivity extends AppCompact {

    ImageButton backToInformation;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        backToInformation = findViewById(R.id.back_buttom_to_information_privacy_policy);
        Intent backToInformationIntent = new Intent(this, InformationActivity.class);
        backToInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(backToInformationIntent);}
        });
    }


}
