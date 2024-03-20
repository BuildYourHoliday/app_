package it.unimib.buildyourholiday.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.unimib.buildyourholiday.ui.welcome.LoginActivity;
import it.unimib.buildyourholiday.R;

public class ChangePasswordActivity extends AppCompact {

    ImageButton backToAccount;
    Button confirm;

    TextInputLayout oldPasswordLayout;
    TextInputLayout newPasswordLayout;
    TextInputLayout newPasswordAgainLayout;
    TextInputEditText oldPassword;
    TextInputEditText newPassword;
    TextInputEditText newPasswordAgain;
    FirebaseUser mFireBaseAuth;

    ProfileActivity profileActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        //Codice che riporta alla pagina dell'account
        backToAccount = findViewById(R.id.back_buttom_to_setting);
        Intent backToAccountIntent = new Intent(this, AccountActivity.class);
        backToAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(backToAccountIntent);}
        });


        oldPasswordLayout = findViewById(R.id.textInputLayout_password);
        newPasswordLayout = findViewById(R.id.textInputLayout_newPassword);
        newPasswordAgainLayout = findViewById(R.id.textInputLayout_newPasswordAgain);
        oldPassword = findViewById(R.id.textInputEditText_password);
        newPassword = findViewById(R.id.textInputEditText_newPassword);
        newPasswordAgain = findViewById(R.id.textInputEditText_newPasswordAgain);
        confirm = findViewById(R.id.confirm_change_button);
        mFireBaseAuth = FirebaseAuth.getInstance().getCurrentUser();
        Intent intentToLogin = new Intent(this, LoginActivity.class);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPasswordLayout.setError(null);
                newPasswordLayout.setError(null);
                newPasswordAgainLayout.setError(null);
                String oldPasswordString = String.valueOf(oldPassword.getText());
                String newPasswordString = String.valueOf(newPassword.getText());
                String newPasswordAgainString = String.valueOf(newPasswordAgain.getText());
                if(!oldPasswordString.equals("") && !newPasswordString.equals("") && !newPasswordAgainString.equals("")){
                    AuthCredential authCredential = EmailAuthProvider.getCredential(mFireBaseAuth.getEmail(),oldPasswordString);
                    mFireBaseAuth.reauthenticate(authCredential)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    //verificare che le due nuove password siano uguali
                                    boolean nuovaDiversaDaVecchia = confrontaPassword(oldPasswordString, newPasswordString);
                                    boolean nuovePassUguali = confrontaPassword(newPasswordString, newPasswordAgainString);
                                    boolean passwordOk = isPasswordOk(eliminaSpazi(newPasswordString));
                                    if(!nuovaDiversaDaVecchia && nuovePassUguali && passwordOk){
                                        mFireBaseAuth.updatePassword(newPasswordString)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Snackbar.make(v, R.string.change_password_success, Snackbar.LENGTH_LONG).show();
                                                        FirebaseAuth.getInstance().signOut();
                                                        mFireBaseAuth = null;
                                                        startActivity(intentToLogin);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Snackbar.make(v, R.string.change_password_error, Snackbar.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }else{
                                        if(!nuovaDiversaDaVecchia)
                                            newPasswordLayout.setError(getString(R.string.old_password_wrong));
                                        if(!nuovePassUguali)
                                            newPasswordAgainLayout.setError(getString(R.string.new_password_again_wrong));
                                        if(!passwordOk)
                                            newPasswordLayout.setError(getString(R.string.invalid_password));
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    oldPasswordLayout.setError(getString(R.string.error_password));
                                }
                            });
                }else{
                    if(oldPasswordString.equals(""))
                        oldPasswordLayout.setError(getString(R.string.compile_camp));
                    if(newPasswordString.equals(""))
                        newPasswordLayout.setError(getString(R.string.compile_camp));
                    if(newPasswordAgainString.equals(""))
                        newPasswordAgainLayout.setError(getString(R.string.compile_camp));
                }

            }
        });
    }

    /**
     * Check if two password are equals.
     * @param pass1 The first password.
     * @param pass2 The second password.
     * @return True if the two password are equals, false otherwise.
     */
    public boolean confrontaPassword(String pass1, String pass2){
        String pass1SenzaSpazi = "";
        String pass2SenzaSpazi = "";
        pass1SenzaSpazi = eliminaSpazi(pass1);
        pass2SenzaSpazi = eliminaSpazi(pass2);
        return(pass1SenzaSpazi.equals(pass2SenzaSpazi));
    }

    /**
     * Check if the password is valid.
     * @param password The password to be checked.
     * @return True if the passowrd is not empty and the length is at least 8, false oterwise.
     */
    public boolean isPasswordOk(String password){
        boolean validation = (password != null && password.length() >= 8);
        return validation;
    }

    /**
     * Remove the white spaces from a password.
     * @param password The password from which to remove white spaces.
     * @return The password without white spaces.
     */
    public String eliminaSpazi(String password){
        String passwordSenzaSpazi = "";
        for(int i = 0; i < password.length(); ++i){
            if(password.charAt(i) != ' ')
                passwordSenzaSpazi += password.charAt(i);
        }
        return passwordSenzaSpazi;
    }
}
