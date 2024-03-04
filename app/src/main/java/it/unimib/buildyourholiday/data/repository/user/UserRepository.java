package it.unimib.buildyourholiday.data.repository.user;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.unimib.buildyourholiday.data.source.travel.BaseSavedTravelDataSource;
import it.unimib.buildyourholiday.data.source.travel.BaseTravelLocalDataSource;
import it.unimib.buildyourholiday.data.source.travel.TravelCallback;
import it.unimib.buildyourholiday.model.Result;
import it.unimib.buildyourholiday.model.Travel;
import it.unimib.buildyourholiday.model.User;
import it.unimib.buildyourholiday.data.source.user.BaseUserAuthenticationRemoteDataSource;
import it.unimib.buildyourholiday.data.source.user.BaseUserDataRemoteDataSource;


/**
 * Repository class to get the user information.
 */
public class UserRepository implements IUserRepository, UserResponseCallback, TravelCallback {

    private static final String TAG = UserRepository.class.getSimpleName();

    private final BaseUserAuthenticationRemoteDataSource userRemoteDataSource;
    private final BaseUserDataRemoteDataSource userDataRemoteDataSource;
    private final BaseTravelLocalDataSource travelLocalDataSource;
    private final MutableLiveData<Result> userMutableLiveData;
    private final MutableLiveData<Result> userFavoriteNewsMutableLiveData;
    private final MutableLiveData<Result> userPreferencesMutableLiveData;

    public UserRepository(BaseUserAuthenticationRemoteDataSource userRemoteDataSource,
                          BaseUserDataRemoteDataSource userDataRemoteDataSource,
                          BaseTravelLocalDataSource travelLocalDataSource) {
        this.userRemoteDataSource = userRemoteDataSource;
        this.userDataRemoteDataSource = userDataRemoteDataSource;
        this.travelLocalDataSource = travelLocalDataSource;
        this.userMutableLiveData = new MutableLiveData<>();
        this.userPreferencesMutableLiveData = new MutableLiveData<>();
        this.userFavoriteNewsMutableLiveData = new MutableLiveData<>();
        this.userRemoteDataSource.setUserResponseCallback(this);
        this.userDataRemoteDataSource.setUserResponseCallback(this);
        this.travelLocalDataSource.setTravelCallback(this);
    }

    @Override
    public MutableLiveData<Result> getUser(String email, String password, boolean isUserRegistered) {
        if (isUserRegistered) {
            signIn(email, password);
        } else {
            signUp(email, password);
        }
        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getGoogleUser(String idToken) {
        signInWithGoogle(idToken);
        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getUserSavedTravels(String idToken) {
        userDataRemoteDataSource.getUserSavedTravels(idToken);
        return userFavoriteNewsMutableLiveData;
    }


    /*
    @Override
    public MutableLiveData<Result> getUserPreferences(String idToken) {
        userDataRemoteDataSource.getUserPreferences(idToken);
        return userPreferencesMutableLiveData;
    }

     */

    @Override
    public User getLoggedUser() {
        return userRemoteDataSource.getLoggedUser();
    }

    @Override
    public MutableLiveData<Result> logout() {
        userRemoteDataSource.logout();
        return userMutableLiveData;
    }

    @Override
    public void signUp(String email, String password) {
        userRemoteDataSource.signUp(email, password);
    }

    @Override
    public void signIn(String email, String password) {
        userRemoteDataSource.signIn(email, password);
    }

    @Override
    public void signInWithGoogle(String token) {
        userRemoteDataSource.signInWithGoogle(token);
    }

    /*
    @Override
    public void saveUserPreferences(String favoriteCountry, Set<String> favoriteTopics, String idToken) {
        userDataRemoteDataSource.saveUserPreferences(favoriteCountry, favoriteTopics, idToken);
    }

     */

    @Override
    public void onSuccessFromAuthentication(User user) {
        if (user != null) {
            userDataRemoteDataSource.saveUserData(user);
        }
    }

    @Override
    public void onFailureFromAuthentication(String message) {
        Result.Error result = new Result.Error(message);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromRemoteDatabase(User user) {
        Result.UserResponseSuccess result = new Result.UserResponseSuccess(user);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromRemoteDatabase(List<Travel> travelsList) {
        travelLocalDataSource.insertTravels(travelsList);
    }

    @Override
    public void onSuccessFromGettingUserPreferences() {
        userPreferencesMutableLiveData.postValue(new Result.UserResponseSuccess(null));
    }

    @Override
    public void onFailureFromRemoteDatabase(String message) {
        Result.Error result = new Result.Error(message);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessLogout() {
        travelLocalDataSource.deleteAll();
    }

    @Override
    public void onSuccessFromRemote(List<Travel> travelList, long lastUpdate) {

    }

    @Override
    public void onFailureFromRemote(Exception exception) {

    }

    @Override
    public void onSuccessFromLocal(List<Travel> travelList) {
        Log.d("UNCLE","PEAR");

    }

    @Override
    public void onFailureFromLocal(Exception exception) {

    }

    @Override
    public void onTravelSavedStatusChanged(Travel travel, List<Travel> savedTravels) {

    }

    @Override
    public void onTravelSavedStatusChanged(List<Travel> travelList) {

    }

    @Override
    public void onDeleteFavoriteNewsSuccess(List<Travel> travelList) {

    }

    @Override
    public void onSuccessFromCloudReading(List<Travel> travelList) {

    }

    @Override
    public void onSuccessFromCloudWriting(Travel travel) {

    }

    @Override
    public void onFailureFromCloud(Exception exception) {

    }

    @Override
    public void onSuccessSynchronization() {

    }

    @Override
    public void onSuccessDeletion() {

    }

    @Override
    public void onSuccessFromBookedCloudReading(List<Travel> travelList) {

    }

    @Override
    public void onDeleteFavoriteNewsSuccess(List<Travel> travelList, Travel deletedTravel) {

    }
}
