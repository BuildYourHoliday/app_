package it.unimib.buildyourholiday;

import static it.unimib.buildyourholiday.util.Constants.EMAIL_ADDRESS;
import static it.unimib.buildyourholiday.util.Constants.ENCRYPTED_DATA_FILE_NAME;
import static it.unimib.buildyourholiday.util.Constants.ENCRYPTED_SHARED_PREFERENCES_FILE_NAME;
import static it.unimib.buildyourholiday.util.Constants.PASSWORD;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;
import java.security.GeneralSecurityException;

import it.unimib.buildyourholiday.util.DataEncryptionUtil;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();


    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    private DataEncryptionUtil dataEncryptionUtil;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        textInputLayoutEmail = findViewById(R.id.textInputLayout_email);
        textInputLayoutPassword = findViewById(R.id.textInputLayout_password);
        final Button buttonLogin = findViewById(R.id.button_login);

        buttonLogin.setOnClickListener(v -> {

            String email = textInputLayoutEmail.getEditText().getText().toString();
            String password = textInputLayoutPassword.getEditText().getText().toString();

            dataEncryptionUtil = new DataEncryptionUtil(this);
            try {
                Log.d(TAG, "Email address from encrypted SharedPref: " + dataEncryptionUtil.
                        readSecretDataWithEncryptedSharedPreferences(
                                ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, EMAIL_ADDRESS));
                Log.d(TAG, "Password from encrypted SharedPref: " + dataEncryptionUtil.
                        readSecretDataWithEncryptedSharedPreferences(
                                ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, PASSWORD));
                Log.d(TAG, "Login data from encrypted file: " + dataEncryptionUtil.
                        readSecretDataOnFile(ENCRYPTED_DATA_FILE_NAME));
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
            }

            // Start login if email and password are ok
            if (isEmailOk(email) & isPasswordOk(password)) {
                Log.d(TAG, "Email and password are ok");
                saveLoginData(email, password);
                sharedPreferences = getSharedPreferences("Log", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putBoolean("Login",true);
                editor.apply();
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
            } else {
                Snackbar.make(findViewById(android.R.id.content),
                        "Check your data", Snackbar.LENGTH_SHORT).show();
            }
        });

    }


    private void saveLoginData(String email, String password) {
        try {
            dataEncryptionUtil.writeSecretDataWithEncryptedSharedPreferences(
                    ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, EMAIL_ADDRESS, email);
            dataEncryptionUtil.writeSecretDataWithEncryptedSharedPreferences(
                    ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, PASSWORD, password);

            dataEncryptionUtil.writeSecreteDataOnFile(ENCRYPTED_DATA_FILE_NAME,
                    email.concat(":").concat(password));
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }


    //controllo base email e password
    private boolean isEmailOk(String email) {
        // Check if the email is valid through the use of this library:
        // https://commons.apache.org/proper/commons-validator/
        if (!EmailValidator.getInstance().isValid((email))) {
            textInputLayoutEmail.setError(getString(R.string.error_email));
            return false;
        } else {
            textInputLayoutEmail.setError(null);
            return true;
        }
    }

    /**
     * Checks if the password is not empty.
     * @param password The password to be checked
     * @return True if the password is not empty, false otherwise
     */
    private boolean isPasswordOk(String password) {
        // Check if the password length is correct
        if (password.isEmpty() || password.length() < 8) {
            textInputLayoutPassword.setError(getString(R.string.error_password));
            return false;
        } else {
            textInputLayoutPassword.setError(null);
            return true;
        }
    }





}