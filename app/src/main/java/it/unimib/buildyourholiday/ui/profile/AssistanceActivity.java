package it.unimib.buildyourholiday.ui.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import it.unimib.buildyourholiday.R;

public class AssistanceActivity extends AppCompact {
    ImageButton backToInformation;
    Button emailAssistance;
    @SuppressLint("MissingInflatedId")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistance);

        backToInformation = findViewById(R.id.back_buttom_to_information_assistance);
        Intent backToTinformationIntent = new Intent(this, InformationActivity.class);
        backToInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(backToTinformationIntent);}
        });

        emailAssistance = findViewById(R.id.mail);
        emailAssistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:BuildYourHoliday.Assistenza@gnail.com"));
                startActivity(Intent.createChooser(emailIntent, "Invia email tramite"));
            }
        });

    }
}