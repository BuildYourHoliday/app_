package it.unimib.buildyourholiday.ui.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.apache.commons.validator.routines.EmailValidator;

import it.unimib.buildyourholiday.R;
import it.unimib.buildyourholiday.ui.profile.AccountActivity;
import it.unimib.buildyourholiday.ui.profile.AppCompact;

public class ChangeEmailActivity extends AppCompact {

    ImageButton backToSetting;
    Button confirmChangeEmail;
    TextInputEditText oldEmail, newEmail, password;
    TextInputLayout oldEmailLayout, newEmailLayout, passwordLayout;
    private FirebaseUser mFireBaseAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        //Codice per tornare alla pagina account
        backToSetting = findViewById(R.id.back_buttom_to_setting);
        Intent backToAccountIntent = new Intent(this, AccountActivity.class);
        backToSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(backToAccountIntent);}
        });

       oldEmailLayout = findViewById(R.id.textInputLayout_email);
       newEmailLayout = findViewById(R.id.textInputLayout_newEmail);
       passwordLayout = findViewById(R.id.textInputLayout_password);
       oldEmail = findViewById(R.id.textInputEditText_email);
       newEmail = findViewById(R.id.textInputEditText_newEamil);
       password = findViewById(R.id.textInputEditText_password);
       confirmChangeEmail = findViewById(R.id.confirm_change_button);
       mFireBaseAuth = FirebaseAuth.getInstance().getCurrentUser();


       confirmChangeEmail.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               oldEmailLayout.setError(null);
               newEmailLayout.setError(null);
               passwordLayout.setError(null);
               String oldEmailString = eliminaSpazi(String.valueOf(oldEmail.getText()));
               String newEmailString = eliminaSpazi(String.valueOf(newEmail.getText()));
               String passwordString = eliminaSpazi(String.valueOf(password.getText()));
               if(!oldEmailString.equals("") && !newEmailString.equals("") && !passwordString.equals("")){
                   String actualEmail = mFireBaseAuth.getEmail();
                   if(confrontaEmail(actualEmail,oldEmailString)) {
                      if(confrontaEmail(oldEmailString,newEmailString)){
                          newEmailLayout.setError(getString(R.string.same_email));
                      }else{
                          if(isEmailOk(newEmailString)) {
                              FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                              AuthCredential credential = EmailAuthProvider
                                      .getCredential(oldEmailString,passwordString);
                              user.reauthenticate(credential)
                                              .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                  @Override
                                                  public void onSuccess(Void unused) {
                                                          FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                          user.verifyBeforeUpdateEmail(newEmailString)
                                                                  .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                      @Override
                                                                      public void onComplete(@NonNull Task<Void> task) {
                                                                          if (task.isSuccessful()) {
                                                                              Snackbar.make(v, R.string.change_email_successs, Snackbar.LENGTH_SHORT).show();
                                                                          }
                                                                      }
                                                                  });
                                                  }
                                              })
                                      .addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(@NonNull Exception e) {
                                              Snackbar.make(v, R.string.fail_login, Snackbar.LENGTH_SHORT).show();
                                          }
                                      });
                          }else{
                              newEmailLayout.setError(getString(R.string.error_email));
                          }
                      }
                   }else{
                       oldEmailLayout.setError(getString(R.string.email_errata));
                   }

               }else{
                    if(oldEmailString.equals("")){oldEmailLayout.setError(getString(R.string.compile_camp));}
                    if(newEmailString.equals("")){newEmailLayout.setError(getString(R.string.compile_camp));}
                    if(passwordString.equals("")){passwordLayout.setError(getString(R.string.compile_camp));}
               }
           }
       });
    }

    /**
     * Check if the email si valid.
     * @param email The email to be checked.
     * @return True if the email is valid, false otherwise.
     */
    private boolean isEmailOk(String email){
        boolean validation = EmailValidator.getInstance().isValid(email);
        return validation;
    }

    /**
     * Check if two emails are equals.
     * @param email1 The first email.
     * @param email2 The second email.
     * @return True if the two emails are the same, false otherwise.
     */
    public boolean confrontaEmail(String email1, String email2){
        String email1SenzaSpazi = "";
        String email2SenzaSpazi = "";
        email1SenzaSpazi = eliminaSpazi(email1);
        email2SenzaSpazi = eliminaSpazi(email2);
        return(email1SenzaSpazi.equals(email2SenzaSpazi));
    }


    /**
     * Remove the white spaces from a password.
     * @param email The email from which to remove white spaces.
     * @return The email without white spaces.
     */
    public String eliminaSpazi(String email){
        String emailSenzaSpazi = "";
        for(int i = 0; i < email.length(); ++i){
            if(email.charAt(i) != ' ')
                emailSenzaSpazi += email.charAt(i);
        }
        return emailSenzaSpazi;
    }

}
