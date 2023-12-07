package it.unimib.buildyourholiday;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.validator.routines.EmailValidator;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private TextInputLayout emailTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
    private TextInputLayout passwordAgainTextInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailTextInputLayout = findViewById(R.id.textInputLayout_email);
        passwordTextInputLayout = findViewById(R.id.textInputLayout_password);
        passwordAgainTextInputLayout = findViewById(R.id.textInputLayout_passwordAgain);
        Button registerButton = findViewById(R.id.button_signUp);

        registerButton.setOnClickListener(item -> {
            Log.d(TAG, "Bottone cliccato");
            String email = emailTextInputLayout.getEditText().getText().toString();
            String password = passwordTextInputLayout.getEditText().getText().toString();
            String passwordAgain = passwordAgainTextInputLayout.getEditText().getText().toString();

            Log.d(TAG, "Email: "+email);
            Log.d(TAG, "Password: "+password);

            if (isEmailOk(email) && isPasswordOk(password) && isPasswordAgainOk(password, passwordAgain)){

            } else {
                Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.error_message),
                        Snackbar.LENGTH_SHORT).show();
            }


        });


    }
    private boolean isEmailOk(String email){
        boolean validation = EmailValidator.getInstance().isValid(email);
        if (!validation){
            emailTextInputLayout.setError("@string/invalid_email");
        } else{
            emailTextInputLayout.setError(null);
        }
        return validation;
    }

    private boolean isPasswordOk(String password){
        boolean validation = password != null && password.length() >= 8;
        if (!validation){
            passwordTextInputLayout.setError("@string/invalid_password");
        } else{
            passwordTextInputLayout.setError(null);
        }
        return validation;
    }

    private boolean isPasswordAgainOk(String password, String passwordAgain){
        boolean validation = password != null && password.equals(passwordAgain);
        if (!validation){
            passwordTextInputLayout.setError("@string/invalid_passwordAgain");
        } else{
            passwordTextInputLayout.setError(null);
        }
        return validation;
    }


}