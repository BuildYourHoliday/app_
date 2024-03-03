package it.unimib.buildyourholiday;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;
import it.unimib.buildyourholiday.model.User;

public class ProfileActivity extends AppCompact implements LogoutBottomSheetDialog.BottomSheetListener, ProfilePicBottomSheetDialog.BottomSheetListener {
    Switch darkModeSwitch;
    boolean darkMode;
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences1;

    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editor;

    SharedPreferences.Editor editor1;
    SharedPreferences.Editor editor2;
    private static final String TAG = ProfileActivity.class.getSimpleName();

    Button language, accountButton, logoutButton, loginButton, AboutUs;

    TextView account, logout, username;

    ImageButton addPhoto, backToMainActivity;
    CircleImageView profilePhoto;
    SharedPreferences sharedPreferences3;
    public static String PROFILE_PREFENCES = "profilePref";
    boolean login;
    boolean profilePic;
    SharedPreferences profileShared;
    SharedPreferences.Editor profilePicEditor;

    private FirebaseUser mFireBaseAuth;
    private UserViewModel userViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        account = findViewById(R.id.account);
        logout = findViewById(R.id.logout);
        accountButton = findViewById(R.id.accountButton);
        logoutButton = findViewById(R.id.logoutButton);
        addPhoto = findViewById(R.id.add_photo);
        loginButton = findViewById(R.id.login_bottom);
        profilePhoto = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);

     /*   sharedPreferences1 = getSharedPreferences("Log", MODE_PRIVATE);
        login = sharedPreferences1.getBoolean("Login", false);*/

        profileShared = getSharedPreferences("Photo",MODE_PRIVATE);
        profilePic = profileShared.getBoolean("PhotoProfile",false);
        mFireBaseAuth = FirebaseAuth.getInstance().getCurrentUser();
        updateUI();

        darkModeSwitch = findViewById(R.id.dark_mode_switch);
        language = findViewById(R.id.languageButton);

        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        darkMode = sharedPreferences.getBoolean("darkMode", false);

        backToMainActivity = findViewById(R.id.back_buttom);
        Intent goToMainActivityIntent = new Intent(this, MainActivity.class);
        backToMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goToMainActivityIntent);
            }
        });


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

        /*
         Funzione che crea un intent che porta nell'activity per scegliere il linguaggio
        */
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

        AboutUs = findViewById(R.id.aboutUsButton);
        Intent goToInformation = new Intent(this, InformationActivity.class);
        AboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(goToInformation);}
        });

        //Codice per andare nella pagina dell'account
        Intent goToAccountIntent = new Intent(this, AccountActivity.class);
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startActivity(goToAccountIntent);}
        });

    }
    /*
    Funzione che aggiorna la UI in base al fatto che un utente sia loggato oppure no,
    se l'utente non è loggato allora viene impostata la foto profilo di default e il tasto
    del logout, dell'account, e il bottone per impostare la foto profilo non vengono mostrati
    mentre viene impostato il bottone per il login. Se invece l'utente è loggato avviene il contratio,
    inoltre viene caricata la foto profilo che l'utente ha impostato (se l'ha fatto).
    */
    @SuppressLint("UseCompatLoadingForDrawables")
    public void updateUI() {
        if (mFireBaseAuth != null) {
            if(profilePic) {
                Uri defaultImageUri = Uri.parse("android.resource://my.package.name/" + R.drawable.no_profile_pic);
                // Uri defaultImageUri = Uri.parse("android.resource://my_app_package/drewable/no_profile:pic");

                sharedPreferences3 = getSharedPreferences(PROFILE_PREFENCES, 0);
                Uri imageUri = Uri.parse(sharedPreferences3.getString("ProfileImageUri", defaultImageUri.toString()));
                Uri imgUri = Uri.parse(String.valueOf(imageUri));
                profilePhoto.setImageURI(imgUri);
            }else
                profilePhoto.setImageDrawable(getResources().getDrawable(
                        R.drawable.no_profile_pic,  getTheme()
                ));

            FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
            String name = users.getEmail();
            loginButton.setVisibility(View.GONE);
            account.setVisibility(View.VISIBLE);
            accountButton.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);
            logoutButton.setVisibility(View.VISIBLE);
            addPhoto.setVisibility(View.VISIBLE);
            username.setVisibility(View.VISIBLE);
            username.setText(name);
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
            username.setText("");
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

                    saveProfileImage(uriImage);
                    profilePicEditor = profileShared.edit();
                    profilePicEditor.putBoolean("PhotoProfile",true);
                    profilePicEditor.apply();
                    profilePic = profileShared.getBoolean("PhotoProfile",false);

                }
            }
    );

    public void saveProfileImage(Uri uri){
        sharedPreferences3 = getSharedPreferences(PROFILE_PREFENCES, 0);
        SharedPreferences.Editor editor = sharedPreferences3.edit();
        editor.putString("ProfileImageUri", String.valueOf(uri));
        editor.apply();
    }

    @Override
    public void oonButtonClicked(String action) {
        if(action.equals("Yes")){
           /* editor1 = sharedPreferences1.edit();
            editor1.putBoolean("Login",false);
            editor1.apply();
            login = sharedPreferences1.getBoolean("Login",false);*/
           // mFireBaseAuth.signOut();
           // mFireBaseAuth = null;
            FirebaseAuth.getInstance().signOut();
            mFireBaseAuth = null;
            updateUI();
        } else if (action.equals("Add")) {
            openGallery();
        } else if (action.equals("Remove")) {
            profilePhoto = findViewById(R.id.profile_image);
            profilePhoto.setImageDrawable(getResources().getDrawable(
                    R.drawable.no_profile_pic,  getTheme()
            ));
            profilePicEditor = profileShared.edit();
            profilePicEditor.putBoolean("PhotoProfile",false);
            profilePicEditor.apply();
            profilePic = profileShared.getBoolean("PhotoProfile",false);
            updateUI();
        }
    }
}