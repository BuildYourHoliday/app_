package it.unimib.buildyourholiday.util;

/**
 * Utility class to save constants used by the app.
 */
public class Constants {


    // Constants for EncryptedSharedPreferences
    public static final String ENCRYPTED_SHARED_PREFERENCES_FILE_NAME = "it.unimib.worldnews.encrypted_preferences";
    public static final String EMAIL_ADDRESS = "email_address";
    public static final String PASSWORD = "password";
    public static final String ID_TOKEN = "google_token";

    // Constants for SharedPreferences
    public static final String SHARED_PREFERENCES_FILE_NAME = "it.unimib.buildyourholiday.preferences";
  /*  public static final String SHARED_PREFERENCES_COUNTRY_OF_INTEREST = "country_of_interest";
    public static final String SHARED_PREFERENCES_TOPICS_OF_INTEREST = "topics_of_interest";
    public static final String SHARED_PREFERENCES_FIRST_LOADING = "first_loading";

   */


    // Dao Database
    public static final String TRAVELS_DATABASE_NAME = "travel_db";

    // Constants for encrypted files
    public static final String ENCRYPTED_DATA_FILE_NAME = "it.unimib.worldnews.encrypted_file.txt";

    // Constants for Amadeus API
    public static final int TOTAL_FLIGHTS_RESULTS = 3;
    public static final int RATE_LIMIT_TIME = 3000;

    // Constants for errors
    public static final String GENERIC_ERROR = "unexpected_error";
    public static final String INVALID_USER_ERROR = "invalidUserError";
    public static final String INVALID_CREDENTIALS_ERROR = "invalidCredentials";
    public static final String USER_COLLISION_ERROR = "userCollisionError";
    public static final String WEAK_PASSWORD_ERROR = "passwordIsWeak";

    // Constants for Firebase Realtime Database
    public static final String FIREBASE_REALTIME_DATABASE = "https://buildyourholiday-default-rtdb.europe-west1.firebasedatabase.app/";
    public static final String FIREBASE_USERS_COLLECTION = "users";
    public static final String FIREBASE_SAVED_TRAVELS_COLLECTION = "travels";
    public static final int FRESH_TIMEOUT = 300000;


}
