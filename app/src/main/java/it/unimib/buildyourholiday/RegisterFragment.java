package it.unimib.buildyourholiday;

import static it.unimib.buildyourholiday.util.Constants.EMAIL_ADDRESS;
import static it.unimib.buildyourholiday.util.Constants.ENCRYPTED_DATA_FILE_NAME;
import static it.unimib.buildyourholiday.util.Constants.ENCRYPTED_SHARED_PREFERENCES_FILE_NAME;
import static it.unimib.buildyourholiday.util.Constants.ID_TOKEN;
import static it.unimib.buildyourholiday.util.Constants.INVALID_CREDENTIALS_ERROR;
import static it.unimib.buildyourholiday.util.Constants.INVALID_USER_ERROR;
import static it.unimib.buildyourholiday.util.Constants.PASSWORD;
import static it.unimib.buildyourholiday.util.Constants.USER_COLLISION_ERROR;
import static it.unimib.buildyourholiday.util.Constants.WEAK_PASSWORD_ERROR;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.app.MediaRouteButton;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;
import java.security.GeneralSecurityException;

import it.unimib.buildyourholiday.model.Result;
import it.unimib.buildyourholiday.model.User;
import it.unimib.buildyourholiday.util.DataEncryptionUtil;

public class RegisterFragment extends Fragment {

    private static final String TAG = RegisterFragment.class.getSimpleName();
    private TextInputLayout emailTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
    private TextInputLayout passwordAgainTextInputLayout;
    private Button loginButton;
    private ProgressBar progressBar;
    private UserViewModel userViewModel;
    private DataEncryptionUtil dataEncryptionUtil;
    private TextInputLayout nameTextInputLayout;

    public RegisterFragment() {
        //
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.setAuthenticationError(false);
        dataEncryptionUtil = new DataEncryptionUtil(requireActivity().getApplication());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        return inflater.inflate(R.layout.fragment_register,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameTextInputLayout = view.findViewById(R.id.textInputLayout_username);
        emailTextInputLayout = view.findViewById(R.id.textInputLayout_email);
        passwordTextInputLayout = view.findViewById(R.id.textInputLayout_password);
        passwordAgainTextInputLayout = view.findViewById(R.id.textInputLayout_passwordAgain);
        loginButton = view.findViewById(R.id.button_alreadySigned);
        progressBar = view.findViewById(R.id.progress_bar);
        Button registerButton = view.findViewById(R.id.button_signUp);

        registerButton.setOnClickListener(item -> {
            Log.d(TAG, "Bottone cliccato");
            String name = nameTextInputLayout.getEditText().getText().toString();
            String email = emailTextInputLayout.getEditText().getText().toString();
            String password = passwordTextInputLayout.getEditText().getText().toString();
            String passwordAgain = passwordAgainTextInputLayout.getEditText().getText().toString();

            Log.d(TAG, "Nome: " + name);
            Log.d(TAG, "Email: "+email);
            Log.d(TAG, "Password: "+password);

            // & to make every single check all the time --> displaying all errors
            if (isEmailOk(email) & isPasswordOk(password) & isPasswordAgainOk(password, passwordAgain)){
                progressBar.setVisibility(View.VISIBLE);
                if(!userViewModel.isAuthenticationError()) {
                    userViewModel.getUserMutableLiveData(email,password,false).observe(
                            getViewLifecycleOwner(), result -> {
                                if (result.isSuccess()) {
                                    User user = ((Result.UserResponseSuccess) result).getData();
                                    saveLoginData(email,password,user.getIdToken());
                                    userViewModel.setAuthenticationError(false);
                                    Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_mainActivity);
                                } else {
                                    userViewModel.setAuthenticationError(true);
                                    Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                            getErrorMessage(((Result.Error) result).getMessage()),
                                            Snackbar.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    userViewModel.getUser(email,password,false);
                }
                progressBar.setVisibility(View.GONE);
            } else {
                if(password.isEmpty()) {
                    Snackbar.make(
                            requireActivity().findViewById(android.R.id.content),
                            getString(R.string.error_form_message),
                            Snackbar.LENGTH_SHORT).show();
                } else if(!isEmailOk(email)) {
                    Snackbar.make(
                            requireActivity().findViewById(android.R.id.content),
                            getString(R.string.invalid_email),
                            Snackbar.LENGTH_SHORT).show();
                } else if(!isPasswordAgainOk(password, passwordAgain)) {
                    Snackbar.make(
                            requireActivity().findViewById(android.R.id.content),
                            getString(R.string.error_check_password),
                            Snackbar.LENGTH_SHORT).show();
                } else
                    Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        getString(R.string.error_message),
                        Snackbar.LENGTH_SHORT).show();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });

    }
    private boolean isEmailOk(String email){
        boolean validation = EmailValidator.getInstance().isValid(email);
        if (!validation){
            emailTextInputLayout.setError(getString(R.string.invalid_email));
        } else{
            emailTextInputLayout.setError(null);
        }
        return validation;
    }

    private boolean isPasswordOk(String password){
        boolean validation = (password != null && password.length() >= 8);
        if (!validation){
            passwordTextInputLayout.setError(getString(R.string.invalid_password));
        } else{
            passwordTextInputLayout.setError(null);
        }
        return validation;
    }

    private boolean isPasswordAgainOk(String password, String passwordAgain){
        boolean validation = (password != null && password.equals(passwordAgain));
        if (!validation){
            passwordAgainTextInputLayout.setError(getString(R.string.invalid_passwordAgain));
        } else{
            passwordTextInputLayout.setError(null);
        }
        return validation;
    }

    private String getErrorMessage(String errorType) {
        switch (errorType) {
            case WEAK_PASSWORD_ERROR:
                return requireActivity().getString(R.string.error_registration_password_message);
            case USER_COLLISION_ERROR:
                return requireActivity().getString(R.string.error_collision_user_message);
            default:
                return requireActivity().getString(R.string.generic_error);
        }
    }

    /**
     * Encrypts login data using DataEncryptionUtil class.
     * @param email The email address to be encrypted and saved
     * @param password The password to be encrypted and saved
     * @param idToken The token associated to the account
     */
    private void saveLoginData(String email, String password, String idToken) {
        try {
            dataEncryptionUtil.writeSecretDataWithEncryptedSharedPreferences(
                    ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, EMAIL_ADDRESS, email);
            dataEncryptionUtil.writeSecretDataWithEncryptedSharedPreferences(
                    ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, PASSWORD, password);
            dataEncryptionUtil.writeSecretDataWithEncryptedSharedPreferences(
                    ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, ID_TOKEN, idToken);

            dataEncryptionUtil.writeSecreteDataOnFile(ENCRYPTED_DATA_FILE_NAME,
                        email.concat(":").concat(password));
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}