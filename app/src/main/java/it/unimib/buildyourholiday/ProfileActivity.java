package it.unimib.buildyourholiday;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import com.google.firebase.database.DatabaseReference;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import it.unimib.buildyourholiday.util.AppCompact;
import it.unimib.buildyourholiday.util.LanguageManager;
import it.unimib.buildyourholiday.util.LogoutBottomSheetDialog;
import it.unimib.buildyourholiday.util.ProfilePicBottomSheetDialog;

public class ProfileActivity extends AppCompact implements LogoutBottomSheetDialog.BottomSheetListener {
    Switch darkModeSwitch;
    boolean darkMode;
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences1;

    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editor;

    SharedPreferences.Editor editor1;
    SharedPreferences.Editor editor2;
    private static final String TAG = ProfileActivity.class.getSimpleName();

    Button language;

    TextView account;
    TextView logout;
    Button accountButton;
    Button logoutButton;
    Button loginButton;
    ImageButton addPhoto;

    CircleImageView profilePhoto;

    boolean login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences1 = getSharedPreferences("Log", MODE_PRIVATE);
        login = sharedPreferences1.getBoolean("Login", false);
        updateUI();

        darkModeSwitch = findViewById(R.id.dark_mode_switch);
        language = findViewById(R.id.languageButton);

        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        darkMode = sharedPreferences.getBoolean("darkMode", false);


        if (darkMode) {
            darkModeSwitch.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        darkModeSwitch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (darkMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("darkMode", false);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("darkMode", true);
                }
                editor.apply();
            }
        });


        Intent intent = new Intent(this, LanguageActivity.class);
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        loginButton = findViewById(R.id.login_bottom);
        Intent intentToLogin = new Intent(this, LoginActivity.class);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentToLogin);
            }
        });

        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogoutBottomSheetDialog logoutBottomSheetDialog = new LogoutBottomSheetDialog();
                logoutBottomSheetDialog.show(getSupportFragmentManager(), "Logout bottom sheet");
            }
        });


        profilePhoto = findViewById(R.id.profile_image);
        addPhoto = findViewById(R.id.add_photo);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfilePicBottomSheetDialog picBottomSheetDialog = new ProfilePicBottomSheetDialog();
                picBottomSheetDialog.show(getSupportFragmentManager(), "Profile pic bottom sheet");

            }
        });

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void updateUI() {
        account = findViewById(R.id.account);
        logout = findViewById(R.id.logout);
        accountButton = findViewById(R.id.accountButton);
        logoutButton = findViewById(R.id.logoutButton);
        addPhoto = findViewById(R.id.add_photo);
        loginButton = findViewById(R.id.login_bottom);
        profilePhoto = findViewById(R.id.profile_image);
        if (login) {
            loginButton.setVisibility(View.GONE);
            account.setVisibility(View.VISIBLE);
            accountButton.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.VISIBLE);
            addPhoto.setVisibility(View.VISIBLE);
        } else {
            profilePhoto.setImageDrawable(getResources().getDrawable(
                    R.drawable.no_profile_pic,  getTheme()
            ));
            loginButton.setVisibility(View.VISIBLE);
            account.setVisibility(View.GONE);
            accountButton.setVisibility(View.GONE);
            logout.setVisibility(View.GONE);
            logoutButton.setVisibility(View.GONE);
            addPhoto.setVisibility(View.GONE);
        }
    }

    public void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        openGallery.launch(intent);
    }
    ActivityResultLauncher<Intent> openGallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    Uri uriImage = o.getData().getData();
                    profilePhoto.setImageURI(uriImage);
                }
            }
    );

    @Override
    public void onButtonClicked(String action) {
        if(action.equals("Yes")){
            editor1 = sharedPreferences1.edit();
            editor1.putBoolean("Login",false);
            editor1.apply();
            login = sharedPreferences1.getBoolean("Login",false);
            updateUI();
        } else if (action.equals("Add")) {
            openGallery();
        } else if (action.equals("Remove")) {
            profilePhoto = findViewById(R.id.profile_image);
            profilePhoto.setImageDrawable(getResources().getDrawable(
                    R.drawable.no_profile_pic,  getTheme()
            ));
        }
    }
}