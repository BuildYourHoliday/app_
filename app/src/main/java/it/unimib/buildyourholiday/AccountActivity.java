package it.unimib.buildyourholiday;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import it.unimib.buildyourholiday.AppCompact;


public class AccountActivity extends AppCompact {
    ImageButton backToSetting;
    Button goToChangeEmail;
    Button goToChangePassword;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        //Codice per tornare alla pagina del profilo
        backToSetting = findViewById(R.id.back_buttom_to_setting);
        Intent backToSettingIntent = new Intent(this, ProfileActivity.class);
        backToSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(backToSettingIntent);
            }
        });

        //Coidce per andare alla pagina di modifica email
        goToChangeEmail = findViewById(R.id.change_email_button);
        Intent goToChangeEmailIntent = new Intent(this, ChangeEmailActivity.class);
        goToChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(goToChangeEmailIntent);}
        });

        //Codice per andare alla pagina di modifica password
        goToChangePassword = findViewById(R.id.change_password_button);
        Intent goToChangePasswordIntent = new Intent(this, ChangePasswordActivity.class);
        goToChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(goToChangePasswordIntent);}
        });
    }
}
